package com.cl;
import com.cl.controller.*;
import com.cl.entity.DiscusssheyingshixinxiEntity;
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
class OrderReviewTest {
 @Autowired JdbcTemplate jdbc;
 @Autowired SheyingshiyuyueController orders;
 @Autowired DiscusssheyingshixinxiController reviews;
 MockHttpServletRequest user(String role,String account){MockHttpServletRequest r=new MockHttpServletRequest();r.getSession().setAttribute("tableName",role);r.getSession().setAttribute("username",account);return r;}
 @Test void completedOwnerOnlyAndOnce(){
  long id=-System.currentTimeMillis();String account="review-test-"+UUID.randomUUID(),photographer=account+"-staff";
  jdbc.update("INSERT INTO yonghu(id,yonghuzhanghao,mima,yonghuxingming) VALUES (?,?,?,?)",id,account,"unused-test-password","测试用户");
  jdbc.update("INSERT INTO sheyingshixinxi(id,sheyingzhanghao) VALUES (?,?)",id,photographer);
  jdbc.update("INSERT INTO sheyingshiyuyue(id,yonghuzhanghao,sheyingzhanghao,sfsh,ispay) VALUES (?,?,?,'是','已支付')",id,account,photographer);
  jdbc.update("INSERT INTO booking_fulfillment(booking_id,stage) VALUES (?,'delivered')",id);
  Map<String,Object> form=new HashMap<>();form.put("score",5);form.put("content","很好<script>alert(1)</script>");form.put("images","");
  assertEquals(400,orders.reviewOrder(id,form,user("yonghu",account)).get("code"));
  jdbc.update("UPDATE booking_fulfillment SET stage='completed' WHERE booking_id=?",id);
  assertEquals(403,orders.reviewOrder(id,form,user("yonghu","other")).get("code"));
  form.put("score",6);assertEquals(400,orders.reviewOrder(id,form,user("yonghu",account)).get("code"));form.put("score",5);
  assertEquals(0,orders.reviewOrder(id,form,user("yonghu",account)).get("code"));
  assertEquals(409,orders.reviewOrder(id,form,user("yonghu",account)).get("code"));
  Long reviewId=jdbc.queryForObject("SELECT id FROM discusssheyingshixinxi WHERE booking_id=?",Long.class,id);
  String text=jdbc.queryForObject("SELECT content FROM discusssheyingshixinxi WHERE id=?",String.class,reviewId);assertTrue(text.contains("&lt;script&gt;"));
  DiscusssheyingshixinxiEntity reply=new DiscusssheyingshixinxiEntity();reply.setId(reviewId);reply.setReply("谢谢认可");
  assertEquals(403,reviews.update(reply,user("sheyingshi","other")).get("code"));
  assertEquals(0,reviews.update(reply,user("sheyingshi",photographer)).get("code"));
  assertEquals(400,reviews.delete(new Long[]{reviewId},user("yonghu",account)).get("code"));
  assertEquals(1,jdbc.queryForObject("SELECT discuss_number FROM sheyingshixinxi WHERE id=?",Integer.class,id));
 }
}
