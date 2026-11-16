package com.cl.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cl.dao.SheyingshiDao;
import com.cl.entity.SheyingshiEntity;
import com.cl.entity.view.SheyingshiView;
import com.cl.service.SheyingshiService;
import com.cl.utils.PageUtils;
import com.cl.utils.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("sheyingshiService")
public class SheyingshiServiceImpl extends ServiceImpl<SheyingshiDao, SheyingshiEntity> implements SheyingshiService {


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<SheyingshiEntity> page = this.selectPage(
                new Query<SheyingshiEntity>(params).getPage(),
                new EntityWrapper<SheyingshiEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Wrapper<SheyingshiEntity> wrapper) {
        Page<SheyingshiView> page = new Query<SheyingshiView>(params).getPage();
        page.setRecords(baseMapper.selectListView(page, wrapper));
        PageUtils pageUtil = new PageUtils(page);
        return pageUtil;
    }

    @Override
    public List<SheyingshiView> selectListView(Wrapper<SheyingshiEntity> wrapper) {
        return baseMapper.selectListView(wrapper);
    }

    @Override
    public SheyingshiView selectView(Wrapper<SheyingshiEntity> wrapper) {
        return baseMapper.selectView(wrapper);
    }


}
