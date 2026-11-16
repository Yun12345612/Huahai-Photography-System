package com.cl.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.cl.entity.BaomingxinxiEntity;
import com.cl.entity.view.BaomingxinxiView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 报名信息
 *
 * @author
 * @email
 * @date 2025-05-22 16:26:52
 */
@Mapper
public interface BaomingxinxiDao extends BaseMapper<BaomingxinxiEntity> {

    List<BaomingxinxiView> selectListView(@Param("ew") Wrapper<BaomingxinxiEntity> wrapper);

    List<BaomingxinxiView> selectListView(Pagination page, @Param("ew") Wrapper<BaomingxinxiEntity> wrapper);

    BaomingxinxiView selectView(@Param("ew") Wrapper<BaomingxinxiEntity> wrapper);


}
