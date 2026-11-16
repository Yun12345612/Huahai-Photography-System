package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.entity.SheyingshixinxiEntity;
import com.cl.entity.view.SheyingshixinxiView;
import com.cl.utils.PageUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 摄影师信息
 *
 * @author
 * @email
 * @date 2025-05-22 16:26:52
 */
public interface SheyingshixinxiService extends IService<SheyingshixinxiEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<SheyingshixinxiView> selectListView(Wrapper<SheyingshixinxiEntity> wrapper);

    SheyingshixinxiView selectView(@Param("ew") Wrapper<SheyingshixinxiEntity> wrapper);

    PageUtils queryPage(Map<String, Object> params, Wrapper<SheyingshixinxiEntity> wrapper);


}

