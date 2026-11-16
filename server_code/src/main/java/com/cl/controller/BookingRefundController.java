package com.cl.controller;
import com.cl.utils.R;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/booking-refunds")
public class BookingRefundController {
 private final JdbcTemplate jdbc;
 public BookingRefundController(JdbcTemplate jdbc){this.jdbc=jdbc;}
 private String role(HttpServletRequest r){return String.valueOf(r.getSession().getAttribute("tableName"));}
 private String account(HttpServletRequest r){return String.valueOf(r.getSession().getAttribute("username"));}
 private boolean staff(HttpServletRequest r){return Arrays.asList("users","sheyingshi").contains(role(r));}
 @GetMapping("/booking/{id}")
 public R detail(@PathVariable Long id,HttpServletRequest r){
  List<Map<String,Object>> rows=jdbc.queryForList("SELECT yonghuzhanghao,sheyingzhanghao FROM sheyingshiyuyue WHERE id=?",id);
  if(rows.isEmpty())return R.error(404,"订单不存在");
  Map<String,Object> b=rows.get(0);
  boolean allowed="users".equals(role(r))||("yonghu".equals(role(r))&&account(r).equals(b.get("yonghuzhanghao")))||("sheyingshi".equals(role(r))&&account(r).equals(b.get("sheyingzhanghao")));
  if(!allowed)return R.error(403,"无权查看退款记录");
  List<Map<String,Object>> refunds=jdbc.queryForList("SELECT * FROM booking_refund WHERE booking_id=?",id);
  return R.ok().put("data",refunds.isEmpty()?null:refunds.get(0));
 }
 @PostMapping("/booking/{id}")
 @Transactional
 public R apply(@PathVariable Long id,@RequestBody Map<String,String> input,HttpServletRequest r){
  if(!"yonghu".equals(role(r)))return R.error(403,"仅订单用户可以申请退款");
  List<Map<String,Object>> rows=jdbc.queryForList("SELECT b.*,f.stage FROM sheyingshiyuyue b LEFT JOIN booking_fulfillment f ON f.booking_id=b.id WHERE b.id=? FOR UPDATE",id);
  if(rows.isEmpty()||!account(r).equals(rows.get(0).get("yonghuzhanghao")))return R.error(403,"无权申请此订单退款");
  Map<String,Object> b=rows.get(0);
  if(!"已支付".equals(b.get("ispay")))return R.error(400,"仅已付款订单可以申请退款，未付款请申请取消预约");
  if("否".equals(b.get("sfsh"))||Arrays.asList("shot","delivered","completed","settled","cancelled").contains(String.valueOf(b.get("stage"))))return R.error(400,"已取消或已开始履约的订单请联系工作室协商处理");
  String reason=Objects.toString(input.get("reason"),"").trim();
  if(reason.isEmpty()||reason.length()>500)return R.error(400,"请填写500字以内退款原因");
  if(jdbc.queryForObject("SELECT COUNT(*) FROM booking_change_request WHERE booking_id=? AND status='pending'",Integer.class,id)>0)return R.error(409,"请先处理正在审核的改期或取消申请");
  List<Map<String,Object>> existing=jdbc.queryForList("SELECT id,status FROM booking_refund WHERE booking_id=? FOR UPDATE",id);
  if(!existing.isEmpty())return R.error(409,"该订单已有退款记录，请查看处理进度或联系工作室");
  BigDecimal amount;try{amount=new BigDecimal(String.valueOf(b.get("yuyuejiage"))).setScale(2,java.math.RoundingMode.HALF_UP);}catch(Exception e){return R.error(400,"订单金额无效，请联系工作室");}
  if(amount.signum()<=0)return R.error(400,"订单金额无效，请联系工作室");
  jdbc.update("INSERT INTO booking_refund(booking_id,amount,reason) VALUES (?,?,?)",id,amount,reason);
  notifyUser(b,id,"退款申请已提交","工作室将审核您的退款申请，申请金额 ¥"+amount+"。审核期间暂不变更拍摄安排。", "refund-request");
  return R.ok("退款申请已提交");
 }
 @GetMapping
 public R list(@RequestParam(defaultValue="all") String status,@RequestParam(defaultValue="1") int page,HttpServletRequest r){
  if(!staff(r))return R.error(403,"仅工作人员可以查看退款管理");
  if(!Arrays.asList("all","pending","approved","rejected","completed").contains(status))return R.error(400,"状态无效");
  String sql=" FROM booking_refund f JOIN sheyingshiyuyue b ON b.id=f.booking_id WHERE 1=1";
  List<Object> args=new ArrayList<>();
  if("sheyingshi".equals(role(r))){sql+=" AND b.sheyingzhanghao=?";args.add(account(r));}
  if(!"all".equals(status)){sql+=" AND f.status=?";args.add(status);}
  Long total=jdbc.queryForObject("SELECT COUNT(*)"+sql,Long.class,args.toArray());
  args.add((Math.max(1,page)-1)*20);
  return R.ok().put("total",total).put("data",jdbc.queryForList("SELECT f.*,b.yonghuxingming,b.sheyingxingming,b.ispay,b.yuyuesheyingriqi,b.yuyueshijianduan"+sql+" ORDER BY f.id DESC LIMIT 20 OFFSET ?",args.toArray()));
 }
 @PostMapping("/{id}/{action}")
 @Transactional
 public R handle(@PathVariable Long id,@PathVariable String action,@RequestBody Map<String,String> input,HttpServletRequest r){
  if(!"users".equals(role(r)))return R.error(403,"仅工作室管理员可以审核或登记退款");
  if(!Arrays.asList("approve","reject","complete").contains(action))return R.error(400,"操作无效");
  List<Long> ids=jdbc.queryForList("SELECT booking_id FROM booking_refund WHERE id=?",Long.class,id);
  if(ids.isEmpty())return R.error(404,"退款申请不存在");
  Long bookingId=ids.get(0);
  List<Map<String,Object>> bookings=jdbc.queryForList("SELECT b.*,f.stage FROM sheyingshiyuyue b LEFT JOIN booking_fulfillment f ON f.booking_id=b.id WHERE b.id=? FOR UPDATE",bookingId);
  if(bookings.isEmpty())return R.error(404,"关联订单不存在");
  Map<String,Object> b=bookings.get(0),refund=jdbc.queryForMap("SELECT * FROM booking_refund WHERE id=? FOR UPDATE",id);
  String status=String.valueOf(refund.get("status")),note=Objects.toString(input.get("note"),"").trim();
  if(note.isEmpty()||note.length()>500)return R.error(400,"请填写500字以内处理说明");
  if("complete".equals(action)){
   if(!"approved".equals(status))return R.error(409,"仅审核通过的申请可以登记退款完成，请勿重复处理");
   jdbc.update("UPDATE booking_refund SET status='completed',result_note=?,completed_at=NOW(),handled_by=? WHERE id=?",note,account(r),id);
   notifyUser(b,bookingId,"退款处理完成","工作室已登记退款完成，金额 ¥"+refund.get("amount")+"。处理说明："+note,"refund-completed");
  }else{
   if(!"pending".equals(status))return R.error(409,"该申请已审核，请勿重复处理");
   if("approve".equals(action)){
    if(!"已支付".equals(b.get("ispay"))||Arrays.asList("shot","delivered","completed","settled").contains(String.valueOf(b.get("stage"))))return R.error(400,"订单状态已变化，不能批准退款，请核对订单");
    jdbc.update("UPDATE sheyingshiyuyue SET sfsh='否',shhf='退款申请已批准，预约已取消' WHERE id=?",bookingId);
    jdbc.update("INSERT INTO booking_fulfillment(booking_id,stage) VALUES (?,'cancelled') ON DUPLICATE KEY UPDATE stage='cancelled'",bookingId);
   }
   jdbc.update("UPDATE booking_refund SET status=?,admin_note=?,handled_by=?,handled_at=NOW() WHERE id=?","approve".equals(action)?"approved":"rejected",note,account(r),id);
   notifyUser(b,bookingId,"退款审核结果","您的退款申请已"+("approve".equals(action)?"批准，预约已取消，等待工作室登记退款结果":"驳回，原预约继续保留")+"。处理说明："+note,"refund-result");
  }
  return R.ok("退款记录已更新");
 }
 private void notifyUser(Map<String,Object> b,Long booking,String title,String text,String type){
  jdbc.update("INSERT INTO system_message(receiver_type,receiver_account,title,content,booking_id,message_type) VALUES ('yonghu',?,?,?,?,?)",b.get("yonghuzhanghao"),title,text,booking,type);
 }
}
