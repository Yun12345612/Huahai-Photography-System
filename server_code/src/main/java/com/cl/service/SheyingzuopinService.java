package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.entity.SheyingzuopinEntity;
import com.cl.entity.view.SheyingzuopinView;
import com.cl.utils.PageUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 摄影作品
 *
 * @author
 * @email
 * @date 2025-05-22 16:26:52
 */
public interface SheyingzuopinService extends IService<SheyingzuopinEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<SheyingzuopinView> selectListView(Wrapper<SheyingzuopinEntity> wrapper);

    SheyingzuopinView selectView(@Param("ew") Wrapper<SheyingzuopinEntity> wrapper);

    PageUtils queryPage(Map<String, Object> params, Wrapper<SheyingzuopinEntity> wrapper);


}

