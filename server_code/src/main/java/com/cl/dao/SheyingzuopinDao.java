package com.cl.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.cl.entity.SheyingzuopinEntity;
import com.cl.entity.view.SheyingzuopinView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 摄影作品
 *
 * @author
 * @email
 * @date 2025-05-22 16:26:52
 */
@Mapper
public interface SheyingzuopinDao extends BaseMapper<SheyingzuopinEntity> {

    List<SheyingzuopinView> selectListView(@Param("ew") Wrapper<SheyingzuopinEntity> wrapper);

    List<SheyingzuopinView> selectListView(Pagination page, @Param("ew") Wrapper<SheyingzuopinEntity> wrapper);

    SheyingzuopinView selectView(@Param("ew") Wrapper<SheyingzuopinEntity> wrapper);


}
