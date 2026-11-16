package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.entity.FenleiEntity;
import com.cl.entity.view.FenleiView;
import com.cl.utils.PageUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 分类
 *
 * @author
 * @email
 * @date 2025-05-22 16:26:52
 */
public interface FenleiService extends IService<FenleiEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<FenleiView> selectListView(Wrapper<FenleiEntity> wrapper);

    FenleiView selectView(@Param("ew") Wrapper<FenleiEntity> wrapper);

    PageUtils queryPage(Map<String, Object> params, Wrapper<FenleiEntity> wrapper);


}

