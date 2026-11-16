package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.entity.DiscusssheyingshixinxiEntity;
import com.cl.entity.view.DiscusssheyingshixinxiView;
import com.cl.utils.PageUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 摄影师信息评论表
 *
 * @author
 * @email
 * @date 2025-05-22 16:26:52
 */
public interface DiscusssheyingshixinxiService extends IService<DiscusssheyingshixinxiEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<DiscusssheyingshixinxiView> selectListView(Wrapper<DiscusssheyingshixinxiEntity> wrapper);

    DiscusssheyingshixinxiView selectView(@Param("ew") Wrapper<DiscusssheyingshixinxiEntity> wrapper);

    PageUtils queryPage(Map<String, Object> params, Wrapper<DiscusssheyingshixinxiEntity> wrapper);


}

