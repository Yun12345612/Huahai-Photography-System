package com.cl.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cl.annotation.IgnoreAuth;
import com.cl.entity.DiscusssheyingshixinxiEntity;
import com.cl.entity.view.DiscusssheyingshixinxiView;
import com.cl.service.DiscusssheyingshixinxiService;
import com.cl.utils.MPUtil;
import com.cl.utils.PageUtils;
import com.cl.utils.R;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 摄影师信息评论表
 * 后端接口
 *
 * @author
 * @email
 * @date 2025-05-22 16:26:52
 */
@RestController
@RequestMapping("/discusssheyingshixinxi")
public class DiscusssheyingshixinxiController {
    @Autowired
    private DiscusssheyingshixinxiService discusssheyingshixinxiService;
    @Autowired private org.springframework.jdbc.core.JdbcTemplate jdbc;


    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, DiscusssheyingshixinxiEntity discusssheyingshixinxi,
                  HttpServletRequest request) {
        EntityWrapper<DiscusssheyingshixinxiEntity> ew = new EntityWrapper<DiscusssheyingshixinxiEntity>();


        PageUtils page = discusssheyingshixinxiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, discusssheyingshixinxi), params), params));
        return R.ok().put("data", page);
    }


    /**
     * 前端列表
     */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, DiscusssheyingshixinxiEntity discusssheyingshixinxi,
                  HttpServletRequest request) {
        EntityWrapper<DiscusssheyingshixinxiEntity> ew = new EntityWrapper<DiscusssheyingshixinxiEntity>();

        PageUtils page = discusssheyingshixinxiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, discusssheyingshixinxi), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/lists")
    public R list(DiscusssheyingshixinxiEntity discusssheyingshixinxi) {
        EntityWrapper<DiscusssheyingshixinxiEntity> ew = new EntityWrapper<DiscusssheyingshixinxiEntity>();
        ew.allEq(MPUtil.allEQMapPre(discusssheyingshixinxi, MPUtil.camelToSnake("discusssheyingshixinxi")));
        return R.ok().put("data", discusssheyingshixinxiService.selectListView(ew));
    }

    /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(DiscusssheyingshixinxiEntity discusssheyingshixinxi) {
        EntityWrapper<DiscusssheyingshixinxiEntity> ew = new EntityWrapper<DiscusssheyingshixinxiEntity>();
        ew.allEq(MPUtil.allEQMapPre(discusssheyingshixinxi, MPUtil.camelToSnake("discusssheyingshixinxi")));
        DiscusssheyingshixinxiView discusssheyingshixinxiView = discusssheyingshixinxiService.selectView(ew);
        return R.ok("查询摄影师信息评论表成功").put("data", discusssheyingshixinxiView);
    }

    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        DiscusssheyingshixinxiEntity discusssheyingshixinxi = discusssheyingshixinxiService.selectById(id);
        discusssheyingshixinxi = discusssheyingshixinxiService.selectView(new EntityWrapper<DiscusssheyingshixinxiEntity>().eq("id", id));
        return R.ok().put("data", discusssheyingshixinxi);
    }

    /**
     * 前端详情
     */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id) {
        DiscusssheyingshixinxiEntity discusssheyingshixinxi = discusssheyingshixinxiService.selectById(id);
        discusssheyingshixinxi = discusssheyingshixinxiService.selectView(new EntityWrapper<DiscusssheyingshixinxiEntity>().eq("id", id));
        return R.ok().put("data", discusssheyingshixinxi);
    }


    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody DiscusssheyingshixinxiEntity discusssheyingshixinxi, HttpServletRequest request) {
        //ValidatorUtils.validateEntity(discusssheyingshixinxi);
        return R.error(400,"请从本人已完成订单提交评价");
    }

    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody DiscusssheyingshixinxiEntity discusssheyingshixinxi, HttpServletRequest request) {
        //ValidatorUtils.validateEntity(discusssheyingshixinxi);
        return R.error(400,"请从本人已完成订单提交评价");
    }

    /**
     * 获取用户密保
     */
    @RequestMapping("/security")
    @IgnoreAuth
    public R security(@RequestParam String username) {
        DiscusssheyingshixinxiEntity discusssheyingshixinxi = discusssheyingshixinxiService.selectOne(new EntityWrapper<DiscusssheyingshixinxiEntity>().eq("", username));
        return R.ok().put("data", discusssheyingshixinxi);
    }


    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody DiscusssheyingshixinxiEntity discusssheyingshixinxi, HttpServletRequest request) {
        //ValidatorUtils.validateEntity(discusssheyingshixinxi);
        DiscusssheyingshixinxiEntity stored=discusssheyingshixinxiService.selectById(discusssheyingshixinxi.getId());
        if(stored==null)return R.error(404,"评价不存在");
        String role=String.valueOf(request.getSession().getAttribute("tableName"));
        String account=String.valueOf(request.getSession().getAttribute("username"));
        boolean allowed="users".equals(role)||("sheyingshi".equals(role)&&jdbc.queryForObject("SELECT COUNT(*) FROM sheyingshixinxi WHERE id=? AND sheyingzhanghao=?",Integer.class,stored.getRefid(),account)>0);
        if(!allowed)return R.error(403,"仅工作室或对应摄影师可以回复评价");
        String reply=discusssheyingshixinxi.getReply();
        if(reply==null||reply.trim().isEmpty()||reply.length()>2000)return R.error(400,"请填写2000字以内回复");
        jdbc.update("UPDATE discusssheyingshixinxi SET reply=? WHERE id=?",reply.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;"),stored.getId());
        return R.ok();
    }


    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids, HttpServletRequest request) {
        String role=String.valueOf(request.getSession().getAttribute("tableName"));
        for(Long id:ids){
            DiscusssheyingshixinxiEntity row=discusssheyingshixinxiService.selectById(id);if(row==null)continue;
            if(row.getBookingId()!=null)return R.error(400,"订单评价需保留，不支持删除后重新评价");
            if("users".equals(role))continue;
            if("sheyingshi".equals(role)){
                Integer count=jdbc.queryForObject("SELECT COUNT(*) FROM sheyingshixinxi WHERE id=? AND sheyingzhanghao=?",Integer.class,row.getRefid(),request.getSession().getAttribute("username"));
                if(count!=null&&count>0)continue;
            }
            if("yonghu".equals(role)&&java.util.Objects.equals(row.getUserid(),request.getSession().getAttribute("userId")))continue;
            return R.error(403,"无权删除评价");
        }
        discusssheyingshixinxiService.deleteBatchIds(Arrays.asList(ids));return R.ok();
    }


    /**
     * 前端智能排序
     */
    @IgnoreAuth
    @RequestMapping("/autoSort")
    public R autoSort(@RequestParam Map<String, Object> params, DiscusssheyingshixinxiEntity discusssheyingshixinxi, HttpServletRequest request, String pre) {
        EntityWrapper<DiscusssheyingshixinxiEntity> ew = new EntityWrapper<DiscusssheyingshixinxiEntity>();
        Map<String, Object> newMap = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
        Iterator<Map.Entry<String, Object>> it = param.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            String key = entry.getKey();
            String newKey = entry.getKey();
            if (pre.endsWith(".")) {
                newMap.put(pre + newKey, entry.getValue());
            } else if (StringUtils.isEmpty(pre)) {
                newMap.put(newKey, entry.getValue());
            } else {
                newMap.put(pre + "." + newKey, entry.getValue());
            }
        }
        params.put("sort", "clicktime");
        params.put("order", "desc");
        PageUtils page = discusssheyingshixinxiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, discusssheyingshixinxi), params), params));
        return R.ok().put("data", page);
    }


}
