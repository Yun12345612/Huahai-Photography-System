# 花海摄影工作室 AI 客服实现文档

## 1. 目标与边界

将现有在线咨询升级为“AI 自动接待 + 用户转人工 + 管理员接管”。首期 AI 只回答业务问题和引导用户，不直接确认预约、改价、退款、审核或改变支付状态；这些动作仍由现有后端业务接口完成。

项目现有基础：

- 用户端：`client_code/src/components/OnlineConsultation.vue`
- 管理端：`manage_code/src/views/chat/list.vue`
- 后端：`server_code/src/main/java/com/cl/controller/ChatController.java`
- 会话迁移：`server_code/migrations/20261106_chat_session.sql`
- 消息表：`chat`；会话模式表：`chat_session`

## 2. 目标流程

```text
用户消息 → 后端鉴权/限流 → 保存用户消息 → 读取知识和历史 → 调模型
        → 保存 AI 回复 → 返回前端
用户转人工 → mode=waiting → 管理员接入 → mode=human，AI 暂停
管理员结束接待 → 恢复 bot 或关闭会话
```

模型 API Key 只能放后端服务器环境变量，不能写入 Vue、浏览器可访问配置或 Git 仓库。国内模型服务可能提供试用额度，但额度、有效期、并发限制和超额计费政策会变化，接入前须在对应供应商控制台核对并设置预算/限额。

## 3. 第一步：整理客服知识

新建 `server_code/src/main/resources/ai/huahai-customer-service.txt`，维护预约、付款、改期/取消/退款、摄影师和作品、活动报名、照片交付、人工客服等业务规则。

预约和支付知识至少写清：用户提交预约后暂不付款；工作室确认档期、套餐、价格和服务内容；用户确认后向工作室商户账户付款；拍摄完成后由工作室交付照片。摄影师不直接向用户收款。

系统提示词还要明确：AI 不得声称已经预约、付款、退款或审核；对退款、投诉、特殊报价、改期以及不确定问题建议转人工；不编造档期、价格和政策。

## 4. 第二步：配置模型供应商

在 `server_code/src/main/resources/application.yml` 增加配置，密钥通过运行环境注入：

```yaml
ai:
  enabled: false
  provider: ${AI_PROVIDER:bailian}
  base-url: ${AI_BASE_URL:}
  api-key: ${AI_API_KEY:}
  model: ${AI_MODEL:}
  timeout-ms: 20000
  max-output-tokens: 800
  temperature: 0.3
```

`enabled` 默认关闭。无 API Key、模型超时或供应商故障时，退回本地规则回复，确保在线咨询仍可使用。建议支持可配置的 OpenAI 兼容接口，供应商/模型/地址不要写死在业务代码中。

## 5. 第三步：增加后端模型调用层

建议新增：

- `AiProperties`：读取模型配置。
- `AiModelClient`：服务端发 HTTP 请求、认证和解析响应。
- `AiCustomerService`：处理知识、历史消息、提示词、降级和安全规则。
- `AiCustomerServiceController`：对前端提供 AI 客服 API。

调用层需设置连接/读取超时，限制输入历史长度和输出 Token，处理超时、429、5xx、空响应和无效 JSON。日志仅记录 request ID、模型、耗时、用量和错误类别，不记录 API Key。不要把内部异常堆栈返回用户。

## 6. 第四步：数据库变更

现有 `chat_session` 已保存 `userid`、`mode`、`updated_at`；`chat` 保存 `userid`、`ask`、`reply`、`adminid`、`isreply`、`addtime`。建议为会话增加：

```sql
ALTER TABLE chat_session
  ADD COLUMN ai_enabled TINYINT NOT NULL DEFAULT 1,
  ADD COLUMN assigned_admin_id BIGINT NULL,
  ADD COLUMN last_intent VARCHAR(64) NULL,
  ADD COLUMN closed_at DATETIME NULL;
```

为消息增加：

```sql
ALTER TABLE chat
  ADD COLUMN sender_type VARCHAR(16) NULL,
  ADD COLUMN model_name VARCHAR(80) NULL,
  ADD COLUMN prompt_tokens INT NULL,
  ADD COLUMN completion_tokens INT NULL,
  ADD COLUMN request_id VARCHAR(80) NULL;
```

`mode` 取值：`bot`、`waiting`、`human`、`closed`。`sender_type` 取值：`user`、`ai`、`admin`、`system`。迁移放入 `server_code/migrations/`，执行前备份数据库；如果迁移可能重复执行，要加版本记录或字段存在性检查。

## 7. 第五步：新增聊天 API

接口：

```http
POST /cl10989512/ai/customer-service/chat
Token: <用户 Token>
Content-Type: application/json
```

请求：

```json
{"message":"我想预约人像摄影师"}
```

响应：

```json
{"code":0,"data":{"reply":"可以先选择摄影师并查看可用档期。","mode":"bot","canTransferHuman":true,"requestId":"..."}}
```

后端处理顺序：

1. 从已经验证的 Token/Session 读取用户 ID，禁止信任前端传入的 `userid`。
2. 检查角色、消息非空、字符数上限和会话归属。
3. 读取或建立 `chat_session`。
4. `mode=human` 时不调用模型；`mode=waiting` 时保存补充消息但不自动回复。
5. 按用户限流，例如每分钟 10 次；阈值做成配置。
6. 保存用户消息，`sender_type=user`。
7. 读取最近 10 至 20 条历史消息和客服知识，限制总字符数。
8. 调用模型；模型关闭或异常时走本地规则回复/转人工提示。
9. 清理输出长度和格式，保存 AI 消息、模型、request ID 和用量。
10. 返回回复、当前模式和是否可转人工。

同一会话需避免并发请求导致回复错序。可按会话串行处理，或用数据库锁/队列，并为重复请求设计幂等 ID。

## 8. 第六步：人工接管

复用并扩展现有 `/chat/handoff` 和 `/chat/session/{userid}/{action}`：

- 用户 `handoff`：只允许用户操作自己的会话；原子地从 `bot` 改为 `waiting`，`ai_enabled=0`，写系统消息。重复调用返回当前状态，不重复插入转接消息。
- 管理员 `accept`：只允许管理员；设置 `mode=human`、`assigned_admin_id`、`ai_enabled=0`。
- 管理员 `close`：将模式设为 `bot` 或 `closed`，按产品规则恢复 AI，写系统消息和结束时间。
- 管理员回复：写入 `sender_type=admin`；用户只能读自己的消息。

后台会话列表需标识 `bot`、`waiting`、`human`、`closed`，并优先展示等待接待会话。

## 9. 第七步：前端改造

### 用户端 `OnlineConsultation.vue`

- 发送消息改为调用 AI 客服接口。
- 显示用户、智能助手/系统、人工客服的消息来源。
- 保留现有历史记录、快捷问题和“转人工”。
- 请求期间显示加载状态并防止重复发送。
- 模型超时不清空用户草稿，提示重试或转人工。
- `waiting`、`human` 模式下不调用 AI。

### 管理端 `manage_code/src/views/chat/list.vue`

- 显示会话模式和接待管理员。
- `waiting` 显示“接入人工”，`human` 显示“结束接待”。
- 人工接管后明确提示 AI 自动回复已暂停。
- 人工回复以 `sender_type=admin` 保存。
- 保留 Enter 发送、Shift+Enter 换行和现有媒体消息展示。

## 10. 第八步：安全、隐私和费用控制

后端必须实现：

- API Key 仅存在服务端环境变量/密钥配置中。
- 只允许登录用户调用；用户只能读取自己的会话。
- 普通用户不能调用管理员接管、会话管理接口。
- 摄影师不能读取客户全量会话。
- 单条消息建议不超过 2000 字，历史消息数和总字符数均设上限。
- 限制请求频率、输出 Token 和日/月用量；配置预算告警。
- 不向模型发送密码、Token、支付凭证、身份证等敏感信息。
- 人工接管后不调用模型。
- 日志不记录密钥；对话内容设置访问权限和保留期限。
- 模型服务失败时降级到现有规则回复，客服页面保持可用。

## 11. 第九步：测试与上线

功能测试：预约咨询、支付流程、改期/退款转人工、快捷问题、聊天持久化、转人工、管理员接管、人工回复、结束接待、恢复 AI。

权限测试：未登录返回 401；用户读取其他用户消息返回 403；用户调用管理员接管接口返回 403；摄影师不能读取全量客户会话。

故障测试：Key 缺失、超时、429、5xx、空响应和数据库错误；验证降级和人工入口可用。

上线顺序：

1. 备份数据库并执行迁移。
2. 后端新增配置和调用层，保持 `ai.enabled=false`。
3. 先验证鉴权、规则降级和转人工。
4. 在服务端配置 Key，在测试环境启用模型。
5. 检查浏览器请求/构建产物没有 Key。
6. 小范围开放，观察失败率、延迟、转人工率和 Token 用量。
7. 验收后逐步扩大开放范围。

## 12. 验收标准

- 模型请求只由后端发起，前端拿不到 API Key。
- AI 回复、人工回复和系统消息来源清楚，刷新后历史仍在。
- 人工模式下 AI 不抢答，结束后按规则恢复或关闭。
- 预约、支付、审核仍由现有业务接口执行，AI 不伪造执行结果。
- 会话权限隔离正确，模型失败时客服仍可用。
- 能查看模型请求次数、错误、耗时及 Token 用量。