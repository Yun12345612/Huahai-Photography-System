package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.entity.SheyingshiyuyueEntity;
import com.cl.entity.view.SheyingshiyuyueView;
import com.cl.utils.PageUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 摄影师预约
 *
 * @author
 * @email
 * @date 2025-05-22 16:26:52
 */
public interface SheyingshiyuyueService extends IService<SheyingshiyuyueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<SheyingshiyuyueView> selectListView(Wrapper<SheyingshiyuyueEntity> wrapper);

    SheyingshiyuyueView selectView(@Param("ew") Wrapper<SheyingshiyuyueEntity> wrapper);

    PageUtils queryPage(Map<String, Object> params, Wrapper<SheyingshiyuyueEntity> wrapper);


}

