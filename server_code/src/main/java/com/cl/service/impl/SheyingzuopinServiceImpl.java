package com.cl.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cl.dao.SheyingzuopinDao;
import com.cl.entity.SheyingzuopinEntity;
import com.cl.entity.view.SheyingzuopinView;
import com.cl.service.SheyingzuopinService;
import com.cl.utils.PageUtils;
import com.cl.utils.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("sheyingzuopinService")
public class SheyingzuopinServiceImpl extends ServiceImpl<SheyingzuopinDao, SheyingzuopinEntity> implements SheyingzuopinService {


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<SheyingzuopinEntity> page = this.selectPage(
                new Query<SheyingzuopinEntity>(params).getPage(),
                new EntityWrapper<SheyingzuopinEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Wrapper<SheyingzuopinEntity> wrapper) {
        Page<SheyingzuopinView> page = new Query<SheyingzuopinView>(params).getPage();
        page.setRecords(baseMapper.selectListView(page, wrapper));
        PageUtils pageUtil = new PageUtils(page);
        return pageUtil;
    }

    @Override
    public List<SheyingzuopinView> selectListView(Wrapper<SheyingzuopinEntity> wrapper) {
        return baseMapper.selectListView(wrapper);
    }

    @Override
    public SheyingzuopinView selectView(Wrapper<SheyingzuopinEntity> wrapper) {
        return baseMapper.selectView(wrapper);
    }


}
