package com.cl.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cl.annotation.IgnoreAuth;
import com.cl.entity.SheyingshiEntity;
import com.cl.entity.TokenEntity;
import com.cl.entity.view.SheyingshiView;
import com.cl.service.SheyingshiService;
import com.cl.service.TokenService;
import com.cl.utils.MPUtil;
import com.cl.utils.PageUtils;
import com.cl.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 摄影师
 * 后端接口
 *
 * @author
 * @email
 * @date 2025-05-22 16:26:52
 */
@RestController
@RequestMapping("/sheyingshi")
public class SheyingshiController {
    @Autowired
    private SheyingshiService sheyingshiService;


    @Autowired
    private TokenService tokenService;

    /**
     * 登录
     */
    @IgnoreAuth
    @RequestMapping(value = "/login")
    public R login(String username, String password, String captcha, HttpServletRequest request) {
        SheyingshiEntity u = sheyingshiService.selectOne(new EntityWrapper<SheyingshiEntity>().eq("sheyingzhanghao", username));
        if (u == null || !u.getMima().equals(password)) {
            return R.error("账号或密码不正确");
        }
        if (!"是".equals(u.getSfsh())) {
            if ("否".equals(u.getSfsh())) return R.error("摄影师申请未通过，请联系工作室处理。");
            return R.error("摄影师资料正在审核，请等待管理员审核通过。");
        }
        String token = tokenService.generateToken(u.getId(), username, "sheyingshi", "摄影师");
        return R.ok().put("token", token);
    }


    /**
     * 注册
     */
    @IgnoreAuth
    @RequestMapping("/register")
    public R register(@RequestBody SheyingshiEntity sheyingshi) {
        //ValidatorUtils.validateEntity(sheyingshi);
        SheyingshiEntity u = sheyingshiService.selectOne(new EntityWrapper<SheyingshiEntity>().eq("sheyingzhanghao", sheyingshi.getSheyingzhanghao()));
        if (u != null) {
            return R.error("注册用户已存在");
        }
        Long uId = new Date().getTime();
        sheyingshi.setId(uId);
        sheyingshi.setSfsh("待审核");
        sheyingshi.setShhf(null);
        sheyingshiService.insert(sheyingshi);
        return R.ok();
    }


    /**
     * 退出
     */
    @RequestMapping("/logout")
    public R logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return R.ok("退出成功");
    }

    /**
     * 获取用户的session用户信息
     */
    @RequestMapping("/session")
    public R getCurrUser(HttpServletRequest request) {
        Long id = (Long) request.getSession().getAttribute("userId");
        return R.ok().put("data", sheyingshiService.selectView(new EntityWrapper<SheyingshiEntity>().eq("id", id)));
    }

    /**
     * 密码重置
     */
    @IgnoreAuth
    @RequestMapping(value = "/resetPass")
    public R resetPass(String username, HttpServletRequest request) {
        SheyingshiEntity u = sheyingshiService.selectOne(new EntityWrapper<SheyingshiEntity>().eq("sheyingzhanghao", username));
        if (u == null) {
            return R.error("账号不存在");
        }
        u.setMima("123456");
        sheyingshiService.updateById(u);
        return R.ok("密码已重置为：123456");
    }


    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, SheyingshiEntity sheyingshi,
                  HttpServletRequest request) {
        EntityWrapper<SheyingshiEntity> ew = new EntityWrapper<SheyingshiEntity>();


        PageUtils page = sheyingshiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, sheyingshi), params), params));
        return R.ok().put("data", page);
    }


    /**
     * 前端列表
     */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, SheyingshiEntity sheyingshi,
                  HttpServletRequest request) {
        EntityWrapper<SheyingshiEntity> ew = new EntityWrapper<SheyingshiEntity>();

        PageUtils page = sheyingshiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, sheyingshi), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/lists")
    public R list(SheyingshiEntity sheyingshi) {
        EntityWrapper<SheyingshiEntity> ew = new EntityWrapper<SheyingshiEntity>();
        ew.allEq(MPUtil.allEQMapPre(sheyingshi, MPUtil.camelToSnake("sheyingshi")));
        return R.ok().put("data", sheyingshiService.selectListView(ew));
    }

    /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(SheyingshiEntity sheyingshi) {
        EntityWrapper<SheyingshiEntity> ew = new EntityWrapper<SheyingshiEntity>();
        ew.allEq(MPUtil.allEQMapPre(sheyingshi, MPUtil.camelToSnake("sheyingshi")));
        SheyingshiView sheyingshiView = sheyingshiService.selectView(ew);
        return R.ok("查询摄影师成功").put("data", sheyingshiView);
    }

    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        SheyingshiEntity sheyingshi = sheyingshiService.selectById(id);
        sheyingshi = sheyingshiService.selectView(new EntityWrapper<SheyingshiEntity>().eq("id", id));
        return R.ok().put("data", sheyingshi);
    }

    /**
     * 前端详情
     */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id) {
        SheyingshiEntity sheyingshi = sheyingshiService.selectById(id);
        sheyingshi = sheyingshiService.selectView(new EntityWrapper<SheyingshiEntity>().eq("id", id));
        return R.ok().put("data", sheyingshi);
    }


    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody SheyingshiEntity sheyingshi, HttpServletRequest request) {
        if (sheyingshiService.selectCount(new EntityWrapper<SheyingshiEntity>().eq("sheyingzhanghao", sheyingshi.getSheyingzhanghao())) > 0) {
            return R.error("摄影账号已存在");
        }
        sheyingshi.setId(new Date().getTime() + new Double(Math.floor(Math.random() * 1000)).longValue());
        //ValidatorUtils.validateEntity(sheyingshi);
        SheyingshiEntity u = sheyingshiService.selectOne(new EntityWrapper<SheyingshiEntity>().eq("sheyingzhanghao", sheyingshi.getSheyingzhanghao()));
        if (u != null) {
            return R.error("用户已存在");
        }
        sheyingshi.setId(new Date().getTime());
        sheyingshiService.insert(sheyingshi);
        return R.ok();
    }

    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody SheyingshiEntity sheyingshi, HttpServletRequest request) {
        if (sheyingshiService.selectCount(new EntityWrapper<SheyingshiEntity>().eq("sheyingzhanghao", sheyingshi.getSheyingzhanghao())) > 0) {
            return R.error("摄影账号已存在");
        }
        sheyingshi.setId(new Date().getTime() + new Double(Math.floor(Math.random() * 1000)).longValue());
        //ValidatorUtils.validateEntity(sheyingshi);
        SheyingshiEntity u = sheyingshiService.selectOne(new EntityWrapper<SheyingshiEntity>().eq("sheyingzhanghao", sheyingshi.getSheyingzhanghao()));
        if (u != null) {
            return R.error("用户已存在");
        }
        sheyingshi.setId(new Date().getTime());
        sheyingshiService.insert(sheyingshi);
        return R.ok();
    }


    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody SheyingshiEntity sheyingshi, HttpServletRequest request) {
        //ValidatorUtils.validateEntity(sheyingshi);
        sheyingshiService.updateById(sheyingshi);//全部更新
        if (null != sheyingshi.getSheyingzhanghao()) {
            // 修改token
            TokenEntity tokenEntity = new TokenEntity();
            tokenEntity.setUsername(sheyingshi.getSheyingzhanghao());
            tokenService.update(tokenEntity, new EntityWrapper<TokenEntity>().eq("userid", sheyingshi.getId()));
        }
        return R.ok();
    }

    /**
     * 审核
     */
    @RequestMapping("/shBatch")
    @Transactional
    public R update(@RequestBody Long[] ids, @RequestParam String sfsh, @RequestParam String shhf) {
        List<SheyingshiEntity> list = new ArrayList<SheyingshiEntity>();
        for (Long id : ids) {
            SheyingshiEntity sheyingshi = sheyingshiService.selectById(id);
            sheyingshi.setSfsh(sfsh);
            sheyingshi.setShhf(shhf);
            list.add(sheyingshi);
        }
        sheyingshiService.updateBatchById(list);
        return R.ok();
    }


    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        sheyingshiService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }


}
