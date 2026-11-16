package com.cl.service;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.*;
@Service
public class BusinessContextService {
 private final JdbcTemplate jdbc; public BusinessContextService(JdbcTemplate jdbc){this.jdbc=jdbc;}
 public String answer(String question,String account){
 if(question!=null&&(question.contains("档期")||question.contains("有空"))){
 java.util.regex.Matcher date=java.util.regex.Pattern.compile("(20\\d{2}-\\d{2}-\\d{2})").matcher(question);
 if(date.find()){
 String day=date.group(1);try{java.time.LocalDate.parse(day);}catch(Exception e){return "请提供有效日期，例如 2026-12-01。";}
 List<Map<String,Object>> people=jdbc.queryForList("SELECT DISTINCT sheyingzhanghao,sheyingxingming FROM sheyingshixinxi WHERE sfsh='是'");
 for(Map<String,Object> p:people){String name=Objects.toString(p.get("sheyingxingming"),"");if(name.isEmpty()||!question.contains(name))continue;
 List<Map<String,Object>> slots=jdbc.queryForList("SELECT yuyueshijianduan FROM sheyingshiyuyue WHERE sheyingzhanghao=? AND yuyuesheyingriqi=? AND (sfsh IS NULL OR sfsh<>'否')",p.get("sheyingzhanghao"),day);
 StringBuilder result=new StringBuilder(name+"在"+day+"的已占用时段：");if(slots.isEmpty())result.append("暂无预约占用");else for(Map<String,Object> slot:slots)result.append("\n").append(slot.get("yuyueshijianduan"));return result.append("\n仅供档期参考，最终以提交预约后工作室确认为准。").toString();
 }
 return "请提供摄影师的完整姓名，以便查询这一天的档期。";
 }else if(!question.contains("我的"))return "查询档期请提供摄影师完整姓名和日期（例如：某某 2026-12-01 有档期吗）。";
 }
 if(question==null||!question.matches("(?s).*(我的|本人).*(预约|订单|退款|交付|照片|拍摄|档期).*"))return null;
 List<Map<String,Object>> rows=jdbc.queryForList("SELECT b.id,b.sheyingxingming,b.yuyuesheyingriqi,b.yuyueshijianduan,b.sfsh,b.ispay,f.stage,(SELECT status FROM booking_refund x WHERE x.booking_id=b.id LIMIT 1) refund FROM sheyingshiyuyue b LEFT JOIN booking_fulfillment f ON f.booking_id=b.id WHERE b.yonghuzhanghao=? ORDER BY b.id DESC LIMIT 10",account);
 if(rows.isEmpty())return "目前没有查询到您名下的预约订单。您可以前往摄影师页面选择档期提交预约。";
 StringBuilder out=new StringBuilder("已查询您最近的预约订单：\n");
 for(Map<String,Object>x:rows){String stage=Objects.toString(x.get("stage"),"pending");Map<String,String> labels=new HashMap<>();labels.put("pending","待确认");labels.put("confirmed","已确认");labels.put("shot","已拍摄，待交付");labels.put("delivered","已交付");labels.put("completed","已完成");labels.put("settled","已完成");labels.put("cancelled","已取消");out.append("\n订单 ").append(x.get("id")).append("｜摄影师：").append(x.get("sheyingxingming")).append("\n拍摄：").append(x.get("yuyuesheyingriqi")).append(" ").append(x.get("yuyueshijianduan")).append("\n状态：").append("否".equals(x.get("sfsh"))?"已取消":labels.getOrDefault(stage,"待确认")).append("，").append(x.get("ispay"));if(x.get("refund")!=null){Map<String,String> refunds=new HashMap<>();refunds.put("pending","待审核");refunds.put("approved","已批准，待登记退款");refunds.put("rejected","已驳回");refunds.put("completed","已登记退款完成");out.append("，退款：").append(refunds.getOrDefault(String.valueOf(x.get("refund")),"处理中"));}out.append("\n");}
 return out.append("\n以上来自您当前账号的业务记录。办理付款、改期、取消、退款请进入我的订单操作。").toString();
 }
}
