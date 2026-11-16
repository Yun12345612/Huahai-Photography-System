package com.cl.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.cl.entity.SheyingshiEntity;
import com.cl.entity.view.SheyingshiView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 摄影师
 *
 * @author
 * @email
 * @date 2025-05-22 16:26:52
 */
@Mapper
public interface SheyingshiDao extends BaseMapper<SheyingshiEntity> {

    List<SheyingshiView> selectListView(@Param("ew") Wrapper<SheyingshiEntity> wrapper);

    List<SheyingshiView> selectListView(Pagination page, @Param("ew") Wrapper<SheyingshiEntity> wrapper);

    SheyingshiView selectView(@Param("ew") Wrapper<SheyingshiEntity> wrapper);


}
