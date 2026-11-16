package com.cl;
import com.cl.controller.SheyingshiyuyueController;
import com.cl.utils.R;
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
class OrderCenterTest {
 @Autowired JdbcTemplate jdbc;
 @Autowired SheyingshiyuyueController controller;
 MockHttpServletRequest user(String account){MockHttpServletRequest r=new MockHttpServletRequest();r.getSession().setAttribute("tableName","yonghu");r.getSession().setAttribute("username",account);return r;}
 @Test void ordersAreScopedAndCompletionRequiresDelivery(){
  String account="order-test-"+UUID.randomUUID();long id=-System.currentTimeMillis();
  jdbc.update("INSERT INTO sheyingshiyuyue(id,yonghuzhanghao,sfsh,ispay) VALUES (?,?,?,?)",id,account,"是","已支付");
  jdbc.update("INSERT INTO booking_fulfillment(booking_id,stage) VALUES (?,?)",id,"confirmed");
  R orders=controller.orders("shoot",1,user(account));assertEquals(1L,((Number)orders.get("total")).longValue());
  assertEquals(0L,((Number)controller.orders("all",1,user(account+"other")).get("total")).longValue());
  assertEquals(400,controller.completeOrder(id,user(account)).get("code"));
  jdbc.update("UPDATE booking_fulfillment SET stage='delivered' WHERE booking_id=?",id);
  assertEquals(403,controller.completeOrder(id,user(account+"other")).get("code"));
  assertEquals(0,controller.completeOrder(id,user(account)).get("code"));
  assertEquals("completed",jdbc.queryForObject("SELECT stage FROM booking_fulfillment WHERE booking_id=?",String.class,id));
  assertEquals("已支付",jdbc.queryForObject("SELECT ispay FROM sheyingshiyuyue WHERE id=?",String.class,id));
  assertEquals(400,controller.completeOrder(id,user(account)).get("code"));
  assertEquals(1L,((Number)controller.orders("completed",1,user(account)).get("total")).longValue());
 }
}
