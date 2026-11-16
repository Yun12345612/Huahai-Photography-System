package com.cl.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;


/**
 * 摄影师
 * 数据库通用操作实体类（普通增删改查）
 *
 * @author
 * @email
 * @date 2025-05-22 16:26:52
 */
@TableName("sheyingshi")
public class SheyingshiEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @TableId
    private Long id;
    /**
     * 摄影账号
     */

    private String sheyingzhanghao;
    /**
     * 密码
     */

    private String mima;
    /**
     * 摄影姓名
     */

    private String sheyingxingming;
    /**
     * 头像
     */

    private String touxiang;
    /**
     * 性别
     */

    private String xingbie;
    /**
     * 联系电话
     */

    private String lianxidianhua;
    /**
     * 是否审核
     */

    private String sfsh;
    /**
     * 回复内容
     */

    private String shhf;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date addtime;

    public SheyingshiEntity() {

    }


    public SheyingshiEntity(T t) {
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
     * 获取：密码
     */
    public String getMima() {
        return mima;
    }

    /**
     * 设置：密码
     */
    public void setMima(String mima) {
        this.mima = mima;
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
     * 获取：头像
     */
    public String getTouxiang() {
        return touxiang;
    }

    /**
     * 设置：头像
     */
    public void setTouxiang(String touxiang) {
        this.touxiang = touxiang;
    }

    /**
     * 获取：性别
     */
    public String getXingbie() {
        return xingbie;
    }

    /**
     * 设置：性别
     */
    public void setXingbie(String xingbie) {
        this.xingbie = xingbie;
    }

    /**
     * 获取：联系电话
     */
    public String getLianxidianhua() {
        return lianxidianhua;
    }

    /**
     * 设置：联系电话
     */
    public void setLianxidianhua(String lianxidianhua) {
        this.lianxidianhua = lianxidianhua;
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

}
