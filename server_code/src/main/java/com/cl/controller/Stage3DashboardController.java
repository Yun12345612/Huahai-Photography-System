package com.cl.controller;

import com.cl.utils.R;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/stage3/dashboard")
public class Stage3DashboardController {
 private final JdbcTemplate jdbc;
 public Stage3DashboardController(JdbcTemplate jdbc){this.jdbc=jdbc;}
 private String role(HttpServletRequest r){return String.valueOf(r.getSession().getAttribute("tableName"));}
 private String account(HttpServletRequest r){return String.valueOf(r.getSession().getAttribute("username"));}
 private String filter(HttpServletRequest r,List<Object> args){if("sheyingshi".equals(role(r))){args.add(account(r));return " AND b.sheyingzhanghao=?";}return "";}
 @GetMapping("/summary") public R summary(HttpServletRequest r){
  if(!Arrays.asList("users","sheyingshi").contains(role(r)))return R.error(403,"仅工作人员可查看统计");
  List<Object> a=new ArrayList<>(); String f=filter(r,a);
  Map<String,Object> m=new LinkedHashMap<>();
  m.put("total",jdbc.queryForObject("SELECT COUNT(*) FROM sheyingshiyuyue b WHERE 1=1"+f,Integer.class,a.toArray()));
  m.put("pending",jdbc.queryForObject("SELECT COUNT(*) FROM sheyingshiyuyue b WHERE b.sfsh='待审核'"+f,Integer.class,a.toArray()));
  m.put("paid",jdbc.queryForObject("SELECT COALESCE(SUM(b.yuyuejiage),0) FROM sheyingshiyuyue b WHERE b.ispay='已支付'"+f,Double.class,a.toArray()));
  m.put("refunded",jdbc.queryForObject("SELECT COALESCE(SUM(f.amount),0) FROM booking_refund f JOIN sheyingshiyuyue b ON b.id=f.booking_id WHERE f.status='completed'"+f,Double.class,a.toArray()));
  m.put("changeRequests",jdbc.queryForObject("SELECT COUNT(*) FROM booking_change_request f JOIN sheyingshiyuyue b ON b.id=f.booking_id WHERE f.status='pending'"+f,Integer.class,a.toArray()));
  m.put("delivery",jdbc.queryForObject("SELECT COUNT(*) FROM booking_fulfillment f JOIN sheyingshiyuyue b ON b.id=f.booking_id WHERE f.stage='shot'"+f,Integer.class,a.toArray()));
  m.put("refundPending",jdbc.queryForObject("SELECT COUNT(*) FROM booking_refund f JOIN sheyingshiyuyue b ON b.id=f.booking_id WHERE f.status='pending'"+f,Integer.class,a.toArray()));
  m.put("net",((Number)m.get("paid")).doubleValue()-((Number)m.get("refunded")).doubleValue()); return R.ok().put("data",m);
 }
 @GetMapping("/trend") public R trend(@RequestParam(defaultValue="7") int days,HttpServletRequest r){if(!Arrays.asList("users","sheyingshi").contains(role(r)))return R.error(403,"仅工作人员可查看统计");days=Math.min(30,Math.max(7,days));List<Object>a=new ArrayList<>();String f=filter(r,a);String sql="SELECT DATE_FORMAT(b.addtime,'%Y-%m-%d') day,COUNT(*) total,SUM(CASE WHEN EXISTS(SELECT 1 FROM booking_fulfillment x WHERE x.booking_id=b.id AND x.stage IN ('completed','settled')) THEN 1 ELSE 0 END) completed,SUM(CASE WHEN b.sfsh='否' THEN 1 ELSE 0 END) cancelled FROM sheyingshiyuyue b WHERE b.addtime>=DATE_SUB(CURDATE(),INTERVAL ? DAY) AND b.addtime<DATE_ADD(CURDATE(),INTERVAL 1 DAY)"+f+" GROUP BY DATE(b.addtime) ORDER BY day";a.add(0,days-1);return R.ok().put("data",jdbc.queryForList(sql,a.toArray()));}
 @GetMapping("/photographers") public R photographers(HttpServletRequest r){if(!Arrays.asList("users","sheyingshi").contains(role(r)))return R.error(403,"仅工作人员可查看统计");List<Object>a=new ArrayList<>();String f=filter(r,a);String sql="SELECT b.sheyingzhanghao account,b.sheyingxingming name,COUNT(*) bookings,SUM(CASE WHEN EXISTS(SELECT 1 FROM booking_fulfillment x WHERE x.booking_id=b.id AND x.stage IN ('completed','settled')) THEN 1 ELSE 0 END) completed,SUM(CASE WHEN b.ispay='已支付' THEN b.yuyuejiage ELSE 0 END) revenue,(SELECT ROUND(AVG(d.score),1) FROM discusssheyingshixinxi d JOIN sheyingshiyuyue z ON z.id=d.booking_id WHERE z.sheyingzhanghao=b.sheyingzhanghao) rating,SUM(CASE WHEN EXISTS(SELECT 1 FROM booking_fulfillment q WHERE q.booking_id=b.id AND q.stage='shot') THEN 1 ELSE 0 END) delivery FROM sheyingshiyuyue b WHERE 1=1"+f+" GROUP BY b.sheyingzhanghao,b.sheyingxingming ORDER BY bookings DESC";return R.ok().put("data",jdbc.queryForList(sql,a.toArray()));}
}
