package com.cl.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.cl.entity.ForumEntity;
import com.cl.entity.view.ForumView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 互动社交
 *
 * @author
 * @email
 * @date 2025-05-22 16:26:51
 */
@Mapper
public interface ForumDao extends BaseMapper<ForumEntity> {

    List<ForumView> selectListView(@Param("ew") Wrapper<ForumEntity> wrapper);

    List<ForumView> selectListView(Pagination page, @Param("ew") Wrapper<ForumEntity> wrapper);

    ForumView selectView(@Param("ew") Wrapper<ForumEntity> wrapper);


}
