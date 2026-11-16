package com.cl.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cl.annotation.IgnoreAuth;
import com.cl.entity.SheyingzuopinEntity;
import com.cl.entity.view.SheyingzuopinView;
import com.cl.service.SheyingzuopinService;
import com.cl.utils.MPUtil;
import com.cl.utils.PageUtils;
import com.cl.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;

/**
 * 摄影作品
 * 后端接口
 *
 * @author
 * @email
 * @date 2025-05-22 16:26:52
 */
@RestController
@RequestMapping("/sheyingzuopin")
public class SheyingzuopinController {
    @Autowired
    private SheyingzuopinService sheyingzuopinService;


    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, SheyingzuopinEntity sheyingzuopin,
                  HttpServletRequest request) {
        String tableName = request.getSession().getAttribute("tableName").toString();
        if (tableName.equals("sheyingshi")) {
            sheyingzuopin.setSheyingzhanghao((String) request.getSession().getAttribute("username"));
        }
        EntityWrapper<SheyingzuopinEntity> ew = new EntityWrapper<SheyingzuopinEntity>();


        PageUtils page = sheyingzuopinService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, sheyingzuopin), params), params));
        return R.ok().put("data", page);
    }


    /**
     * 前端列表
     */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, SheyingzuopinEntity sheyingzuopin,
                  HttpServletRequest request) {
        EntityWrapper<SheyingzuopinEntity> ew = new EntityWrapper<SheyingzuopinEntity>();

        PageUtils page = sheyingzuopinService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, sheyingzuopin), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/lists")
    public R list(SheyingzuopinEntity sheyingzuopin) {
        EntityWrapper<SheyingzuopinEntity> ew = new EntityWrapper<SheyingzuopinEntity>();
        ew.allEq(MPUtil.allEQMapPre(sheyingzuopin, MPUtil.camelToSnake("sheyingzuopin")));
        return R.ok().put("data", sheyingzuopinService.selectListView(ew));
    }

    /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(SheyingzuopinEntity sheyingzuopin) {
        EntityWrapper<SheyingzuopinEntity> ew = new EntityWrapper<SheyingzuopinEntity>();
        ew.allEq(MPUtil.allEQMapPre(sheyingzuopin, MPUtil.camelToSnake("sheyingzuopin")));
        SheyingzuopinView sheyingzuopinView = sheyingzuopinService.selectView(ew);
        return R.ok("查询摄影作品成功").put("data", sheyingzuopinView);
    }

    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        SheyingzuopinEntity sheyingzuopin = sheyingzuopinService.selectById(id);
        sheyingzuopin = sheyingzuopinService.selectView(new EntityWrapper<SheyingzuopinEntity>().eq("id", id));
        return R.ok().put("data", sheyingzuopin);
    }

    /**
     * 前端详情
     */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id) {
        SheyingzuopinEntity sheyingzuopin = sheyingzuopinService.selectById(id);
        sheyingzuopin = sheyingzuopinService.selectView(new EntityWrapper<SheyingzuopinEntity>().eq("id", id));
        return R.ok().put("data", sheyingzuopin);
    }


    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody SheyingzuopinEntity sheyingzuopin, HttpServletRequest request) {
        //ValidatorUtils.validateEntity(sheyingzuopin);
        sheyingzuopinService.insert(sheyingzuopin);
        return R.ok();
    }

    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody SheyingzuopinEntity sheyingzuopin, HttpServletRequest request) {
        //ValidatorUtils.validateEntity(sheyingzuopin);
        sheyingzuopinService.insert(sheyingzuopin);
        return R.ok();
    }


    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody SheyingzuopinEntity sheyingzuopin, HttpServletRequest request) {
        //ValidatorUtils.validateEntity(sheyingzuopin);
        sheyingzuopinService.updateById(sheyingzuopin);//全部更新
        return R.ok();
    }


    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        sheyingzuopinService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }


}
