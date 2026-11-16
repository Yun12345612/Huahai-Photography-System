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
 * 摄影作品
 * 数据库通用操作实体类（普通增删改查）
 *
 * @author
 * @email
 * @date 2025-05-22 16:26:52
 */
@TableName("sheyingzuopin")
public class SheyingzuopinEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 作品名称
     */

    private String zuopinmingcheng;
    /**
     * 摄影账号
     */

    private String sheyingzhanghao;
    /**
     * 摄影姓名
     */

    private String sheyingxingming;
    /**
     * 作品简介
     */

    private String zuopinjianjie;
    /**
     * 作品图片
     */

    private String zuopintupian;
    /**
     * 作品概述
     */

    private String zuopingaishu;
    /**
     * 创作背景
     */

    private String chuangzuobeijing;
    /**
     * 发布时间
     */

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date fabushijian;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date addtime;

    public SheyingzuopinEntity() {

    }


    public SheyingzuopinEntity(T t) {
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
     * 获取：作品名称
     */
    public String getZuopinmingcheng() {
        return zuopinmingcheng;
    }

    /**
     * 设置：作品名称
     */
    public void setZuopinmingcheng(String zuopinmingcheng) {
        this.zuopinmingcheng = zuopinmingcheng;
    }

    /**
     * 获取：摄影账号
     */
    public String getSheyingzhanghao() {
        return sheyingzhanghao;
    }

    /**
     * 设置：摄影账号
     */
    public void setSheyingzhanghao(String sheyingzhanghao) {
        this.sheyingzhanghao = sheyingzhanghao;
    }

    /**
     * 获取：摄影姓名
     */
    public String getSheyingxingming() {
        return sheyingxingming;
    }

    /**
     * 设置：摄影姓名
     */
    public void setSheyingxingming(String sheyingxingming) {
        this.sheyingxingming = sheyingxingming;
    }

    /**
     * 获取：作品简介
     */
    public String getZuopinjianjie() {
        return zuopinjianjie;
    }

    /**
     * 设置：作品简介
     */
    public void setZuopinjianjie(String zuopinjianjie) {
        this.zuopinjianjie = zuopinjianjie;
    }

    /**
     * 获取：作品图片
     */
    public String getZuopintupian() {
        return zuopintupian;
    }

    /**
     * 设置：作品图片
     */
    public void setZuopintupian(String zuopintupian) {
        this.zuopintupian = zuopintupian;
    }

    /**
     * 获取：作品概述
     */
    public String getZuopingaishu() {
        return zuopingaishu;
    }

    /**
     * 设置：作品概述
     */
    public void setZuopingaishu(String zuopingaishu) {
        this.zuopingaishu = zuopingaishu;
    }

    /**
     * 获取：创作背景
     */
    public String getChuangzuobeijing() {
        return chuangzuobeijing;
    }

    /**
     * 设置：创作背景
     */
    public void setChuangzuobeijing(String chuangzuobeijing) {
        this.chuangzuobeijing = chuangzuobeijing;
    }

    /**
     * 获取：发布时间
     */
    public Date getFabushijian() {
        return fabushijian;
    }

    /**
     * 设置：发布时间
     */
    public void setFabushijian(Date fabushijian) {
        this.fabushijian = fabushijian;
    }

}
