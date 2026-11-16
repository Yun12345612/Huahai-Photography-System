package com.cl.service;

import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;
import java.time.*;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@EnableScheduling
public class BookingReminderService {
    private static final Logger log=LoggerFactory.getLogger(BookingReminderService.class);
    private final JdbcTemplate jdbc;
    private final TransactionTemplate transaction;
    @Value("${booking.reminders.enabled:true}") private boolean enabled;
    public BookingReminderService(JdbcTemplate jdbc,org.springframework.transaction.PlatformTransactionManager manager){this.jdbc=jdbc;this.transaction=new TransactionTemplate(manager);}
    @Scheduled(initialDelay=15000, fixedDelay=60000)
    public void scheduled(){
        if(!enabled)return;
        try{dispatchFor(LocalDate.now(ZoneId.of("Asia/Shanghai")));}
        catch(Exception e){log.error("Booking reminder generation failed",e);}
    }
    public void dispatchFor(LocalDate today){
        java.sql.Date tomorrow=java.sql.Date.valueOf(today.plusDays(1));
        List<Long> ids=jdbc.queryForList("SELECT id FROM sheyingshiyuyue WHERE yuyuesheyingriqi>=? AND yuyuesheyingriqi<DATE_ADD(?,INTERVAL 1 DAY) AND sfsh='是'",Long.class,tomorrow,tomorrow);
        for(Long id:ids)transaction.execute(status->{
            List<Map<String,Object>> bookings=jdbc.queryForList("SELECT b.*,f.stage,f.package_name,f.service_content FROM sheyingshiyuyue b LEFT JOIN booking_fulfillment f ON f.booking_id=b.id WHERE b.id=? AND b.yuyuesheyingriqi>=? AND b.yuyuesheyingriqi<DATE_ADD(?,INTERVAL 1 DAY) AND b.sfsh='是' FOR UPDATE",id,tomorrow,tomorrow);
            if(bookings.isEmpty())return null;
            Map<String,Object> b=bookings.get(0);
            if(Arrays.asList("shot","delivered","settled","cancelled").contains(String.valueOf(b.get("stage"))))return null;
            String slot=Objects.toString(b.get("yuyueshijianduan"),"待确认时段");
            String content="明天（"+tomorrow+"）"+slot+"有拍摄安排。摄影师："+Objects.toString(b.get("sheyingxingming"),"待确认")+"；套餐："+Objects.toString(b.get("package_name"),"以订单确认为准")+"。请提前核对地点、服装及拍摄需求，按时到场。若有改期或取消，以订单最新安排为准。";
            send(b,"yonghu","yonghuzhanghao",tomorrow,slot,content);
            send(b,"sheyingshi","sheyingzhanghao",tomorrow,slot,content);
            return null;
        });
    }
    private void send(Map<String,Object> b,String role,String field,java.sql.Date day,String slot,String content){
        Object account=b.get(field);if(account==null)return;
        String key="shoot:"+b.get("id")+":"+day+":"+slot+":"+role;
        jdbc.update("INSERT IGNORE INTO system_message(receiver_type,receiver_account,title,content,booking_id,message_type,reminder_key) VALUES (?,?,?,?,?,?,?)",role,account,"明日拍摄提醒",content,b.get("id"),"shoot-reminder",key);
    }
}
