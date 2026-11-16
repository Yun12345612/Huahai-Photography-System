package com.cl.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.cl.entity.DiscusssheyingshixinxiEntity;
import com.cl.entity.view.DiscusssheyingshixinxiView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 摄影师信息评论表
 *
 * @author
 * @email
 * @date 2025-05-22 16:26:52
 */
@Mapper
public interface DiscusssheyingshixinxiDao extends BaseMapper<DiscusssheyingshixinxiEntity> {

    List<DiscusssheyingshixinxiView> selectListView(@Param("ew") Wrapper<DiscusssheyingshixinxiEntity> wrapper);

    List<DiscusssheyingshixinxiView> selectListView(Pagination page, @Param("ew") Wrapper<DiscusssheyingshixinxiEntity> wrapper);

    DiscusssheyingshixinxiView selectView(@Param("ew") Wrapper<DiscusssheyingshixinxiEntity> wrapper);


}
