package com.cl.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cl.dao.SheyingshixinxiDao;
import com.cl.entity.SheyingshixinxiEntity;
import com.cl.entity.view.SheyingshixinxiView;
import com.cl.service.SheyingshixinxiService;
import com.cl.utils.PageUtils;
import com.cl.utils.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("sheyingshixinxiService")
public class SheyingshixinxiServiceImpl extends ServiceImpl<SheyingshixinxiDao, SheyingshixinxiEntity> implements SheyingshixinxiService {


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<SheyingshixinxiEntity> page = this.selectPage(
                new Query<SheyingshixinxiEntity>(params).getPage(),
                new EntityWrapper<SheyingshixinxiEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Wrapper<SheyingshixinxiEntity> wrapper) {
        Page<SheyingshixinxiView> page = new Query<SheyingshixinxiView>(params).getPage();
        page.setRecords(baseMapper.selectListView(page, wrapper));
        PageUtils pageUtil = new PageUtils(page);
        return pageUtil;
    }

    @Override
    public List<SheyingshixinxiView> selectListView(Wrapper<SheyingshixinxiEntity> wrapper) {
        return baseMapper.selectListView(wrapper);
    }

    @Override
    public SheyingshixinxiView selectView(Wrapper<SheyingshixinxiEntity> wrapper) {
        return baseMapper.selectView(wrapper);
    }


}
