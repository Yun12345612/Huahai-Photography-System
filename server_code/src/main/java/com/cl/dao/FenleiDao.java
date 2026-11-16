package com.cl.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.cl.entity.FenleiEntity;
import com.cl.entity.view.FenleiView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 分类
 *
 * @author
 * @email
 * @date 2025-05-22 16:26:52
 */
@Mapper
public interface FenleiDao extends BaseMapper<FenleiEntity> {

    List<FenleiView> selectListView(@Param("ew") Wrapper<FenleiEntity> wrapper);

    List<FenleiView> selectListView(Pagination page, @Param("ew") Wrapper<FenleiEntity> wrapper);

    FenleiView selectView(@Param("ew") Wrapper<FenleiEntity> wrapper);


}
