package com.cl.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cl.annotation.IgnoreAuth;
import com.cl.entity.SheyingshixinxiEntity;
import com.cl.entity.view.SheyingshixinxiView;
import com.cl.service.SheyingshixinxiService;
import com.cl.utils.MPUtil;
import com.cl.utils.PageUtils;
import com.cl.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 摄影师信息
 * 后端接口
 *
 * @author
 * @email
 * @date 2025-05-22 16:26:52
 */
@RestController
@RequestMapping("/sheyingshixinxi")
public class SheyingshixinxiController {
    @Autowired
    private SheyingshixinxiService sheyingshixinxiService;


    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, SheyingshixinxiEntity sheyingshixinxi,
                  HttpServletRequest request) {
        String tableName = request.getSession().getAttribute("tableName").toString();
        if (tableName.equals("sheyingshi")) {
            sheyingshixinxi.setSheyingzhanghao((String) request.getSession().getAttribute("username"));
        }
        EntityWrapper<SheyingshixinxiEntity> ew = new EntityWrapper<SheyingshixinxiEntity>();


        PageUtils page = sheyingshixinxiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, sheyingshixinxi), params), params));
        return R.ok().put("data", page);
    }


    /**
     * 前端列表
     */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, SheyingshixinxiEntity sheyingshixinxi,
                  HttpServletRequest request) {
        EntityWrapper<SheyingshixinxiEntity> ew = new EntityWrapper<SheyingshixinxiEntity>();

        PageUtils page = sheyingshixinxiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, sheyingshixinxi), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/lists")
    public R list(SheyingshixinxiEntity sheyingshixinxi) {
        EntityWrapper<SheyingshixinxiEntity> ew = new EntityWrapper<SheyingshixinxiEntity>();
        ew.allEq(MPUtil.allEQMapPre(sheyingshixinxi, MPUtil.camelToSnake("sheyingshixinxi")));
        return R.ok().put("data", sheyingshixinxiService.selectListView(ew));
    }

    /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(SheyingshixinxiEntity sheyingshixinxi) {
        EntityWrapper<SheyingshixinxiEntity> ew = new EntityWrapper<SheyingshixinxiEntity>();
        ew.allEq(MPUtil.allEQMapPre(sheyingshixinxi, MPUtil.camelToSnake("sheyingshixinxi")));
        SheyingshixinxiView sheyingshixinxiView = sheyingshixinxiService.selectView(ew);
        return R.ok("查询摄影师信息成功").put("data", sheyingshixinxiView);
    }

    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        SheyingshixinxiEntity sheyingshixinxi = sheyingshixinxiService.selectById(id);
        sheyingshixinxi = sheyingshixinxiService.selectView(new EntityWrapper<SheyingshixinxiEntity>().eq("id", id));
        return R.ok().put("data", sheyingshixinxi);
    }

    /**
     * 前端详情
     */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id) {
        SheyingshixinxiEntity sheyingshixinxi = sheyingshixinxiService.selectById(id);
        sheyingshixinxi = sheyingshixinxiService.selectView(new EntityWrapper<SheyingshixinxiEntity>().eq("id", id));
        return R.ok().put("data", sheyingshixinxi);
    }


    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody SheyingshixinxiEntity sheyingshixinxi, HttpServletRequest request) {
        //ValidatorUtils.validateEntity(sheyingshixinxi);
        sheyingshixinxiService.insert(sheyingshixinxi);
        return R.ok();
    }

    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody SheyingshixinxiEntity sheyingshixinxi, HttpServletRequest request) {
        //ValidatorUtils.validateEntity(sheyingshixinxi);
        sheyingshixinxiService.insert(sheyingshixinxi);
        return R.ok();
    }


    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody SheyingshixinxiEntity sheyingshixinxi, HttpServletRequest request) {
        sheyingshixinxiService.updateById(sheyingshixinxi);//全部更新
        return R.ok();
    }

    /**
     * 审核
     */
    @RequestMapping("/shBatch")
    @Transactional
    public R update(@RequestBody Long[] ids, @RequestParam String sfsh, @RequestParam String shhf) {
        List<SheyingshixinxiEntity> list = new ArrayList<SheyingshixinxiEntity>();
        for (Long id : ids) {
            SheyingshixinxiEntity sheyingshixinxi = sheyingshixinxiService.selectById(id);
            sheyingshixinxi.setSfsh(sfsh);
            sheyingshixinxi.setShhf(shhf);
            list.add(sheyingshixinxi);
        }
        sheyingshixinxiService.updateBatchById(list);
        return R.ok();
    }


    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        sheyingshixinxiService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }


}
