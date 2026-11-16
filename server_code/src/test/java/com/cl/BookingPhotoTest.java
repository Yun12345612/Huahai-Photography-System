package com.cl;
import com.cl.controller.BookingPhotoController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.server.ResponseStatusException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(properties={"booking.reminders.enabled=false","ai.enabled=false"})
@Transactional
class BookingPhotoTest {
 @Autowired JdbcTemplate jdbc; @Autowired BookingPhotoController controller;
 MockHttpServletRequest user(String role,String account){MockHttpServletRequest r=new MockHttpServletRequest();r.getSession().setAttribute("tableName",role);r.getSession().setAttribute("username",account);return r;}
 @Test void uploadPublishPreviewAndPrivacy()throws Exception{
  long id=-System.currentTimeMillis();String account="photo-test-"+UUID.randomUUID();
  jdbc.update("INSERT INTO sheyingshiyuyue(id,yonghuzhanghao,sheyingzhanghao,sfsh,ispay) VALUES (?,?,?,'是','已支付')",id,account,"photo-staff");
  jdbc.update("INSERT INTO booking_fulfillment(booking_id,stage) VALUES (?,'shot')",id);
  ByteArrayOutputStream out=new ByteArrayOutputStream();ImageIO.write(new BufferedImage(2,2,BufferedImage.TYPE_INT_RGB),"png",out);
  MockMultipartFile image=new MockMultipartFile("file","test.png","image/png",out.toByteArray());
  assertThrows(ResponseStatusException.class,()->controller.list(id,user("yonghu",account)));
  assertThrows(ResponseStatusException.class,()->controller.upload(id,image,"retouched",user("sheyingshi","other")));
  assertEquals(400,controller.upload(id,new MockMultipartFile("file","fake.png","image/png",new byte[]{1,2,3}),"retouched",user("users","admin")).get("code"));
  try{
   assertEquals(0,controller.upload(id,image,"retouched",user("sheyingshi","photo-staff")).get("code"));
   long photo=jdbc.queryForObject("SELECT id FROM booking_photo WHERE booking_id=?",Long.class,id);
   assertEquals(403,controller.publish(id,user("sheyingshi","photo-staff")).get("code"));
   assertEquals(0,controller.publish(id,user("users","admin")).get("code"));
   assertEquals(400,controller.publish(id,user("users","admin")).get("code"));
   assertArrayEquals(out.toByteArray(),controller.file(photo,false,user("yonghu",account)).getBody());
   assertThrows(ResponseStatusException.class,()->controller.file(photo,false,user("yonghu","other")));
   assertEquals(1,jdbc.queryForObject("SELECT COUNT(*) FROM system_message WHERE booking_id=? AND message_type='photo-delivery'",Integer.class,id));
   assertEquals("delivered",jdbc.queryForObject("SELECT stage FROM booking_fulfillment WHERE booking_id=?",String.class,id));
   assertEquals(400,controller.upload(id,image,"retouched",user("users","admin")).get("code"));
  }finally{for(String key:jdbc.queryForList("SELECT file_key FROM booking_photo WHERE booking_id=?",String.class,id))Files.deleteIfExists(Paths.get("private-data","booking-photos",key));}
 }
}
