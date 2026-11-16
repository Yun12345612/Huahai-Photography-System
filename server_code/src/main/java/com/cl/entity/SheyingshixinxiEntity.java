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
 * 摄影师信息
 * 数据库通用操作实体类（普通增删改查）
 *
 * @author
 * @email
 * @date 2025-05-22 16:26:52
 */
@TableName("sheyingshixinxi")
public class SheyingshixinxiEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 摄影账号
     */

    private String sheyingzhanghao;
    /**
     * 摄影姓名
     */

    private String sheyingxingming;
    /**
     * 摄影师照片
     */

    private String sheyingshizhaopian;
    /**
     * 分类
     */

    private String fenlei;
    /**
     * 简介
     */

    private String jianjie;
    /**
     * 预约价格
     */

    private Double yuyuejiage;
    /**
     * 工作经历
     */

    private String gongzuojingli;
    /**
     * 专业技能
     */

    private String zhuanyejineng;
    /**
     * 个人特长
     */

    private String gerentezhang;
    /**
     * 是否审核
     */

    private String sfsh;
    /**
     * 回复内容
     */

    private String shhf;
    /**
     * 评论数
     */

    private Integer discussNumber;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date addtime;

    public SheyingshixinxiEntity() {

    }


    public SheyingshixinxiEntity(T t) {
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
     * 获取：摄影师照片
     */
    public String getSheyingshizhaopian() {
        return sheyingshizhaopian;
    }

    /**
     * 设置：摄影师照片
     */
    public void setSheyingshizhaopian(String sheyingshizhaopian) {
        this.sheyingshizhaopian = sheyingshizhaopian;
    }

    /**
     * 获取：分类
     */
    public String getFenlei() {
        return fenlei;
    }

    /**
     * 设置：分类
     */
    public void setFenlei(String fenlei) {
        this.fenlei = fenlei;
    }

    /**
     * 获取：简介
     */
    public String getJianjie() {
        return jianjie;
    }

    /**
     * 设置：简介
     */
    public void setJianjie(String jianjie) {
        this.jianjie = jianjie;
    }

    /**
     * 获取：预约价格
     */
    public Double getYuyuejiage() {
        return yuyuejiage;
    }

    /**
     * 设置：预约价格
     */
    public void setYuyuejiage(Double yuyuejiage) {
        this.yuyuejiage = yuyuejiage;
    }

    /**
     * 获取：工作经历
     */
    public String getGongzuojingli() {
        return gongzuojingli;
    }

    /**
     * 设置：工作经历
     */
    public void setGongzuojingli(String gongzuojingli) {
        this.gongzuojingli = gongzuojingli;
    }

    /**
     * 获取：专业技能
     */
    public String getZhuanyejineng() {
        return zhuanyejineng;
    }

    /**
     * 设置：专业技能
     */
    public void setZhuanyejineng(String zhuanyejineng) {
        this.zhuanyejineng = zhuanyejineng;
    }

    /**
     * 获取：个人特长
     */
    public String getGerentezhang() {
        return gerentezhang;
    }

    /**
     * 设置：个人特长
     */
    public void setGerentezhang(String gerentezhang) {
        this.gerentezhang = gerentezhang;
    }

    /**
     * 获取：是否审核
     */
    public String getSfsh() {
        return sfsh;
    }

    /**
     * 设置：是否审核
     */
    public void setSfsh(String sfsh) {
        this.sfsh = sfsh;
    }

    /**
     * 获取：回复内容
     */
    public String getShhf() {
        return shhf;
    }

    /**
     * 设置：回复内容
     */
    public void setShhf(String shhf) {
        this.shhf = shhf;
    }

    /**
     * 获取：评论数
     */
    public Integer getDiscussNumber() {
        return discussNumber;
    }

    /**
     * 设置：评论数
     */
    public void setDiscussNumber(Integer discussNumber) {
        this.discussNumber = discussNumber;
    }

}
