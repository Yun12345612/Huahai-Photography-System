package com.cl.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.cl.entity.HuodongxinxiEntity;
import com.cl.entity.view.HuodongxinxiView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 活动信息
 *
 * @author
 * @email
 * @date 2025-05-22 16:26:52
 */
@Mapper
public interface HuodongxinxiDao extends BaseMapper<HuodongxinxiEntity> {

    List<HuodongxinxiView> selectListView(@Param("ew") Wrapper<HuodongxinxiEntity> wrapper);

    List<HuodongxinxiView> selectListView(Pagination page, @Param("ew") Wrapper<HuodongxinxiEntity> wrapper);

    HuodongxinxiView selectView(@Param("ew") Wrapper<HuodongxinxiEntity> wrapper);


}
