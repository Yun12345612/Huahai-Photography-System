package com.cl.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.cl.annotation.IgnoreAuth;
import com.cl.entity.SheyingshiyuyueEntity;
import com.cl.service.SheyingshiyuyueService;
import com.cl.utils.MPUtil;
import com.cl.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.time.*;
import java.util.*;

/** Reservation access and scheduling rules shared by the front end and admin. */
@RestController
@RequestMapping("/sheyingshiyuyue")
public class SheyingshiyuyueController {
    @Autowired private SheyingshiyuyueService sheyingshiyuyueService;
    @Autowired private JdbcTemplate jdbc;
    private static final ZoneId ZONE = ZoneId.of("Asia/Shanghai");
    private static final List<String> SLOTS = Arrays.asList("08:00-09:00", "09:00-10:00", "10:00-11:00", "11:00-12:00", "13:00-14:00", "14:00-15:00", "15:00-16:00", "16:00-17:00");

    private String role(HttpServletRequest r) { return String.valueOf(r.getSession().getAttribute("tableName")); }
    private String username(HttpServletRequest r) { return String.valueOf(r.getSession().getAttribute("username")); }
    private boolean admin(HttpServletRequest r) { return "users".equals(role(r)); }
    private boolean allowed(SheyingshiyuyueEntity row, HttpServletRequest r) {
        return row != null && (admin(r) || ("yonghu".equals(role(r)) && username(r).equals(row.getYonghuzhanghao())) || ("sheyingshi".equals(role(r)) && username(r).equals(row.getSheyingzhanghao())));
    }
    private Wrapper<SheyingshiyuyueEntity> scope(EntityWrapper<SheyingshiyuyueEntity> ew, HttpServletRequest r) {
        if (admin(r)) return ew;
        if ("yonghu".equals(role(r))) return ew.eq("yonghuzhanghao", username(r));
        if ("sheyingshi".equals(role(r))) return ew.eq("sheyingzhanghao", username(r));
        return ew.eq("id", -1);
    }

    private boolean refundBlocks(Long id){return id!=null&&jdbc.queryForObject("SELECT COUNT(*) FROM booking_refund WHERE booking_id=? AND status IN ('pending','approved','completed')",Integer.class,id)>0;}
    @RequestMapping({"/page", "/list"})
    public R page(@RequestParam Map<String,Object> params, SheyingshiyuyueEntity row, HttpServletRequest r) {
        EntityWrapper<SheyingshiyuyueEntity> ew = new EntityWrapper<>();
        Wrapper<SheyingshiyuyueEntity> filtered = MPUtil.likeOrEq(ew, row);
        filtered = MPUtil.between(filtered, params);
        filtered = scope((EntityWrapper<SheyingshiyuyueEntity>) filtered, r);
        return R.ok().put("data", sheyingshiyuyueService.queryPage(params, MPUtil.sort(filtered, params)));
    }

    /** Public availability deliberately contains no customer or order fields. */
    @IgnoreAuth
    @GetMapping("/availability")
    public R availability(@RequestParam String sheyingzhanghao, @RequestParam String date) {
        LocalDate day;
        try { day = LocalDate.parse(date); } catch (RuntimeException e) { return R.error(400, "请选择有效日期"); }
        if (sheyingzhanghao.trim().isEmpty()) return R.error(400, "请先选择摄影师");
        List<String> occupied = jdbc.queryForList("SELECT DISTINCT yuyueshijianduan FROM sheyingshiyuyue WHERE sheyingzhanghao=? AND yuyuesheyingriqi=? AND (sfsh IS NULL OR sfsh<>'否')", String.class, sheyingzhanghao, java.sql.Date.valueOf(day));
        return R.ok().put("data", occupied);
    }

    @RequestMapping("/lists")
    public R lists(SheyingshiyuyueEntity row, HttpServletRequest r) {
        EntityWrapper<SheyingshiyuyueEntity> ew = new EntityWrapper<>();
        ew.allEq(MPUtil.allEQMapPre(row, MPUtil.camelToSnake("sheyingshiyuyue")));
        return R.ok().put("data", sheyingshiyuyueService.selectListView(scope(ew, r)));
    }
    @RequestMapping("/query")
    public R query(SheyingshiyuyueEntity row, HttpServletRequest r) {
        EntityWrapper<SheyingshiyuyueEntity> ew = new EntityWrapper<>();
        ew.allEq(MPUtil.allEQMapPre(row, MPUtil.camelToSnake("sheyingshiyuyue")));
        return R.ok().put("data", sheyingshiyuyueService.selectView(scope(ew, r)));
    }
    @RequestMapping({"/info/{id}", "/detail/{id}"})
    public R info(@PathVariable Long id, HttpServletRequest r) {
        SheyingshiyuyueEntity row = sheyingshiyuyueService.selectById(id);
        if (!allowed(row, r)) return R.error(403, "无权查看此预约");
        return R.ok().put("data", row);
    }

    private String dayKey(Date value) { return value.toInstant().atZone(ZONE).toLocalDate().toString(); }
    private boolean sameSlot(SheyingshiyuyueEntity a, SheyingshiyuyueEntity b) {
        return Objects.equals(a.getSheyingzhanghao(), b.getSheyingzhanghao()) && a.getYuyuesheyingriqi()!=null && b.getYuyuesheyingriqi()!=null && dayKey(a.getYuyuesheyingriqi()).equals(dayKey(b.getYuyuesheyingriqi())) && Objects.equals(a.getYuyueshijianduan(), b.getYuyueshijianduan());
    }
    private String validateSlot(SheyingshiyuyueEntity row, boolean newSlot) {
        if (row.getSheyingzhanghao()==null || row.getSheyingzhanghao().trim().isEmpty()) return "请选择摄影师";
        if (row.getYuyuesheyingriqi()==null || !SLOTS.contains(row.getYuyueshijianduan())) return "请选择有效日期和预约时段";
        LocalDate day = LocalDate.parse(dayKey(row.getYuyuesheyingriqi()));
        if (newSlot && !LocalDateTime.of(day, LocalTime.parse(row.getYuyueshijianduan().substring(0,5))).isAfter(LocalDateTime.now(ZONE))) return "请选择尚未开始的预约时段";
        if (row.getLianxidianhua()==null || !row.getLianxidianhua().matches("^1[3-9]\\d{9}$")) return "请输入正确的11位手机号码";
        return null;
    }
    /** Lock the photographer row until transaction commit, serializing competing bookings. */
    private List<Map<String,Object>> lockPhotographer(String account) {
        return jdbc.queryForList("SELECT id,sheyingxingming,yuyuejiage,sfsh FROM sheyingshixinxi WHERE sheyingzhanghao=? ORDER BY id FOR UPDATE", account);
    }
    private boolean conflict(SheyingshiyuyueEntity row) {
        Integer count = jdbc.queryForObject("SELECT COUNT(*) FROM sheyingshiyuyue WHERE sheyingzhanghao=? AND yuyuesheyingriqi=? AND yuyueshijianduan=? AND (sfsh IS NULL OR sfsh<>'否') AND id<>?", Integer.class, row.getSheyingzhanghao(), java.sql.Date.valueOf(dayKey(row.getYuyuesheyingriqi())), row.getYuyueshijianduan(), row.getId()==null ? -1L : row.getId());
        return count != null && count > 0;
    }

    @RequestMapping({"/save", "/add"})
    @Transactional
    public R save(@RequestBody SheyingshiyuyueEntity row, HttpServletRequest r) {
        if (!admin(r) && !"yonghu".equals(role(r))) return R.error(403, "仅用户或管理员可以创建预约");
        String error = validateSlot(row, true);
        if (error!=null) return R.error(400, error);
        List<Map<String,Object>> profiles = lockPhotographer(row.getSheyingzhanghao());
        Map<String,Object> profile = null;
        for (Map<String,Object> candidate : profiles) if ("是".equals(candidate.get("sfsh"))) { profile = candidate; break; }
        if (profile==null) return R.error(400, "该摄影师当前不可预约");
        if (conflict(row)) return R.error(409, "该时段已被预约，请选择其他时段");
        String account = admin(r) ? row.getYonghuzhanghao() : username(r);
        List<Map<String,Object>> customers = jdbc.queryForList("SELECT yonghuzhanghao,yonghuxingming FROM yonghu WHERE yonghuzhanghao=?", account);
        if (customers.isEmpty()) return R.error(400, "预约用户不存在");
        row.setId(null);
        row.setYonghuzhanghao(account);
        row.setYonghuxingming(String.valueOf(customers.get(0).get("yonghuxingming")));
        row.setSheyingxingming(String.valueOf(profile.get("sheyingxingming")));
        row.setYuyuejiage(((Number)profile.get("yuyuejiage")).doubleValue());
        row.setSfsh("待审核"); row.setShhf(null); row.setIspay("未支付"); row.setAddtime(new Date());
        sheyingshiyuyueService.insert(row);
        jdbc.update("INSERT IGNORE INTO booking_fulfillment(booking_id, stage) VALUES (?, 'pending')", row.getId());
        return R.ok("预约已提交，请等待确认");
    }

    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody SheyingshiyuyueEntity input, HttpServletRequest r) {
        SheyingshiyuyueEntity stored = input.getId()==null ? null : sheyingshiyuyueService.selectById(input.getId());
        if (!allowed(stored,r)) return R.error(403,"无权修改此预约");
        jdbc.queryForList("SELECT id FROM sheyingshiyuyue WHERE id=? FOR UPDATE",input.getId());
        if(refundBlocks(input.getId()))return R.error(409,"该订单正在退款或已退款，不能修改预约");
        // User edits cannot change identity, price, approval or payment state.
        if ("yonghu".equals(role(r))) {
            if (!"已支付".equals(stored.getIspay()) && "已支付".equals(input.getIspay())) return R.error(400,"请通过订单支付入口完成支付");
            if ("已支付".equals(stored.getIspay()) || "是".equals(stored.getSfsh())) return R.error(400,"已确认的预约请联系工作室调整");
            stored.setLianxidianhua(input.getLianxidianhua());
            stored.setYuyuesheyingriqi(input.getYuyuesheyingriqi());
            stored.setYuyueshijianduan(input.getYuyueshijianduan());
            input = stored;
        } else if ("sheyingshi".equals(role(r))) {
            stored.setSfsh(input.getSfsh()); stored.setShhf(input.getShhf()); input = stored;
        }
        if (!Arrays.asList("是","否","待审核").contains(input.getSfsh())) return R.error(400,"预约审核状态无效");
        String error=validateSlot(input,!sameSlot(input,sheyingshiyuyueService.selectById(input.getId())));
        if(error!=null) return R.error(400,error);
        if(lockPhotographer(input.getSheyingzhanghao()).isEmpty()) return R.error(400,"摄影师不存在");
        if(!"否".equals(input.getSfsh()) && conflict(input)) return R.error(409,"该时段已被预约，请选择其他时段");
        sheyingshiyuyueService.updateById(input);
        return R.ok();
    }


    @GetMapping("/workflow/{id}")
    public R workflow(@PathVariable Long id,HttpServletRequest r){
        SheyingshiyuyueEntity row=sheyingshiyuyueService.selectById(id);
        if(!allowed(row,r))return R.error(403,"无权查看订单");
        List<Map<String,Object>> rows=jdbc.queryForList("SELECT * FROM booking_fulfillment WHERE booking_id=?",id);
        Map<String,Object> data=rows.isEmpty()?new HashMap<>():rows.get(0);
        if(!admin(r))data.remove("settlement_note");
        return R.ok().put("data",data);
    }
    @PostMapping("/workflow/{id}/{action}")
    @Transactional
    public R advance(@PathVariable Long id,@PathVariable String action,@RequestBody Map<String,String> input,HttpServletRequest r){
        jdbc.queryForList("SELECT id FROM sheyingshiyuyue WHERE id=? FOR UPDATE",id);
        SheyingshiyuyueEntity row=sheyingshiyuyueService.selectById(id);
        if(!allowed(row,r)||"yonghu".equals(role(r)))return R.error(403,"仅工作室或对应摄影师可操作");
        if(refundBlocks(id))return R.error(409,"该订单正在退款或已退款，不能继续履约");
        jdbc.update("INSERT IGNORE INTO booking_fulfillment(booking_id) VALUES (?)",id);
        Map<String,Object> flow=jdbc.queryForMap("SELECT * FROM booking_fulfillment WHERE booking_id=?",id);
        String stage=String.valueOf(flow.get("stage"));
        if("confirm".equals(action)){
            if(!"pending".equals(stage)||"已支付".equals(row.getIspay()))return R.error(400,"订单不可重复确认");
            String name=input.get("packageName"),content=input.get("serviceContent");
            if(name==null||name.trim().isEmpty()||name.length()>200||content==null||content.trim().isEmpty()||content.length()>2000)return R.error(400,"请填写套餐及服务内容");
            if(conflict(row))return R.error(409,"档期冲突，请调整预约");
            jdbc.update("UPDATE booking_fulfillment SET package_name=?,service_content=?,stage='confirmed' WHERE booking_id=?",name.trim(),content.trim(),id);
            jdbc.update("UPDATE sheyingshiyuyue SET sfsh='是' WHERE id=?",id);
        }else if("shoot".equals(action)){
            if(!"confirmed".equals(stage)||!"已支付".equals(row.getIspay()))return R.error(400,"请先确认订单并完成付款");
            if(row.getYuyuesheyingriqi()!=null && dayKey(row.getYuyuesheyingriqi()).compareTo(LocalDate.now(ZONE).toString())>0)return R.error(400,"未到拍摄日期");
            jdbc.update("UPDATE booking_fulfillment SET stage='shot' WHERE booking_id=?",id);
        }else if("deliver".equals(action)){
            if(!admin(r)||!"shot".equals(stage))return R.error(400,"仅工作室可在拍摄完成后交付");
            String url=input.get("deliveryUrl");
            if(url==null||url.length()>1000||!url.matches("https?://[^\\s]+"))return R.error(400,"请填写有效的照片交付链接");
            jdbc.update("UPDATE booking_fulfillment SET stage='delivered',delivery_url=? WHERE booking_id=?",url,id);
        }else if("settle".equals(action)){
            if(!admin(r)||!("delivered".equals(stage)||"completed".equals(stage)))return R.error(400,"仅工作室可在交付后登记结算");
            String note=input.get("settlementNote");if(note==null||note.trim().isEmpty()||note.length()>500)return R.error(400,"请填写内部结算说明");
            jdbc.update("UPDATE booking_fulfillment SET stage='settled',settlement_note=? WHERE booking_id=?",note.trim(),id);
        }else return R.error(400,"未知操作");
        return R.ok();
    }
    @GetMapping("/photographerHistory")
    public R photographerHistory(HttpServletRequest r){
        if(!"yonghu".equals(role(r)))return R.error(403,"仅用户可查看自己的历史");
        return R.ok().put("data",jdbc.queryForList("SELECT b.sheyingzhanghao,b.sheyingxingming,COUNT(*) AS bookings,MAX(b.yuyuesheyingriqi) AS latestDate,(SELECT MIN(p.id) FROM sheyingshixinxi p WHERE p.sheyingzhanghao=b.sheyingzhanghao AND p.sfsh='是') AS profileId FROM sheyingshiyuyue b WHERE b.yonghuzhanghao=? AND (b.sfsh IS NULL OR b.sfsh<>'否') GROUP BY b.sheyingzhanghao,b.sheyingxingming ORDER BY latestDate DESC",username(r)));
    }
    /** Records order payment state after service confirmation. */
    @PostMapping({"/pay/{id}", "/simulatePay/{id}"})
    @Transactional
    public R pay(@PathVariable Long id, @RequestBody(required=false) Map<String,String> payment, HttpServletRequest r) {
        String channel=payment==null?"微信支付":payment.get("channel");
        if(!Arrays.asList("微信支付","支付宝","银行卡","云闪付").contains(channel))return R.error(400,"请选择支持的支付方式");
        List<Map<String,Object>> rows=jdbc.queryForList("SELECT yonghuzhanghao,sfsh FROM sheyingshiyuyue WHERE id=? FOR UPDATE", id);
        if(rows.isEmpty() || !(admin(r) || ("yonghu".equals(role(r)) && username(r).equals(rows.get(0).get("yonghuzhanghao"))))) return R.error(403,"无权支付此预约");
        if(refundBlocks(id))return R.error(409,"该订单正在退款或已退款，不能付款");
        if(!"是".equals(rows.get(0).get("sfsh"))) return R.error(400,"请等待预约审核通过后再支付");
        Integer confirmed=jdbc.queryForObject("SELECT COUNT(*) FROM booking_fulfillment WHERE booking_id=? AND stage='confirmed' AND package_name IS NOT NULL AND service_content IS NOT NULL",Integer.class,id);
        if(confirmed==null||confirmed==0)return R.error(400,"请工作室先确认套餐和服务内容，再付款");
        jdbc.update("UPDATE sheyingshiyuyue SET ispay='已支付' WHERE id=?", id);
        return R.ok("支付完成");
    }

    @PostMapping("/change-request")
    @Transactional
    public R changeRequest(@RequestBody Map<String,String> input, HttpServletRequest r) {
        if (!"yonghu".equals(role(r))) return R.error(403,"仅用户可以提交改期或取消申请");
        Long bookingId; try { bookingId=Long.valueOf(input.get("bookingId")); } catch(Exception e) { return R.error(400,"预约编号无效"); }
        SheyingshiyuyueEntity row=sheyingshiyuyueService.selectById(bookingId);
        if(!allowed(row,r)) return R.error(403,"无权操作此预约");
        jdbc.queryForList("SELECT id FROM sheyingshiyuyue WHERE id=? FOR UPDATE",bookingId);
        if(refundBlocks(bookingId))return R.error(409,"该订单正在退款或已退款，不能申请变更");
        if("已支付".equals(row.getIspay()) && "cancel".equals(input.get("type"))) return R.error(400,"已付款订单请先联系工作室确认退款");
        if("是".equals(row.getSfsh()) && "cancel".equals(input.get("type"))) return R.error(400,"已确认预约请通过工作室申请取消");
        String type="reschedule".equals(input.get("type"))?"reschedule":"cancel";
        Integer pending=jdbc.queryForObject("SELECT COUNT(*) FROM booking_change_request WHERE booking_id=? AND status='pending'",Integer.class,bookingId);
        if(pending!=null&&pending>0)return R.error(400,"已有申请正在处理中");
        String date=input.get("date"),slot=input.get("slot"),reason=input.get("reason");
        if("reschedule".equals(type)){try{LocalDate.parse(date); }catch(Exception e){return R.error(400,"请选择有效日期");} if(!SLOTS.contains(slot))return R.error(400,"请选择有效时间段"); Integer conflict=jdbc.queryForObject("SELECT COUNT(*) FROM sheyingshiyuyue WHERE sheyingzhanghao=? AND yuyuesheyingriqi=? AND yuyueshijianduan=? AND id<>? AND (sfsh IS NULL OR sfsh<>'否')",Integer.class,row.getSheyingzhanghao(),java.sql.Date.valueOf(date),slot,bookingId);if(conflict!=null&&conflict>0)return R.error(409,"该时段已被预约");}
        if(reason!=null&&reason.length()>500)return R.error(400,"说明不能超过500字");
        jdbc.update("INSERT INTO booking_change_request(booking_id,request_type,requested_date,requested_slot,reason) VALUES (?,?,?,?,?)",bookingId,type,"reschedule".equals(type)?java.sql.Date.valueOf(date):null,"reschedule".equals(type)?slot:null,reason);
        return R.ok("申请已提交，请等待工作室处理");
    }
    @GetMapping("/change-requests")
    public R changeRequests(@RequestParam(required=false) String status,HttpServletRequest r){
        if(!admin(r)&&!"sheyingshi".equals(role(r)))return R.error(403,"无权查看申请");
        String sql="SELECT c.*,b.sheyingxingming,b.yonghuxingming,b.yuyuesheyingriqi,b.yuyueshijianduan FROM booking_change_request c JOIN sheyingshiyuyue b ON b.id=c.booking_id WHERE 1=1";
        List<Object> args=new ArrayList<>(); if("sheyingshi".equals(role(r))){sql+=" AND b.sheyingzhanghao=?";args.add(username(r));} if(status!=null&&!status.trim().isEmpty()){sql+=" AND c.status=?";args.add(status);} sql+=" ORDER BY c.id DESC";
        return R.ok().put("data",jdbc.queryForList(sql,args.toArray()));
    }
    @PostMapping("/change-requests/{id}/handle")
    @Transactional
    public R handleChange(@PathVariable Long id,@RequestBody Map<String,String> input,HttpServletRequest r){
        if(!admin(r)&&!"sheyingshi".equals(role(r)))return R.error(403,"仅工作室或摄影师可以处理申请");
        List<Map<String,Object>> req=jdbc.queryForList("SELECT c.*,b.sheyingzhanghao FROM booking_change_request c JOIN sheyingshiyuyue b ON b.id=c.booking_id WHERE c.id=?",id);if(req.isEmpty())return R.error(404,"申请不存在"); Map<String,Object> item=req.get(0);if("sheyingshi".equals(role(r))&&!username(r).equals(String.valueOf(item.get("sheyingzhanghao"))))return R.error(403,"无权处理此申请");
        Long changeBookingId=Long.valueOf(String.valueOf(item.get("booking_id")));
        jdbc.queryForList("SELECT id FROM sheyingshiyuyue WHERE id=? FOR UPDATE",changeBookingId);
        if(refundBlocks(changeBookingId))return R.error(409,"该订单正在退款或已退款，不能处理变更");
        String action=input.get("action");if(!Arrays.asList("approve","reject").contains(action))return R.error(400,"处理方式无效");if(!"pending".equals(String.valueOf(item.get("status"))))return R.error(400,"该申请已处理");
        if("approve".equals(action)){Long booking=Long.valueOf(String.valueOf(item.get("booking_id")));if("reschedule".equals(item.get("request_type"))){jdbc.update("UPDATE sheyingshiyuyue SET yuyuesheyingriqi=?,yuyueshijianduan=?,sfsh='是' WHERE id=?",item.get("requested_date"),item.get("requested_slot"),booking);}else{jdbc.update("UPDATE sheyingshiyuyue SET sfsh='否',shhf='预约已按用户申请取消' WHERE id=?",booking);}}
        jdbc.update("UPDATE booking_change_request SET status=?,admin_note=?,handled_at=NOW() WHERE id=?", "approve".equals(action)?"approved":"rejected",input.get("note"),id);
        Long bookingId=Long.valueOf(String.valueOf(item.get("booking_id")));
        Map<String,Object> booking=jdbc.queryForMap("SELECT yonghuzhanghao FROM sheyingshiyuyue WHERE id=?",bookingId);
        String result="approve".equals(action)?"批准":"驳回";
        String kind="reschedule".equals(item.get("request_type"))?"改期":"取消";
        String note=input.get("note");
        jdbc.update("INSERT INTO system_message(receiver_type,receiver_account,title,content,booking_id,message_type) VALUES (?,?,?,?,?,?)", "yonghu", String.valueOf(booking.get("yonghuzhanghao")), "预约变更申请结果", "您的"+kind+"申请已"+result+"。"+(note==null?"":note), bookingId, "booking-change");
        return R.ok("申请处理完成");
    }
    @GetMapping("/orders")
    public R orders(@RequestParam(defaultValue="all") String status,@RequestParam(defaultValue="1") int page,HttpServletRequest r){
        if(!"yonghu".equals(role(r)))return R.error(403,"请使用用户账号查看订单");
        String state="CASE WHEN b.sfsh='否' THEN 'cancelled' WHEN f.stage IN ('completed','settled') THEN 'completed' WHEN f.stage='delivered' THEN 'delivered' WHEN f.stage='shot' THEN 'delivery' WHEN b.ispay='已支付' THEN 'shoot' WHEN f.stage='confirmed' AND b.sfsh='是' THEN 'payment' ELSE 'pending' END";
        String sql=" FROM sheyingshiyuyue b LEFT JOIN booking_fulfillment f ON f.booking_id=b.id WHERE b.yonghuzhanghao=?";
        List<Object> args=new ArrayList<>();args.add(username(r));
        if(!"all".equals(status)){if(!Arrays.asList("pending","payment","shoot","delivery","delivered","completed","cancelled").contains(status))return R.error(400,"订单状态无效");sql+=" AND ("+state+")=?";args.add(status);}
        Long total=jdbc.queryForObject("SELECT COUNT(*)"+sql,Long.class,args.toArray());
        args.add((Math.max(1,page)-1)*10);
        return R.ok().put("total",total).put("data",jdbc.queryForList("SELECT b.*,f.package_name,f.service_content,f.delivery_url,(SELECT status FROM booking_refund refund WHERE refund.booking_id=b.id) AS refund_status,(SELECT COUNT(*) FROM discusssheyingshixinxi d WHERE d.booking_id=b.id) AS reviewed,"+state+" AS order_status"+sql+" ORDER BY b.id DESC LIMIT 10 OFFSET ?",args.toArray()));
    }
    @PostMapping("/orders/{id}/review")
    @Transactional
    public R reviewOrder(@PathVariable Long id,@RequestBody Map<String,Object> input,HttpServletRequest r){
        jdbc.queryForList("SELECT id FROM sheyingshiyuyue WHERE id=? FOR UPDATE",id);
        SheyingshiyuyueEntity row=sheyingshiyuyueService.selectById(id);
        if(!"yonghu".equals(role(r))||!allowed(row,r))return R.error(403,"仅订单本人可以评价");
        jdbc.queryForList("SELECT id FROM sheyingshixinxi WHERE sheyingzhanghao=? ORDER BY id FOR UPDATE",row.getSheyingzhanghao());
        Integer complete=jdbc.queryForObject("SELECT COUNT(*) FROM booking_fulfillment WHERE booking_id=? AND stage IN ('completed','settled')",Integer.class,id);
        if(complete==0||"否".equals(row.getSfsh()))return R.error(400,"请完成订单后再评价");
        if(jdbc.queryForObject("SELECT COUNT(*) FROM discusssheyingshixinxi WHERE booking_id=?",Integer.class,id)>0)return R.error(409,"该订单已评价");
        int score;try{double v=Double.parseDouble(String.valueOf(input.get("score")));score=(int)v;if(v!=score||score<1||score>5)return R.error(400,"请选择1至5星评分");}catch(Exception e){return R.error(400,"评分无效");}
        String content=Objects.toString(input.get("content"),"").trim();
        if(content.isEmpty()||content.length()>2000)return R.error(400,"请填写2000字以内评价");
        String images=Objects.toString(input.get("images"),"");
        if(images.length()>2000)return R.error(400,"图片数量过多");
        if(!images.isEmpty()){String[] paths=images.split(",");if(paths.length>6)return R.error(400,"最多上传6张评价图片");for(String path:paths)if(!path.matches("file/[a-zA-Z0-9_-]+[.](png|jpg|jpeg|gif|webp)"))return R.error(400,"评价图片地址无效");}
        List<Long> profiles=jdbc.queryForList("SELECT id FROM sheyingshixinxi WHERE sheyingzhanghao=? ORDER BY id",Long.class,row.getSheyingzhanghao());
        if(profiles.isEmpty())return R.error(400,"摄影师资料不存在，请联系工作室");
        Object uid=r.getSession().getAttribute("userId");
        List<Map<String,Object>> users=jdbc.queryForList("SELECT id,yonghuxingming,touxiang FROM yonghu WHERE yonghuzhanghao=?",username(r));
        if(users.isEmpty())return R.error(403,"用户资料不存在");
        Map<String,Object> u=users.get(0);
        String escaped=content.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;").replace("\n","<br>");
        jdbc.update("INSERT INTO discusssheyingshixinxi(refid,userid,avatarurl,nickname,score,content,booking_id,review_images) VALUES (?,?,?,?,?,?,?,?)",profiles.get(0),u.get("id"),u.get("touxiang"),u.get("yonghuxingming"),score,escaped,id,images);
        jdbc.update("UPDATE sheyingshixinxi SET discuss_number=COALESCE(discuss_number,0)+1 WHERE id=?",profiles.get(0));
        return R.ok("评价已发布");
    }
    @PostMapping("/orders/{id}/complete")
    @Transactional
    public R completeOrder(@PathVariable Long id,HttpServletRequest r){
        jdbc.queryForList("SELECT id FROM sheyingshiyuyue WHERE id=? FOR UPDATE",id);
        SheyingshiyuyueEntity row=sheyingshiyuyueService.selectById(id);
        if(!"yonghu".equals(role(r))||!allowed(row,r))return R.error(403,"无权确认此订单");
        int changed=jdbc.update("UPDATE booking_fulfillment SET stage='completed' WHERE booking_id=? AND stage='delivered'",id);
        if(changed==0)return R.error(400,"仅已交付订单可以确认完成");
        return R.ok("已确认完成");
    }
    @GetMapping("/messages")
    public R messages(@RequestParam(defaultValue="1") int page,@RequestParam(defaultValue="20") int limit,HttpServletRequest r){
        String type=role(r),account=username(r); int size=Math.min(50,Math.max(1,limit)),offset=Math.max(0,page-1)*size;
        List<Map<String,Object>> rows=jdbc.queryForList("SELECT m.id,m.title,m.content,m.booking_id,m.message_type,m.is_read,DATE_FORMAT(m.created_at,'%Y-%m-%d %H:%i:%s') AS created_at FROM system_message m WHERE receiver_type=? AND receiver_account=? ORDER BY id DESC LIMIT ? OFFSET ?",type,account,size,offset);
        Integer unread=jdbc.queryForObject("SELECT COUNT(*) FROM system_message WHERE receiver_type=? AND receiver_account=? AND is_read=0",Integer.class,type,account);
        return R.ok().put("data",rows).put("total",jdbc.queryForObject("SELECT COUNT(*) FROM system_message WHERE receiver_type=? AND receiver_account=?",Integer.class,type,account)).put("unread",unread==null?0:unread);
    }
    @GetMapping("/messages/unread")
    public R unreadMessages(HttpServletRequest r){
        Integer count=jdbc.queryForObject("SELECT COUNT(*) FROM system_message WHERE receiver_type=? AND receiver_account=? AND is_read=0",Integer.class,role(r),username(r));
        return R.ok().put("data",count==null?0:count);
    }
    @PostMapping("/messages/read-all")
    public R readAllMessages(HttpServletRequest r){jdbc.update("UPDATE system_message SET is_read=1 WHERE receiver_type=? AND receiver_account=?",role(r),username(r));return R.ok();}
    @PostMapping("/messages/read/{id}")
    public R readMessage(@PathVariable Long id,HttpServletRequest r){jdbc.update("UPDATE system_message SET is_read=1 WHERE id=? AND receiver_type=? AND receiver_account=?",id,role(r),username(r));return R.ok();}

    @RequestMapping("/delete")
    @Transactional
    public R delete(@RequestBody Long[] ids, HttpServletRequest r) {
        if(!admin(r)) return R.error(403,"仅管理员可以删除预约，请联系工作室取消或调整");
        for(Long id:ids){jdbc.queryForList("SELECT id FROM sheyingshiyuyue WHERE id=? FOR UPDATE",id);if(jdbc.queryForObject("SELECT COUNT(*) FROM booking_refund WHERE booking_id=?",Integer.class,id)>0)return R.error(400,"含退款记录的订单需保留，不支持删除");}
        sheyingshiyuyueService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
}

