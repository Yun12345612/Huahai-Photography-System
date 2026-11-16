package com.cl.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cl.dao.FriendshipLinkDao;
import com.cl.entity.FriendshipLinkEntity;
import com.cl.entity.view.FriendshipLinkView;
import com.cl.service.FriendshipLinkService;
import com.cl.utils.PageUtils;
import com.cl.utils.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("friendshipLinkService")
public class FriendshipLinkServiceImpl extends ServiceImpl<FriendshipLinkDao, FriendshipLinkEntity> implements FriendshipLinkService {


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<FriendshipLinkEntity> page = this.selectPage(
                new Query<FriendshipLinkEntity>(params).getPage(),
                new EntityWrapper<FriendshipLinkEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Wrapper<FriendshipLinkEntity> wrapper) {
        Page<FriendshipLinkView> page = new Query<FriendshipLinkView>(params).getPage();
        page.setRecords(baseMapper.selectListView(page, wrapper));
        PageUtils pageUtil = new PageUtils(page);
        return pageUtil;
    }

    @Override
    public List<FriendshipLinkView> selectListView(Wrapper<FriendshipLinkEntity> wrapper) {
        return baseMapper.selectListView(wrapper);
    }

    @Override
    public FriendshipLinkView selectView(Wrapper<FriendshipLinkEntity> wrapper) {
        return baseMapper.selectView(wrapper);
    }


}
