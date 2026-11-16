package com.cl;
import com.cl.controller.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(properties={"booking.reminders.enabled=false","ai.enabled=false"})
@Transactional
class BookingRefundTest {
 @Autowired JdbcTemplate jdbc;
 @Autowired BookingRefundController refunds;
 @Autowired SheyingshiyuyueController bookings;
 MockHttpServletRequest user(String role,String account){MockHttpServletRequest r=new MockHttpServletRequest();r.getSession().setAttribute("tableName",role);r.getSession().setAttribute("username",account);return r;}
 @Test void fullRefundLifecyclePreservesPaymentAndReleasesSlot(){
  long id=-System.currentTimeMillis();String account="refund-test-"+UUID.randomUUID();
  jdbc.update("INSERT INTO sheyingshiyuyue(id,yonghuzhanghao,sheyingzhanghao,sfsh,ispay,yuyuejiage,yuyuesheyingriqi,yuyueshijianduan) VALUES (?,?,?,'是','已支付',2000,'2099-01-01','08:00-09:00')",id,account,account+"-staff");
  jdbc.update("INSERT INTO booking_fulfillment(booking_id,stage) VALUES (?,'confirmed')",id);
  MockHttpServletRequest owner=user("yonghu",account),admin=user("users","test-admin"),staff=user("sheyingshi",account+"-staff");
  Map<String,String> input=new HashMap<>();input.put("reason","拍摄计划变化");input.put("amount","0.01");
  assertEquals(403,refunds.apply(id,input,user("yonghu","other")).get("code"));
  assertEquals(0,refunds.apply(id,input,owner).get("code"));
  assertEquals(409,refunds.apply(id,input,owner).get("code"));
  Long refund=jdbc.queryForObject("SELECT id FROM booking_refund WHERE booking_id=?",Long.class,id);
  assertEquals(2000.0,jdbc.queryForObject("SELECT amount FROM booking_refund WHERE id=?",Double.class,refund));
  assertEquals(403,refunds.detail(id,user("yonghu","other")).get("code"));
  assertEquals(0L,((Number)refunds.list("all",1,user("sheyingshi","other")).get("total")).longValue());
  assertEquals(409,bookings.advance(id,"shoot",new HashMap<>(),admin).get("code"));
  Map<String,String> note=Collections.singletonMap("note","已核对订单，全额批准");
  assertEquals(403,refunds.handle(refund,"approve",note,staff).get("code"));
  assertEquals(0,refunds.handle(refund,"approve",note,admin).get("code"));
  assertEquals("否",jdbc.queryForObject("SELECT sfsh FROM sheyingshiyuyue WHERE id=?",String.class,id));
  assertEquals("cancelled",jdbc.queryForObject("SELECT stage FROM booking_fulfillment WHERE booking_id=?",String.class,id));
  assertEquals("已支付",jdbc.queryForObject("SELECT ispay FROM sheyingshiyuyue WHERE id=?",String.class,id));
  assertTrue(((List<?>)bookings.availability(account+"-staff","2099-01-01").get("data")).isEmpty());
  assertEquals(409,refunds.handle(refund,"approve",note,admin).get("code"));
  assertEquals(0,refunds.handle(refund,"complete",note,admin).get("code"));
  assertEquals(409,refunds.handle(refund,"complete",note,admin).get("code"));
  assertEquals(3,jdbc.queryForObject("SELECT COUNT(*) FROM system_message WHERE booking_id=?",Integer.class,id));
  assertEquals(400,bookings.delete(new Long[]{id},admin).get("code"));
 }
 @Test void rejectKeepsBookingAndUnpaidCannotApply(){
  long id=-System.currentTimeMillis()-2;String account="reject-test-"+UUID.randomUUID();
  jdbc.update("INSERT INTO sheyingshiyuyue(id,yonghuzhanghao,sfsh,ispay,yuyuejiage) VALUES (?,?,'是','未支付',100)",id,account);
  jdbc.update("INSERT INTO booking_fulfillment(booking_id,stage) VALUES (?,'confirmed')",id);
  MockHttpServletRequest owner=user("yonghu",account),admin=user("users","test-admin");
  Map<String,String> input=Collections.singletonMap("reason","取消计划");
  assertEquals(400,refunds.apply(id,input,owner).get("code"));
  jdbc.update("UPDATE sheyingshiyuyue SET ispay='已支付' WHERE id=?",id);
  assertEquals(0,refunds.apply(id,input,owner).get("code"));
  Long refund=jdbc.queryForObject("SELECT id FROM booking_refund WHERE booking_id=?",Long.class,id);
  assertEquals(0,refunds.handle(refund,"reject",Collections.singletonMap("note","请补充情况并联系工作室"),admin).get("code"));
  assertEquals("是",jdbc.queryForObject("SELECT sfsh FROM sheyingshiyuyue WHERE id=?",String.class,id));
  assertEquals("confirmed",jdbc.queryForObject("SELECT stage FROM booking_fulfillment WHERE booking_id=?",String.class,id));
  assertEquals(409,refunds.handle(refund,"complete",Collections.singletonMap("note","不应允许处理"),admin).get("code"));
 }
}
