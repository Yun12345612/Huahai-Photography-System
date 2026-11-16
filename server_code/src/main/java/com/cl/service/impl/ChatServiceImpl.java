package com.cl.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cl.dao.ChatDao;
import com.cl.entity.ChatEntity;
import com.cl.entity.view.ChatView;
import com.cl.service.ChatService;
import com.cl.utils.PageUtils;
import com.cl.utils.Query;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service("chatService")
public class ChatServiceImpl extends ServiceImpl<ChatDao, ChatEntity> implements ChatService {

    /**
     * 查询在线咨询列表（分页）
     * 根据请求参数进行分页查询，返回分页结果
     *
     * @param params 请求参数，包含分页信息和其他查询条件
     * @return 分页结果，包含聊天记录列表和分页信息
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        // 创建分页对象
        Page<ChatEntity> page = this.selectPage(
                new Query<ChatEntity>(params).getPage(),
                new EntityWrapper<ChatEntity>()
        );
        // 返回分页结果
        return new PageUtils(page);
    }

    /**
     * 查询在线咨询列表（分页，带查询条件）
     * 根据请求参数和查询条件封装在Wrapper中进行分页查询，返回分页结果
     *
     * @param params  请求参数，包含分页信息和其他查询条件
     * @param wrapper 查询条件封装
     * @return 分页结果，包含聊天记录列表和分页信息
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params, Wrapper<ChatEntity> wrapper) {
        // 创建分页对象
        Page<ChatView> page = new Query<ChatView>(params).getPage();
        // 设置分页记录
        page.setRecords(baseMapper.selectListView(page, wrapper));
        // 返回分页结果
        PageUtils pageUtil = new PageUtils(page);
        return pageUtil;
    }

    /**
     * 查询在线咨询列表（自定义视图）
     * 根据查询条件封装在Wrapper中，查询聊天记录列表，并转换为自定义视图对象列表
     *
     * @param wrapper 查询条件封装
     * @return 聊天记录视图对象列表
     */
    @Override
    public List<ChatView> selectListView(Wrapper<ChatEntity> wrapper) {
        // 调用Mapper层方法查询聊天记录列表
        return baseMapper.selectListView(wrapper);
    }

    /**
     * 查询单个在线咨询视图
     * 根据查询条件封装在Wrapper中，查询单个聊天记录并转换为视图对象
     *
     * @param wrapper 查询条件封装
     * @return 聊天记录视图对象
     */
    @Override
    public ChatView selectView(Wrapper<ChatEntity> wrapper) {
        // 调用Mapper层方法查询单个聊天记录视图
        return baseMapper.selectView(wrapper);
    }
}