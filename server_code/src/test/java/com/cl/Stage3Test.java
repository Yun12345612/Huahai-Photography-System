package com.cl;
import com.cl.controller.*;
import com.cl.service.BusinessContextService;
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
class Stage3Test {
 @Autowired JdbcTemplate jdbc;
 @Autowired Stage3DashboardController dashboard;
 @Autowired BusinessContextService business;
 @Autowired OperationLogController logs;
 @Autowired SheyingshiyuyueController bookings;
 MockHttpServletRequest user(String role,String account){MockHttpServletRequest r=new MockHttpServletRequest();r.getSession().setAttribute("tableName",role);r.getSession().setAttribute("username",account);return r;}
 @Test void statisticsAndBusinessQueriesStayWithinAccount(){
 String a="stage3-"+UUID.randomUUID();long id=-System.currentTimeMillis();
 jdbc.update("INSERT INTO sheyingshiyuyue(id,yonghuzhanghao,sheyingzhanghao,sheyingxingming,sfsh,ispay,yuyuejiage,addtime) VALUES (?,?,?,'测试摄影师','是','已支付',100,NOW())",id,a,a);
 jdbc.update("INSERT INTO booking_fulfillment(booking_id,stage) VALUES (?,'completed')",id);
 assertEquals(403,dashboard.summary(user("yonghu",a)).get("code"));
 assertEquals(403,dashboard.trend(7,user("yonghu",a)).get("code"));
 assertEquals(403,dashboard.photographers(user("yonghu",a)).get("code"));
 Map<?,?> s=(Map<?,?>)dashboard.summary(user("sheyingshi",a)).get("data");assertEquals(1,((Number)s.get("total")).intValue());assertEquals(100.0,((Number)s.get("net")).doubleValue());
 assertEquals(0,((Number)((Map<?,?>)dashboard.summary(user("sheyingshi",a+"other")).get("data")).get("total")).intValue());
 assertFalse(((List<?>)dashboard.trend(7,user("sheyingshi",a)).get("data")).isEmpty());
 List<?> staff=(List<?>)dashboard.photographers(user("sheyingshi",a)).get("data");assertEquals(1,((Number)((Map<?,?>)staff.get(0)).get("completed")).intValue());
 assertTrue(business.answer("我的订单状态",a).contains(String.valueOf(id)));assertFalse(business.answer("我的订单状态",a+"other").contains(String.valueOf(id)));
 assertNull(business.answer("你好",a));assertEquals(403,logs.list(1,20,"",user("sheyingshi",a)).get("code"));
 assertEquals(0,logs.list(1,20,"not-found",user("users",a)).get("code"));
 }
 @Test void markAllReadOnlyTouchesCurrentReceiver(){String a="message-"+UUID.randomUUID();for(String name:Arrays.asList(a,a+"other"))jdbc.update("INSERT INTO system_message(receiver_type,receiver_account,title,content) VALUES ('yonghu',?,'测试','测试')",name);
 bookings.readAllMessages(user("yonghu",a));assertEquals(0,((Number)bookings.unreadMessages(user("yonghu",a)).get("data")).intValue());assertEquals(1,((Number)bookings.unreadMessages(user("yonghu",a+"other")).get("data")).intValue());assertEquals(1,((Number)bookings.messages(1,20,user("yonghu",a)).get("total")).intValue());}
}
