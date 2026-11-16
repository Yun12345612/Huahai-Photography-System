package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.entity.HuodongxinxiEntity;
import com.cl.entity.view.HuodongxinxiView;
import com.cl.utils.PageUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 活动信息
 *
 * @author
 * @email
 * @date 2025-05-22 16:26:52
 */
public interface HuodongxinxiService extends IService<HuodongxinxiEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<HuodongxinxiView> selectListView(Wrapper<HuodongxinxiEntity> wrapper);

    HuodongxinxiView selectView(@Param("ew") Wrapper<HuodongxinxiEntity> wrapper);

    PageUtils queryPage(Map<String, Object> params, Wrapper<HuodongxinxiEntity> wrapper);


}

