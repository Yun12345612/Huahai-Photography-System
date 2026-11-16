package com.cl.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cl.config.AiProperties;
import com.cl.entity.ChatEntity;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AiCustomerService {
    private static final Logger logger = LoggerFactory.getLogger(AiCustomerService.class);
    private static final String FALLBACK = "智能问答服务暂时不可用，请稍后重试。若需处理摄影预约或订单，可点击转人工联系工作室。";
    private final AiProperties properties;
    private final AiModelClient modelClient;
    private final ConcurrentHashMap<Long, Deque<Long>> requestTimes = new ConcurrentHashMap<Long, Deque<Long>>();
    private volatile String knowledge;

    public AiCustomerService(AiProperties properties, AiModelClient modelClient) {
        this.properties = properties;
        this.modelClient = modelClient;
    }

    public boolean allowRequest(Long userId) {
        long now = System.currentTimeMillis();
        Deque<Long> queue = requestTimes.computeIfAbsent(userId, k -> new ArrayDeque<Long>());
        synchronized (queue) {
            while (!queue.isEmpty() && now - queue.peekFirst() >= 60000L) queue.removeFirst();
            if (queue.size() >= Math.max(1, properties.getMaxRequestsPerMinute())) return false;
            queue.addLast(now);
            return true;
        }
    }

    public AiModelClient.Reply answer(String question, List<ChatEntity> history) {
        String clockAnswer = clockAnswer(question);
        if (clockAnswer != null) return new AiModelClient.Reply(clockAnswer, "system-clock", null, null, null);
        String local = fallbackAnswer(question);
        if (!properties.isEnabled()) return new AiModelClient.Reply(local, "local-rules", null, null, null);
        try {
            List<Map<String, String>> messages = new ArrayList<Map<String, String>>();
            messages.add(message("system", systemPrompt()));
            for (ChatEntity item : history) {
                if (item.getAsk() != null && !item.getAsk().trim().isEmpty()) messages.add(message("user", item.getAsk()));
                if (item.getReply() != null && !item.getReply().trim().isEmpty()) messages.add(message("assistant", item.getReply()));
            }
            String userPrompt = question;
            AiModelClient.SearchResults search = null;
            if (properties.isSearchEnabled() && requiresWebSearch(question)) {
                search = modelClient.search(question);
                if (search != null) userPrompt += "\n\n以下是联网搜索到的近期网页资料，请据此回答，并保留准确来源；资料不足时说明，不要臆测：\n" + search.getContext();
            }
            messages.add(message("user", userPrompt));
            int total = 0;
            List<Map<String, String>> bounded = new ArrayList<Map<String, String>>();
            bounded.add(messages.get(0));
            for (int i = messages.size() - 1; i > 0 && bounded.size() < Math.max(1, properties.getMaxHistoryMessages()) + 1; i--) {
                Map<String, String> item = messages.get(i);
                int length = item.get("content").length();
                if (total + length > properties.getMaxInputCharacters()) continue;
                bounded.add(1, item);
                total += length;
            }
            AiModelClient.Reply reply = modelClient.complete(bounded);
            if (reply != null && search != null && !search.getSources().isEmpty()) {
                StringBuilder answer = new StringBuilder(reply.getText()).append("\n\n参考来源：");
                for (int i = 0; i < search.getSources().size(); i++) {
                    answer.append("\n").append(i + 1).append(". ").append(search.getSources().get(i));
                }
                reply = new AiModelClient.Reply(answer.toString(), reply.getModel(), reply.getPromptTokens(), reply.getCompletionTokens(), reply.getRequestId());
            }
            return reply == null ? new AiModelClient.Reply(local, "local-rules", null, null, null) : reply;
        } catch (Exception e) {
            return new AiModelClient.Reply(local, "local-fallback", null, null, null);
        }
    }

    private Map<String, String> message(String role, String content) {
        Map<String, String> result = new HashMap<String, String>();
        result.put("role", role);
        result.put("content", content);
        return result;
    }

    private String systemPrompt() {
        String now = ZonedDateTime.now(ZoneId.of("Asia/Shanghai")).format(DateTimeFormatter.ofPattern("yyyy年M月d日 EEEE HH:mm:ss"));
        String base = "当前中国标准时间（Asia/Shanghai）是：" + now + "。用户询问当前日期、时间或年份时，直接依据这个时间回答，不要声称无法获取。"
                + "你是花海摄影系统的中文智能助手，也是一名通用知识助手。回答简洁、友好，按问题需要提供解释和步骤。"
                + "用户咨询天文、地理、历史、科学、编程、学习、日常生活等非摄影话题时，直接运用通用知识回答，不必强行引回摄影，也不要因话题与摄影无关而拒答。"
                + "对于知识类问题，不确定时坦诚说明，不编造事实或引用来源。本系统可按需调用后端联网搜索；只有未获取到搜索资料时，涉及实时天气、最新新闻、当前行情等外部实时信息才说明无法核实，不得猜测。当前日期和时间由系统提供，不属于需要联网查询的信息。"
                + "用户咨询本系统摄影业务时，优先依据下方业务知识回答；这些业务规则仅适用于花海摄影系统，不要套用到其他领域的问题。"
                + "不得编造本系统的档期、价格、政策或用户订单状态；未提供的数据应建议从对应页面查看或联系人工核实。不得声称已经预约、支付、退款、审核或修改订单。"
                + "本系统预约流程：用户提交预约后暂不付款；工作室确认档期、套餐、价格和服务内容；用户确认后付款给工作室商户账户；完成拍摄后由工作室交付照片。摄影师不直接收款。"
                + "本系统退款、改期、投诉、特殊报价、订单变更需人工核实。通用知识问题不应一律引导转人工。用户明确要求人工服务时，配合系统转人工流程，不得虚构人工已经接入。"
                + "不索取密码或验证码，不泄露他人隐私。\n\n以下为花海摄影系统业务知识：\n";
        return base + loadKnowledge();
    }

    private boolean requiresWebSearch(String question) {
        if (question == null) return false;
        String text = question.replaceAll("\\s+", "");
        return text.matches(".*(新闻|天气|股价|股票行情|汇率|实时|最新|今日|近期|最新消息|当前.*(情况|政策|动态|消息)|今天.*(发生|情况|新闻|天气|比赛|事件)|现在.*(行情|政策|动态)).*");
    }
    private String clockAnswer(String question) {
        String text = question == null ? "" : question.replaceAll("[\\s，。？！、,.?!]", "");
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));
        if (text.matches(".*(现在几点|现在时间|当前时间|现在是几点|几点钟了|几点了).*")) {
            return "现在是北京时间 " + now.format(DateTimeFormatter.ofPattern("HH:mm")) + "。";
        }
        if (text.matches(".*(今年是哪一年|今年几几年|现在是哪一年|现在几年|今年年份|今年是几几年).*")) {
            return "今年是 " + now.getYear() + " 年。";
        }
        if (text.matches(".*(今天几号|今天日期|今天是几月几日|现在日期|当前日期).*")) {
            return "今天是 " + now.format(DateTimeFormatter.ofPattern("yyyy年M月d日 EEEE")) + "。";
        }
        return null;
    }
    private String loadKnowledge() {
        if (knowledge != null) return knowledge;
        synchronized (this) {
            if (knowledge != null) return knowledge;
            try (InputStream input = new ClassPathResource("ai/huahai-customer-service.txt").getInputStream(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[1024]; int count;
                while ((count = input.read(buffer)) > 0) out.write(buffer, 0, count);
                knowledge = new String(out.toByteArray(), StandardCharsets.UTF_8);
            } catch (Exception e) { knowledge = "预约及价格以页面和工作室确认为准；退款和订单变更请联系人工客服。"; }
            return knowledge;
        }
    }

    private String fallbackAnswer(String text) {
        if (text.matches(".*(退款|投诉|改期|取消|改价).*")) return "这类问题需要核实您的预约和服务情况，请点击“转人工”，工作室客服会协助处理。智能助手不会直接修改订单或承诺退款。";
        if (text.matches(".*(支付|付款|收款|钱).*")) return "请等待工作室确认档期、套餐和服务内容后再付款，款项支付给工作室商户账户，不需要直接转给摄影师。";
        if (text.matches(".*(预约|档期|时间).*")) return "请在摄影师详情页选择日期和可用时段，填写联系方式后提交预约。工作室确认档期与服务内容后，您再确认付款。";
        if (text.matches(".*(价格|费用|多少钱|收费).*")) return "价格以摄影师详情页和工作室最终确认的套餐为准。您可以先浏览摄影师作品；需要定制报价时请转人工。";
        if (text.matches(".*(风格|作品|拍摄).*")) return "您可以在摄影作品页面查看样片，并在摄影师信息页了解擅长风格。告诉我拍摄用途和喜欢的风格，我可以帮您梳理选择方向。";
        if (text.matches(".*(你好|您好|hello|嗨).*")) return "您好，我是花海智能助手。摄影预约、学习、科学、生活等问题都可以问我；需要工作室工作人员协助时可以转人工。";
        return FALLBACK;
    }
}