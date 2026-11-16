package com.cl.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;


/**
 * 友情链接
 * 数据库通用操作实体类（普通增删改查）
 *
 * @author
 * @email
 * @date 2025-05-22 16:26:52
 */
@TableName("friendship_link")
public class FriendshipLinkEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 链接名称
     */

    private String linkName;
    /**
     * 链接图片
     */

    private String linkImage;
    /**
     * 链接地址
     */

    private String linkUrl;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date addtime;

    public FriendshipLinkEntity() {

    }


    public FriendshipLinkEntity(T t) {
        try {
            BeanUtils.copyProperties(this, t);
        } catch (IllegalAccessException | InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取：链接名称
     */
    public String getLinkName() {
        return linkName;
    }

    /**
     * 设置：链接名称
     */
    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    /**
     * 获取：链接图片
     */
    public String getLinkImage() {
        return linkImage;
    }

    /**
     * 设置：链接图片
     */
    public void setLinkImage(String linkImage) {
        this.linkImage = linkImage;
    }

    /**
     * 获取：链接地址
     */
    public String getLinkUrl() {
        return linkUrl;
    }

    /**
     * 设置：链接地址
     */
    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

}
