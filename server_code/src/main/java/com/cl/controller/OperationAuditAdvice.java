package com.cl.controller;
import com.cl.utils.R;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
@ControllerAdvice
public class OperationAuditAdvice implements ResponseBodyAdvice<Object> {
 private final JdbcTemplate jdbc; public OperationAuditAdvice(JdbcTemplate jdbc){this.jdbc=jdbc;}
 public boolean supports(MethodParameter p,Class<? extends HttpMessageConverter<?>> c){return R.class.isAssignableFrom(p.getParameterType());}
 public Object beforeBodyWrite(Object body,MethodParameter p,MediaType t,Class<? extends HttpMessageConverter<?>> c,ServerHttpRequest req,ServerHttpResponse res){
 if(!(body instanceof R)||!Integer.valueOf(0).equals(((R)body).get("code"))||!(req instanceof ServletServerHttpRequest))return body;
 HttpServletRequest r=((ServletServerHttpRequest)req).getServletRequest();String path=r.getRequestURI();
 if(!Arrays.asList("POST","PUT","DELETE").contains(r.getMethod())||!(path.contains("/sheyingshiyuyue/")||path.contains("/booking-refunds")||path.contains("/booking-photos")||path.contains("/discusssheyingshixinxi/"))||path.contains("/messages/"))return body;
 String role=Objects.toString(r.getSession().getAttribute("tableName"),"");if(role.isEmpty())return body;
 java.util.regex.Matcher matcher=java.util.regex.Pattern.compile("/(\\d+)(?:/|$)").matcher(path);Long target=matcher.find()?Long.valueOf(matcher.group(1)):null;
 String action=Objects.toString(((R)body).get("msg"),p.getMethod().getName());if(action.length()>100)action=action.substring(0,100);
 jdbc.update("INSERT INTO operation_log(operator_type,operator_account,action,target_type,target_id,detail) VALUES (?,?,?,?,?,?)",role,Objects.toString(r.getSession().getAttribute("username"),""),action,path.contains("booking-refunds")?"退款申请":path.contains("booking-photos")?"照片交付":path.contains("discuss")?"评价":"预约订单",target,path+"；"+Objects.toString(((R)body).get("msg"),"操作成功"));
 if("yonghu".equals(role)&&(path.endsWith("/change-request")||path.matches(".*/booking-refunds/booking/\\d+"))){
  String kind=path.contains("booking-refunds")?"refund-request":"booking-change";
  for(Map<String,Object> admin:jdbc.queryForList("SELECT username FROM users"))jdbc.update("INSERT INTO system_message(receiver_type,receiver_account,title,content,booking_id,message_type) VALUES ('users',?,?,?,?,?)",admin.get("username"),"有新的待处理申请",action+"，请前往"+(kind.equals("refund-request")?"退款管理":"预约变更申请")+"核对处理。",kind.equals("refund-request")?target:null,kind);
 }
 return body;
 }
}
