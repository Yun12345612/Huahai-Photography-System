package com.cl.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.cl.entity.SheyingshixinxiEntity;
import com.cl.entity.view.SheyingshixinxiView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 摄影师信息
 *
 * @author
 * @email
 * @date 2025-05-22 16:26:52
 */
@Mapper
public interface SheyingshixinxiDao extends BaseMapper<SheyingshixinxiEntity> {

    List<SheyingshixinxiView> selectListView(@Param("ew") Wrapper<SheyingshixinxiEntity> wrapper);

    List<SheyingshixinxiView> selectListView(Pagination page, @Param("ew") Wrapper<SheyingshixinxiEntity> wrapper);

    SheyingshixinxiView selectView(@Param("ew") Wrapper<SheyingshixinxiEntity> wrapper);


}
