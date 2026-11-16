package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.entity.ForumEntity;
import com.cl.entity.view.ForumView;
import com.cl.utils.PageUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 互动社交
 *
 * @author
 * @email
 * @date 2025-05-22 16:26:51
 */
public interface ForumService extends IService<ForumEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<ForumView> selectListView(Wrapper<ForumEntity> wrapper);

    ForumView selectView(@Param("ew") Wrapper<ForumEntity> wrapper);

    PageUtils queryPage(Map<String, Object> params, Wrapper<ForumEntity> wrapper);


}

