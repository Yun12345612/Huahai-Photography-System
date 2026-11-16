package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.entity.ChatEntity;
import com.cl.entity.view.ChatView;
import com.cl.utils.PageUtils;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * 在线咨询业务逻辑接口
 *
 * @author
 * @email
 * @date 2025-05-22 16:26:51
 */
public interface ChatService extends IService<ChatEntity> {

    /**
     * 查询在线咨询列表（分页）
     *
     * @param params 请求参数，包含分页信息和其他查询条件
     * @return 分页结果，包含聊天记录列表和分页信息
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询在线咨询列表（自定义视图）
     *
     * @param wrapper 查询条件封装
     * @return 聊天记录视图对象列表
     */
    List<ChatView> selectListView(Wrapper<ChatEntity> wrapper);

    /**
     * 查询单个在线咨询视图
     *
     * @param wrapper 查询条件封装
     * @return 聊天记录视图对象
     */
    ChatView selectView(@Param("ew") Wrapper<ChatEntity> wrapper);

    /**
     * 查询在线咨询列表（分页，带查询条件）
     *
     * @param params  请求参数，包含分页信息和其他查询条件
     * @param wrapper 查询条件封装
     * @return 分页结果，包含聊天记录列表和分页信息
     */
    PageUtils queryPage(Map<String, Object> params, Wrapper<ChatEntity> wrapper);
}