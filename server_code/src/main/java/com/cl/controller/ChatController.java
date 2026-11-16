package com.cl.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.cl.config.AiProperties;
import com.cl.entity.ChatEntity;
import com.cl.service.AiCustomerService;
import com.cl.service.AiModelClient;
import com.cl.service.ChatService;
import com.cl.utils.MPUtil;
import com.cl.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired private ChatService chatService;
    @Autowired private JdbcTemplate jdbc;
    @Autowired private com.cl.service.BusinessContextService businessContext;
    @Autowired private AiCustomerService aiCustomerService;
    @Autowired private AiProperties aiProperties;

    private void ensure(Long id) { jdbc.update("INSERT IGNORE INTO chat_session(userid) VALUES (?)", id); }
    private String mode(Long id) { ensure(id); return jdbc.queryForObject("SELECT mode FROM chat_session WHERE userid=?", String.class, id); }

    @GetMapping("/session")
    public R session(HttpServletRequest r) {
        if (!customer(r)) return R.error(403, "仅用户可查看自己的接待状态");
        return R.ok().put("data", Collections.singletonMap("mode", mode(userId(r))));
    }

    @PostMapping("/handoff")
    @Transactional
    public synchronized R handoff(HttpServletRequest r) {
        if (!customer(r)) return R.error(403, "仅用户可申请人工");
        Long id = userId(r);
        if (!"bot".equals(mode(id))) return R.ok("已在人工接待流程中");
        jdbc.update("UPDATE chat_session SET mode='waiting', ai_enabled=0 WHERE userid=?", id);
        systemReply(id, "已为您转接人工客服，客服看到后会在这里回复。您可以继续补充需求。", "system");

        return R.ok();
    }

    @PostMapping("/session/{id}/{action}")
    @Transactional
    public synchronized R sessionAction(@PathVariable Long id, @PathVariable String action, HttpServletRequest r) {
        if (!admin(r)) return R.error(403, "仅管理员可管理接待");
        if (chatService.selectCount(new EntityWrapper<ChatEntity>().eq("userid", id)) == 0) return R.error(404, "会话不存在");
        if (!"accept".equals(action) && !"close".equals(action)) return R.error(400, "无效操作");
        ensure(id);
        if ("accept".equals(action)) {
            jdbc.update("UPDATE chat_session SET mode='human', ai_enabled=0, assigned_admin_id=? WHERE userid=?", userId(r), id);
            systemReply(id, "人工客服已接入，您好，请问有什么可以帮您？", "system");
        } else {
            jdbc.update("UPDATE chat_session SET mode='bot', ai_enabled=1, assigned_admin_id=NULL, closed_at=NOW() WHERE userid=?", id);
            systemReply(id, "本次人工接待已结束，已切换为智能助手。如需帮助可再次转人工。", "system");
            chatService.updateForSet("isreply=0", new EntityWrapper<ChatEntity>().eq("userid", id));
        }
        return R.ok();
    }

    @GetMapping("/conversations")
    public R conversations(HttpServletRequest r) {
        if (!admin(r)) return R.error(403, "仅管理员可查看客户会话");
        String sql = "SELECT c.userid, COALESCE(NULLIF(y.yonghuxingming,''),y.yonghuzhanghao,CONCAT('客户 ',c.userid)) AS name, COALESCE(s.mode,'bot') AS mode, COALESCE(c.ask,c.reply) AS preview,c.addtime, (SELECT COUNT(*) FROM chat q WHERE q.userid=c.userid AND q.isreply=1) AS pending FROM chat c JOIN (SELECT userid,MAX(id) id FROM chat WHERE userid IS NOT NULL GROUP BY userid) latest ON latest.id=c.id LEFT JOIN chat_session s ON s.userid=c.userid LEFT JOIN yonghu y ON y.id=c.userid ORDER BY (s.mode='waiting') DESC,c.id DESC";
        return R.ok().put("data", jdbc.queryForList(sql));
    }

    private boolean requestsHuman(String text) {
        String value = text == null ? "" : text.replaceAll("\\s+", "");
        if (value.matches(".*(不需要转人工|不用转人工|不要转人工|暂时不转人工|先不转人工|暂时不需要人工|不需要人工客服|不想转人工|不需要人工|不用人工|不要人工|不想找人工|不用联系人工|不要联系人工).*")) return false;
        return value.matches(".*(转人工|转接人工|人工客服|找人工|联系人工|人工接待|真人客服|我要人工|需要人工|联系一下客服).*");
    }
    private void systemReply(Long id, String text, String sender) {
        ChatEntity row = new ChatEntity();
        row.setUserid(id); row.setAdminid(0L); row.setReply(text); row.setIsreply(0); row.setSenderType(sender); row.setAddtime(new Date());
        chatService.insert(row);
    }

    private boolean admin(HttpServletRequest r) { return "users".equals(r.getSession().getAttribute("tableName")); }
    private boolean customer(HttpServletRequest r) { return "yonghu".equals(r.getSession().getAttribute("tableName")); }
    private Long userId(HttpServletRequest r) { return ((Number) r.getSession().getAttribute("userId")).longValue(); }
    private boolean allowed(ChatEntity row, HttpServletRequest r) { return row != null && (admin(r) || (customer(r) && userId(r).equals(row.getUserid()))); }
    private Wrapper<ChatEntity> scope(Wrapper<ChatEntity> ew, HttpServletRequest r) {
        if (admin(r)) return ew;
        return customer(r) ? ew.eq("userid", userId(r)) : ew.eq("id", -1);
    }

    @RequestMapping({"/page", "/list"})
    public R page(@RequestParam Map<String, Object> params, ChatEntity row, HttpServletRequest r) {
        Wrapper<ChatEntity> ew = MPUtil.between(MPUtil.likeOrEq(new EntityWrapper<ChatEntity>(), row), params);
        return R.ok().put("data", chatService.queryPage(params, MPUtil.sort(scope(ew, r), params)));
    }

    @RequestMapping("/lists")
    public R lists(ChatEntity row, HttpServletRequest r) {
        return R.ok().put("data", chatService.selectListView(scope(MPUtil.likeOrEq(new EntityWrapper<ChatEntity>(), row), r)));
    }

    @RequestMapping("/query")
    public R query(ChatEntity row, HttpServletRequest r) {
        return R.ok().put("data", chatService.selectView(scope(MPUtil.likeOrEq(new EntityWrapper<ChatEntity>(), row), r)));
    }

    @RequestMapping({"/info/{id}", "/detail/{id}"})
    public R info(@PathVariable Long id, HttpServletRequest r) {
        ChatEntity row = chatService.selectById(id);
        return allowed(row, r) ? R.ok().put("data", row) : R.error(403, "无权查看此咨询记录");
    }

    @RequestMapping({"/save", "/add"})
    public synchronized R send(@RequestBody ChatEntity input, HttpServletRequest r) {
        if (!admin(r) && !customer(r)) return R.error(403, "请使用用户账号咨询，管理员账号回复");
        boolean humanReply = admin(r);
        String text = humanReply ? input.getReply() : input.getAsk();
        if (text == null || text.trim().isEmpty()) return R.error(400, "消息不能为空");
        text = text.trim();
        if (text.length() > 2000) return R.error(400, "消息不能超过2000字");
        if (!humanReply && input.getReply() != null && !input.getReply().trim().isEmpty()) return R.error(403, "用户不能发送客服回复");

        Long recipient = humanReply ? input.getUserid() : userId(r);
        if (recipient == null) return R.error(400, "请选择咨询用户");
        if (humanReply && chatService.selectCount(new EntityWrapper<ChatEntity>().eq("userid", recipient)) == 0) return R.error(400, "该用户尚未发起咨询");

        String currentMode = mode(recipient);
        boolean handoffRequested = !humanReply && requestsHuman(text);
        if (!humanReply && "closed".equals(currentMode)) return R.error(400, "本次会话已结束，请重新发起咨询");
        if (!humanReply && "bot".equals(currentMode) && !handoffRequested && !aiCustomerService.allowRequest(recipient)) return R.error(429, "发送过于频繁，请稍后再试或转人工");

        List<ChatEntity> history = Collections.emptyList();
        if (!humanReply && "bot".equals(currentMode) && !handoffRequested) {
            int limit = Math.max(1, Math.min(aiProperties.getMaxHistoryMessages(), 30));
            history = chatService.selectList(new EntityWrapper<ChatEntity>().eq("userid", recipient).orderBy("id", false).last("LIMIT " + limit));
            Collections.reverse(history);
        }

        chatService.updateForSet("isreply=0", new EntityWrapper<ChatEntity>().eq("userid", recipient));
        ChatEntity row = new ChatEntity();
        row.setUserid(recipient); row.setAddtime(new Date());
        if (humanReply) {
            row.setReply(text); row.setAdminid(userId(r)); row.setIsreply(0); row.setSenderType("admin");
            chatService.insert(row);
            jdbc.update("UPDATE chat_session SET mode='human', ai_enabled=0, assigned_admin_id=? WHERE userid=?", userId(r), recipient);
            return R.ok("消息已发送");
        }

        row.setAsk(text); row.setIsreply(1); row.setSenderType("user");
        chatService.insert(row);
        if ("bot".equals(currentMode) && handoffRequested) {
            jdbc.update("UPDATE chat_session SET mode='waiting', ai_enabled=0, assigned_admin_id=NULL WHERE userid=?", recipient);
            systemReply(recipient, "已为您转接人工客服，客服看到后会在这里回复。您可以继续补充需求。", "system");
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("mode", "waiting");
            data.put("handoff", true);
            return R.ok("已申请人工客服").put("data", data);
        }
        if ("bot".equals(currentMode)) {
            String businessAnswer=businessContext.answer(text,String.valueOf(r.getSession().getAttribute("username")));
            AiModelClient.Reply reply = businessAnswer==null?aiCustomerService.answer(text, history):new AiModelClient.Reply(businessAnswer,"business-query",null,null,null);
            String answer = reply.getText();
            if (answer.length() > 2000) answer = answer.substring(0, 2000);
            ChatEntity bot = new ChatEntity();
            bot.setUserid(recipient); bot.setAdminid(0L); bot.setReply(answer); bot.setIsreply(0); bot.setSenderType("ai"); bot.setModelName(reply.getModel()); bot.setPromptTokens(reply.getPromptTokens()); bot.setCompletionTokens(reply.getCompletionTokens()); bot.setRequestId(reply.getRequestId()); bot.setAddtime(new Date());
            chatService.insert(bot);
            chatService.updateForSet("isreply=0", new EntityWrapper<ChatEntity>().eq("userid", recipient));
        }
        return R.ok("消息已发送");
    }

    @RequestMapping("/update")
    public R update(@RequestBody ChatEntity row, HttpServletRequest r) { return R.error(403, "咨询消息不能修改，请发送新消息"); }

    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids, HttpServletRequest r) {
        if (!admin(r)) return R.error(403, "仅管理员可删除咨询记录");
        chatService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
}