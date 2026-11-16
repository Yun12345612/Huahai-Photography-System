package com.cl.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cl.annotation.IgnoreAuth;
import com.cl.entity.BaomingxinxiEntity;
import com.cl.entity.view.BaomingxinxiView;
import com.cl.service.BaomingxinxiService;
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
 * 报名信息
 * 后端接口
 *
 * @author
 * @email
 * @date 2025-05-22 16:26:52
 */
@RestController
@RequestMapping("/baomingxinxi")
public class BaomingxinxiController {
    @Autowired
    private BaomingxinxiService baomingxinxiService;


    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, BaomingxinxiEntity baomingxinxi,
                  HttpServletRequest request) {
        String tableName = request.getSession().getAttribute("tableName").toString();
        if (tableName.equals("yonghu")) {
            baomingxinxi.setYonghuzhanghao((String) request.getSession().getAttribute("username"));
        }
        EntityWrapper<BaomingxinxiEntity> ew = new EntityWrapper<BaomingxinxiEntity>();
        //使用 MPUtil.likeOrEq 根据baomingxinxi对象生成模糊查询或等于查询条件。
        //使用 MPUtil.between 在生成的条件基础上添加范围查询条件。
        //使用 MPUtil.sort 在生成的条件基础上添加排序条件。
        //生成的条件huodongmingcheng LIKE '%test%' AND yonghuzhanghao = 'user123' AND kaishishijian >= '2023-01-01' AND kaishishijian <= '2023-12-31' ORDER BY addtime DESC
        PageUtils page = baomingxinxiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, baomingxinxi), params), params));
        return R.ok().put("data", page);
    }


    /**
     * 前端列表
     */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, BaomingxinxiEntity baomingxinxi,
                  HttpServletRequest request) {
        EntityWrapper<BaomingxinxiEntity> ew = new EntityWrapper<BaomingxinxiEntity>();

        PageUtils page = baomingxinxiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, baomingxinxi), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/lists")
    public R list(BaomingxinxiEntity baomingxinxi) {
        EntityWrapper<BaomingxinxiEntity> ew = new EntityWrapper<BaomingxinxiEntity>();
        ew.allEq(MPUtil.allEQMapPre(baomingxinxi, MPUtil.camelToSnake("baomingxinxi")));
        return R.ok().put("data", baomingxinxiService.selectListView(ew));
    }

    /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(BaomingxinxiEntity baomingxinxi) {
        EntityWrapper<BaomingxinxiEntity> ew = new EntityWrapper<BaomingxinxiEntity>();
        ew.allEq(MPUtil.allEQMapPre(baomingxinxi, MPUtil.camelToSnake("baomingxinxi")));
        BaomingxinxiView baomingxinxiView = baomingxinxiService.selectView(ew);
        return R.ok("查询报名信息成功").put("data", baomingxinxiView);
    }

    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        BaomingxinxiEntity baomingxinxi = baomingxinxiService.selectById(id);
        baomingxinxi = baomingxinxiService.selectView(new EntityWrapper<BaomingxinxiEntity>().eq("id", id));
        return R.ok().put("data", baomingxinxi);
    }

    /**
     * 前端详情
     */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id) {
        BaomingxinxiEntity baomingxinxi = baomingxinxiService.selectById(id);
        baomingxinxi = baomingxinxiService.selectView(new EntityWrapper<BaomingxinxiEntity>().eq("id", id));
        return R.ok().put("data", baomingxinxi);
    }


    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody BaomingxinxiEntity baomingxinxi, HttpServletRequest request) {
        //ValidatorUtils.validateEntity(baomingxinxi);
        baomingxinxiService.insert(baomingxinxi);
        return R.ok();
    }

    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody BaomingxinxiEntity baomingxinxi, HttpServletRequest request) {
        //ValidatorUtils.validateEntity(baomingxinxi);
        baomingxinxiService.insert(baomingxinxi);
        return R.ok();
    }


    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody BaomingxinxiEntity baomingxinxi, HttpServletRequest request) {
        //ValidatorUtils.validateEntity(baomingxinxi);
        baomingxinxiService.updateById(baomingxinxi);//全部更新
        return R.ok();
    }


    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        baomingxinxiService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }


}
