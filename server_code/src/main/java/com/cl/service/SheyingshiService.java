package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.entity.SheyingshiEntity;
import com.cl.entity.view.SheyingshiView;
import com.cl.utils.PageUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 摄影师
 *
 * @author
 * @email
 * @date 2025-05-22 16:26:52
 */
public interface SheyingshiService extends IService<SheyingshiEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<SheyingshiView> selectListView(Wrapper<SheyingshiEntity> wrapper);

    SheyingshiView selectView(@Param("ew") Wrapper<SheyingshiEntity> wrapper);

    PageUtils queryPage(Map<String, Object> params, Wrapper<SheyingshiEntity> wrapper);


}

