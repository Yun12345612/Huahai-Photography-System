package com.cl.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cl.dao.SheyingshiyuyueDao;
import com.cl.entity.SheyingshiyuyueEntity;
import com.cl.entity.view.SheyingshiyuyueView;
import com.cl.service.SheyingshiyuyueService;
import com.cl.utils.PageUtils;
import com.cl.utils.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("sheyingshiyuyueService")
public class SheyingshiyuyueServiceImpl extends ServiceImpl<SheyingshiyuyueDao, SheyingshiyuyueEntity> implements SheyingshiyuyueService {


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<SheyingshiyuyueEntity> page = this.selectPage(
                new Query<SheyingshiyuyueEntity>(params).getPage(),
                new EntityWrapper<SheyingshiyuyueEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Wrapper<SheyingshiyuyueEntity> wrapper) {
        Page<SheyingshiyuyueView> page = new Query<SheyingshiyuyueView>(params).getPage();
        page.setRecords(baseMapper.selectListView(page, wrapper));
        PageUtils pageUtil = new PageUtils(page);
        return pageUtil;
    }

    @Override
    public List<SheyingshiyuyueView> selectListView(Wrapper<SheyingshiyuyueEntity> wrapper) {
        return baseMapper.selectListView(wrapper);
    }

    @Override
    public SheyingshiyuyueView selectView(Wrapper<SheyingshiyuyueEntity> wrapper) {
        return baseMapper.selectView(wrapper);
    }


}
