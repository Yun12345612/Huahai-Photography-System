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
 * 报名信息
 * 数据库通用操作实体类（普通增删改查）
 *
 * @author
 * @email
 * @date 2025-05-22 16:26:52
 */
@TableName("baomingxinxi")
public class BaomingxinxiEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 活动名称
     */

    private String huodongmingcheng;
    /**
     * 活动图片
     */

    private String huodongtupian;
    /**
     * 开始时间
     */

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date kaishishijian;
    /**
     * 结束时间
     */

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date jieshushijian;
    /**
     * 活动地点
     */

    private String huodongdidian;
    /**
     * 用户账号
     */

    private String yonghuzhanghao;
    /**
     * 用户姓名
     */

    private String yonghuxingming;
    /**
     * 跨表用户id
     */

    private Long crossuserid;
    /**
     * 跨表主键id
     */

    private Long crossrefid;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date addtime;

    public BaomingxinxiEntity() {

    }


    public BaomingxinxiEntity(T t) {
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
     * 获取：活动名称
     */
    public String getHuodongmingcheng() {
        return huodongmingcheng;
    }

    /**
     * 设置：活动名称
     */
    public void setHuodongmingcheng(String huodongmingcheng) {
        this.huodongmingcheng = huodongmingcheng;
    }

    /**
     * 获取：活动图片
     */
    public String getHuodongtupian() {
        return huodongtupian;
    }

    /**
     * 设置：活动图片
     */
    public void setHuodongtupian(String huodongtupian) {
        this.huodongtupian = huodongtupian;
    }

    /**
     * 获取：开始时间
     */
    public Date getKaishishijian() {
        return kaishishijian;
    }

    /**
     * 设置：开始时间
     */
    public void setKaishishijian(Date kaishishijian) {
        this.kaishishijian = kaishishijian;
    }

    /**
     * 获取：结束时间
     */
    public Date getJieshushijian() {
        return jieshushijian;
    }

    /**
     * 设置：结束时间
     */
    public void setJieshushijian(Date jieshushijian) {
        this.jieshushijian = jieshushijian;
    }

    /**
     * 获取：活动地点
     */
    public String getHuodongdidian() {
        return huodongdidian;
    }

    /**
     * 设置：活动地点
     */
    public void setHuodongdidian(String huodongdidian) {
        this.huodongdidian = huodongdidian;
    }

    /**
     * 获取：用户账号
     */
    public String getYonghuzhanghao() {
        return yonghuzhanghao;
    }

    /**
     * 设置：用户账号
     */
    public void setYonghuzhanghao(String yonghuzhanghao) {
        this.yonghuzhanghao = yonghuzhanghao;
    }

    /**
     * 获取：用户姓名
     */
    public String getYonghuxingming() {
        return yonghuxingming;
    }

    /**
     * 设置：用户姓名
     */
    public void setYonghuxingming(String yonghuxingming) {
        this.yonghuxingming = yonghuxingming;
    }

    /**
     * 获取：跨表用户id
     */
    public Long getCrossuserid() {
        return crossuserid;
    }

    /**
     * 设置：跨表用户id
     */
    public void setCrossuserid(Long crossuserid) {
        this.crossuserid = crossuserid;
    }

    /**
     * 获取：跨表主键id
     */
    public Long getCrossrefid() {
        return crossrefid;
    }

    /**
     * 设置：跨表主键id
     */
    public void setCrossrefid(Long crossrefid) {
        this.crossrefid = crossrefid;
    }

}
