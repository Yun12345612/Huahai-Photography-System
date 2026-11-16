package com.cl.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.cl.entity.SheyingshiyuyueEntity;
import com.cl.entity.view.SheyingshiyuyueView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 摄影师预约
 *
 * @author
 * @email
 * @date 2025-05-22 16:26:52
 */
@Mapper
public interface SheyingshiyuyueDao extends BaseMapper<SheyingshiyuyueEntity> {

    List<SheyingshiyuyueView> selectListView(@Param("ew") Wrapper<SheyingshiyuyueEntity> wrapper);

    List<SheyingshiyuyueView> selectListView(Pagination page, @Param("ew") Wrapper<SheyingshiyuyueEntity> wrapper);

    SheyingshiyuyueView selectView(@Param("ew") Wrapper<SheyingshiyuyueEntity> wrapper);


}
