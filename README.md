# 花海摄影系统

花海摄影工作室预约与运营管理系统，包含用户端、管理端和 Spring Boot 后端。

## 目录

- `client_code`：用户端，默认端口 8083
- `manage_code`：管理端，默认端口 8082
- `server_code`：后端服务，默认端口 8080，接口前缀 `/cl10989512`
- `server_code/migrations`：数据库迁移脚本

## 本地运行

1. 创建 MySQL 数据库 `fang`，执行项目数据库脚本和 `server_code/migrations` 下的迁移脚本。
2. 在后端配置数据库连接。不要把真实密码或 API Key 提交到仓库。
3. 在 `server_code` 执行 `mvn spring-boot:run`。
4. 在 `manage_code` 执行 `npm install && npm run serve`。
5. 在 `client_code` 执行 `npm install && npm run serve`。

管理端：`http://localhost:8082`
用户端：`http://localhost:8083`

## AI 客服

通过环境变量配置：`AI_ENABLED`、`AI_API_KEY`、`AI_MODEL`、`AI_SEARCH_ENABLED`。默认不启用外部模型，避免密钥误用。

## 安全说明

数据库密码、智谱 API Key、生产环境配置和用户数据不应提交到 Git 仓库。生产部署时请使用环境变量或服务器私有配置文件。
