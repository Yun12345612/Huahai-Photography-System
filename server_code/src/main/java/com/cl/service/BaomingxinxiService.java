package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.entity.BaomingxinxiEntity;
import com.cl.entity.view.BaomingxinxiView;
import com.cl.utils.PageUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 报名信息服务接口，定义了对报名信息进行增删改查等操作的方法
 *
 * @author
 * @email
 * @date 2025-05-22 16:26:52
 */
public interface BaomingxinxiService extends IService<BaomingxinxiEntity> {

    /**
     * 查询报名信息列表（分页）
     * 根据请求参数进行分页查询，返回分页结果
     *
     * @param params 请求参数，可能包含分页信息和其他查询条件
     * @return 分页结果，包含报名信息列表和分页信息
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询报名信息列表（自定义视图）
     * 根据查询条件封装在Wrapper中，查询报名信息列表，并转换为自定义视图对象列表
     *
     * @param wrapper 查询条件封装
     * @return 报名信息视图对象列表
     */
    List<BaomingxinxiView> selectListView(Wrapper<BaomingxinxiEntity> wrapper);

    /**
     * 查询单个报名信息视图
     * 根据查询条件封装在Wrapper中，查询单个报名信息并转换为视图对象
     *
     * @param wrapper 查询条件封装
     * @return 报名信息视图对象
     */
    BaomingxinxiView selectView(@Param("ew") Wrapper<BaomingxinxiEntity> wrapper);

    /**
     * 查询报名信息列表（分页，带查询条件）
     * 根据请求参数和查询条件封装在Wrapper中进行分页查询，返回分页结果
     *
     * @param params 请求参数，可能包含分页信息和其他查询条件
     * @param wrapper 查询条件封装
     * @return 分页结果，包含报名信息列表和分页信息
     */
    PageUtils queryPage(Map<String, Object> params, Wrapper<BaomingxinxiEntity> wrapper);
}

