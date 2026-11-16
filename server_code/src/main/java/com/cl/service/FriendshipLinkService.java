package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.entity.FriendshipLinkEntity;
import com.cl.entity.view.FriendshipLinkView;
import com.cl.utils.PageUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 友情链接
 *
 * @author
 * @email
 * @date 2025-05-22 16:26:52
 */
public interface FriendshipLinkService extends IService<FriendshipLinkEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<FriendshipLinkView> selectListView(Wrapper<FriendshipLinkEntity> wrapper);

    FriendshipLinkView selectView(@Param("ew") Wrapper<FriendshipLinkEntity> wrapper);

    PageUtils queryPage(Map<String, Object> params, Wrapper<FriendshipLinkEntity> wrapper);


}

