package com.cl.controller;
import com.cl.utils.R;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import java.nio.file.*;
import java.util.*;
import java.io.*;
import javax.imageio.ImageIO;

@RestController
@RequestMapping("/booking-photos")
public class BookingPhotoController {
 private final JdbcTemplate jdbc;
 private final Path directory=Paths.get("private-data","booking-photos").toAbsolutePath().normalize();
 public BookingPhotoController(JdbcTemplate jdbc){this.jdbc=jdbc;}
 private String role(HttpServletRequest r){return String.valueOf(r.getSession().getAttribute("tableName"));}
 private String account(HttpServletRequest r){return String.valueOf(r.getSession().getAttribute("username"));}
 private Map<String,Object> booking(Long id,HttpServletRequest r,boolean write){
  List<Map<String,Object>> rows=jdbc.queryForList("SELECT b.*,f.stage FROM sheyingshiyuyue b LEFT JOIN booking_fulfillment f ON f.booking_id=b.id WHERE b.id=?",id);
  if(rows.isEmpty())throw new ResponseStatusException(HttpStatus.NOT_FOUND,"预约不存在");
  Map<String,Object> b=rows.get(0);String role=role(r);
  boolean staff="users".equals(role)||("sheyingshi".equals(role)&&account(r).equals(b.get("sheyingzhanghao")));
  boolean customer="yonghu".equals(role)&&account(r).equals(b.get("yonghuzhanghao"));
  if(!staff&&(!customer||write||!Arrays.asList("delivered","completed","settled").contains(String.valueOf(b.get("stage")))))throw new ResponseStatusException(HttpStatus.FORBIDDEN,"无权访问此相册或尚未交付");
  return b;
 }
 @GetMapping("/{bookingId}")
 public R list(@PathVariable Long bookingId,HttpServletRequest r){booking(bookingId,r,false);return R.ok().put("data",jdbc.queryForList("SELECT id,booking_id,category,original_name,created_at FROM booking_photo WHERE booking_id=? ORDER BY id",bookingId));}
 @PostMapping("/{bookingId}")
 @Transactional
 public R upload(@PathVariable Long bookingId,@RequestParam MultipartFile file,@RequestParam String category,HttpServletRequest r)throws IOException{
  jdbc.queryForList("SELECT id FROM sheyingshiyuyue WHERE id=? FOR UPDATE",bookingId);
  Map<String,Object> b=booking(bookingId,r,true);
  if(!"shot".equals(b.get("stage")))return R.error(400,"请登记拍摄完成后上传，发布交付后不可修改相册");
  if(!Arrays.asList("original","retouched","behind").contains(category))return R.error(400,"照片分类无效");
  if(file.isEmpty()||file.getSize()>20*1024*1024)return R.error(400,"请上传20MB以内的照片");
  String name=Objects.toString(file.getOriginalFilename(),"photo.jpg");String lower=name.toLowerCase(Locale.ROOT);
  String ext=lower.endsWith(".png")?"png":(lower.endsWith(".jpg")||lower.endsWith(".jpeg"))?"jpg":null;
  if(ext==null)return R.error(400,"仅支持JPEG、PNG照片");
  try(InputStream in=file.getInputStream()){if(ImageIO.read(in)==null)return R.error(400,"文件不是有效照片");}
  Files.createDirectories(directory);String key=UUID.randomUUID().toString()+"."+ext;Path target=directory.resolve(key);
  try{try(InputStream source=file.getInputStream()){Files.copy(source,target);}jdbc.update("INSERT INTO booking_photo(booking_id,category,file_key,original_name,media_type) VALUES (?,?,?,?,?)",bookingId,category,key,name.substring(0,Math.min(name.length(),200)),"png".equals(ext)?"image/png":"image/jpeg");}catch(Exception e){Files.deleteIfExists(target);throw e;}
  return R.ok("照片已上传");
 }
 @GetMapping("/file/{id}")
 public ResponseEntity<byte[]> file(@PathVariable Long id,@RequestParam(defaultValue="false") boolean download,HttpServletRequest r)throws IOException{
  List<Map<String,Object>> rows=jdbc.queryForList("SELECT * FROM booking_photo WHERE id=?",id);if(rows.isEmpty())throw new ResponseStatusException(HttpStatus.NOT_FOUND,"照片不存在");Map<String,Object> photo=rows.get(0);booking(((Number)photo.get("booking_id")).longValue(),r,false);
  Path path=directory.resolve(String.valueOf(photo.get("file_key"))).normalize();if(!path.startsWith(directory)||!Files.exists(path))throw new ResponseStatusException(HttpStatus.NOT_FOUND,"照片文件不存在");
  HttpHeaders headers=new HttpHeaders();headers.setContentType(MediaType.parseMediaType(String.valueOf(photo.get("media_type"))));headers.setCacheControl("private, no-store");headers.set("X-Content-Type-Options","nosniff");headers.setContentDisposition(ContentDisposition.builder(download?"attachment":"inline").filename("photo-"+id+"."+("image/png".equals(photo.get("media_type"))?"png":"jpg")).build());return new ResponseEntity<>(Files.readAllBytes(path),headers,HttpStatus.OK);
 }
 @PostMapping("/{bookingId}/publish")
 @Transactional
 public R publish(@PathVariable Long bookingId,HttpServletRequest r){
  if(!"users".equals(role(r)))return R.error(403,"仅工作室管理员可以发布交付");
  jdbc.queryForList("SELECT id FROM sheyingshiyuyue WHERE id=? FOR UPDATE",bookingId);Map<String,Object> b=booking(bookingId,r,true);
  if(!"shot".equals(b.get("stage")))return R.error(400,"请先完成拍摄，勿重复发布");
  if(jdbc.queryForObject("SELECT COUNT(*) FROM booking_photo WHERE booking_id=?",Integer.class,bookingId)==0)return R.error(400,"请先上传照片");
  jdbc.update("UPDATE booking_fulfillment SET stage='delivered' WHERE booking_id=?",bookingId);
  jdbc.update("INSERT INTO system_message(receiver_type,receiver_account,title,content,booking_id,message_type) VALUES ('yonghu',?,?,?,?,?)",b.get("yonghuzhanghao"),"拍摄照片已交付","您的拍摄照片已交付，请进入订单相册查看、下载并确认服务完成。",bookingId,"photo-delivery");return R.ok("已交付并通知用户");
 }
}
