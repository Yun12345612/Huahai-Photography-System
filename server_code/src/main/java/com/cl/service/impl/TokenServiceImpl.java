package com.cl.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cl.dao.TokenDao;
import com.cl.entity.TokenEntity;
import com.cl.service.TokenService;
import com.cl.utils.CommonUtil;
import com.cl.utils.PageUtils;
import com.cl.utils.Query;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * token
 */
@Service("tokenService")
public class TokenServiceImpl extends ServiceImpl<TokenDao, TokenEntity> implements TokenService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        Page<TokenEntity> page = this.selectPage(
                new Query<TokenEntity>(params).getPage(),
                new EntityWrapper<TokenEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public List<TokenEntity> selectListView(Wrapper<TokenEntity> wrapper) {
        return baseMapper.selectListView(wrapper);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params,
                               Wrapper<TokenEntity> wrapper) {
        Page<TokenEntity> page = new Query<TokenEntity>(params).getPage();
        page.setRecords(baseMapper.selectListView(page, wrapper));
        PageUtils pageUtil = new PageUtils(page);
        return pageUtil;
    }

    @Override
    // 覆盖 generateToken 方法，用于生成带有过期时间的令牌
    public String generateToken(Long userid, String username, String tableName, String role) {
        // 查询数据库，获取与指定用户 ID 和角色对应的令牌实体
        TokenEntity tokenEntity = this.selectOne(new EntityWrapper<TokenEntity>().eq("userid", userid).eq("role", role));
        // 生成一个长度为 32 位的随机字符串作为令牌
        String token = CommonUtil.getRandomString(32);
        // 创建一个日历对象，用于处理日期和时间
        Calendar cal = Calendar.getInstance();
        // 设置日历对象的时间为当前系统时间
        cal.setTime(new Date());
        // 在当前时间的基础上增加 1 小时，作为令牌的过期时间
        cal.add(Calendar.HOUR_OF_DAY, 1);
        // 判断令牌实体是否存在
        if (tokenEntity != null) {
            // 如果令牌实体存在，更新令牌和过期时间
            tokenEntity.setToken(token);
            tokenEntity.setExpiratedtime(cal.getTime());
            // 更新数据库中的令牌实体
            this.updateById(tokenEntity);
        } else {
            // 如果令牌实体不存在，创建一个新的令牌实体并插入数据库
            this.insert(new TokenEntity(userid, username, tableName, role, token, cal.getTime()));
        }
        // 返回生成的令牌
        return token;
    }

    @Override
    public TokenEntity getTokenEntity(String token) {
        TokenEntity tokenEntity = this.selectOne(new EntityWrapper<TokenEntity>().eq("token", token));
        if (tokenEntity == null || tokenEntity.getExpiratedtime().getTime() < new Date().getTime()) {
            return null;
        }
        return tokenEntity;
    }
}
