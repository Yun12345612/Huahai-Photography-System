package com.cl.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cl.dao.BaomingxinxiDao;
import com.cl.entity.BaomingxinxiEntity;
import com.cl.entity.view.BaomingxinxiView;
import com.cl.service.BaomingxinxiService;
import com.cl.utils.PageUtils;
import com.cl.utils.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("baomingxinxiService")
public class BaomingxinxiServiceImpl extends ServiceImpl<BaomingxinxiDao, BaomingxinxiEntity> implements BaomingxinxiService {


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<BaomingxinxiEntity> page = this.selectPage(
                new Query<BaomingxinxiEntity>(params).getPage(),
                new EntityWrapper<BaomingxinxiEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Wrapper<BaomingxinxiEntity> wrapper) {
        Page<BaomingxinxiView> page = new Query<BaomingxinxiView>(params).getPage();
        page.setRecords(baseMapper.selectListView(page, wrapper));
        PageUtils pageUtil = new PageUtils(page);
        return pageUtil;
    }

    @Override
    public List<BaomingxinxiView> selectListView(Wrapper<BaomingxinxiEntity> wrapper) {
        return baseMapper.selectListView(wrapper);
    }

    @Override
    public BaomingxinxiView selectView(Wrapper<BaomingxinxiEntity> wrapper) {
        return baseMapper.selectView(wrapper);
    }


}
