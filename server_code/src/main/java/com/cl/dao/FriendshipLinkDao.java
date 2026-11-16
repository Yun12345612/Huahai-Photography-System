package com.cl.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.cl.entity.FriendshipLinkEntity;
import com.cl.entity.view.FriendshipLinkView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 友情链接
 *
 * @author
 * @email
 * @date 2025-05-22 16:26:52
 */
@Mapper
public interface FriendshipLinkDao extends BaseMapper<FriendshipLinkEntity> {

    List<FriendshipLinkView> selectListView(@Param("ew") Wrapper<FriendshipLinkEntity> wrapper);

    List<FriendshipLinkView> selectListView(Pagination page, @Param("ew") Wrapper<FriendshipLinkEntity> wrapper);

    FriendshipLinkView selectView(@Param("ew") Wrapper<FriendshipLinkEntity> wrapper);


}
