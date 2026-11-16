package com.cl.controller;
import com.cl.utils.R;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
@RestController @RequestMapping("/operation-logs")
public class OperationLogController {
 private final JdbcTemplate jdbc; public OperationLogController(JdbcTemplate jdbc){this.jdbc=jdbc;}
 @GetMapping public R list(@RequestParam(defaultValue="1") int page,@RequestParam(defaultValue="20") int limit,@RequestParam(defaultValue="") String action,HttpServletRequest r){
 if(!"users".equals(r.getSession().getAttribute("tableName")))return R.error(403,"仅管理员可查看操作日志");
 page=Math.max(1,page);limit=Math.min(100,Math.max(1,limit));String where=" WHERE action LIKE ?";
 Long total=jdbc.queryForObject("SELECT COUNT(*) FROM operation_log"+where,Long.class,"%"+action.trim()+"%");
 return R.ok().put("total",total).put("data",jdbc.queryForList("SELECT l.id,l.operator_type,l.operator_account,l.action,l.target_type,l.target_id,l.detail,DATE_FORMAT(l.created_at,'%Y-%m-%d %H:%i:%s') AS created_at FROM operation_log l"+where+" ORDER BY id DESC LIMIT ? OFFSET ?","%"+action.trim()+"%",limit,(page-1)*limit));
 }
}
