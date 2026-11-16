package com.cl.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cl.dao.DiscusssheyingshixinxiDao;
import com.cl.entity.DiscusssheyingshixinxiEntity;
import com.cl.entity.view.DiscusssheyingshixinxiView;
import com.cl.service.DiscusssheyingshixinxiService;
import com.cl.utils.PageUtils;
import com.cl.utils.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("discusssheyingshixinxiService")
public class DiscusssheyingshixinxiServiceImpl extends ServiceImpl<DiscusssheyingshixinxiDao, DiscusssheyingshixinxiEntity> implements DiscusssheyingshixinxiService {


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<DiscusssheyingshixinxiEntity> page = this.selectPage(
                new Query<DiscusssheyingshixinxiEntity>(params).getPage(),
                new EntityWrapper<DiscusssheyingshixinxiEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Wrapper<DiscusssheyingshixinxiEntity> wrapper) {
        Page<DiscusssheyingshixinxiView> page = new Query<DiscusssheyingshixinxiView>(params).getPage();
        page.setRecords(baseMapper.selectListView(page, wrapper));
        PageUtils pageUtil = new PageUtils(page);
        return pageUtil;
    }

    @Override
    public List<DiscusssheyingshixinxiView> selectListView(Wrapper<DiscusssheyingshixinxiEntity> wrapper) {
        return baseMapper.selectListView(wrapper);
    }

    @Override
    public DiscusssheyingshixinxiView selectView(Wrapper<DiscusssheyingshixinxiEntity> wrapper) {
        return baseMapper.selectView(wrapper);
    }


}
