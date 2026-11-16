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
 * 摄影师预约
 * 数据库通用操作实体类（普通增删改查）
 *
 * @author
 * @email
 * @date 2025-05-22 16:26:52
 */
@TableName("sheyingshiyuyue")
public class SheyingshiyuyueEntity<T> implements Serializable {
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
     * 预约价格
     */

    private Double yuyuejiage;
    /**
     * 是否审核
     */

    private String sfsh;
    /**
     * 回复内容
     */

    private String shhf;
    /**
     * 用户账号
     */

    private String yonghuzhanghao;
    /**
     * 用户姓名
     */

    private String yonghuxingming;
    /**
     * 用户电话
     */
    private String lianxidianhua;


    /**
     * 预约摄影日期
     */

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date yuyuesheyingriqi;
    /**
     * 预约摄影时间段
     */
    private String yuyueshijianduan;

    /**
     * 是否支付
     */

    private String ispay;
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

    public SheyingshiyuyueEntity() {

    }


    public SheyingshiyuyueEntity(T t) {
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
     * 获取：预约摄影日期
     */
    public Date getYuyuesheyingriqi() {
        return yuyuesheyingriqi;
    }

    /**
     * 设置：预约摄影日期
     */
    public void setYuyuesheyingriqi(Date yuyuesheyingriqi) {
        this.yuyuesheyingriqi = yuyuesheyingriqi;
    }

    /**
     * 获取：是否支付
     */
    public String getIspay() {
        return ispay;
    }

    /**
     * 设置：是否支付
     */
    public void setIspay(String ispay) {
        this.ispay = ispay;
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


    public String getLianxidianhua() {
        return lianxidianhua;
    }

    public void setLianxidianhua(String lianxidianhua) {
        this.lianxidianhua = lianxidianhua;
    }

    /**
     * 设置：跨表主键id
     */
    public void setCrossrefid(Long crossrefid) {
        this.crossrefid = crossrefid;
    }

    public String getYuyueshijianduan() {
        return yuyueshijianduan;
    }

    public void setYuyueshijianduan(String yuyueshijianduan) {
        this.yuyueshijianduan = yuyueshijianduan;
    }
}
