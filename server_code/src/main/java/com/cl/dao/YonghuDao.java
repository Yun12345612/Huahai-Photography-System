package com.cl.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.cl.entity.YonghuEntity;
import com.cl.entity.view.YonghuView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 用户
 *
 * @author
 * @email
 * @date 2025-05-22 16:26:52
 */
@Mapper
public interface YonghuDao extends BaseMapper<YonghuEntity> {

    List<YonghuView> selectListView(@Param("ew") Wrapper<YonghuEntity> wrapper);

    List<YonghuView> selectListView(Pagination page, @Param("ew") Wrapper<YonghuEntity> wrapper);

    YonghuView selectView(@Param("ew") Wrapper<YonghuEntity> wrapper);


}
