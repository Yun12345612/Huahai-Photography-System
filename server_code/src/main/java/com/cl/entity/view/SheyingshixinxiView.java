package com.cl.entity.view;

import com.baomidou.mybatisplus.annotations.TableName;
import com.cl.entity.SheyingshixinxiEntity;
import org.apache.commons.beanutils.BeanUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;


/**
 * 摄影师信息
 * 后端返回视图实体辅助类
 * （通常后端关联的表或者自定义的字段需要返回使用）
 *
 * @author
 * @email
 * @date 2025-05-22 16:26:52
 */
@TableName("sheyingshixinxi")
public class SheyingshixinxiView extends SheyingshixinxiEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    public SheyingshixinxiView() {
    }

    public SheyingshixinxiView(SheyingshixinxiEntity sheyingshixinxiEntity) {
        try {
            BeanUtils.copyProperties(this, sheyingshixinxiEntity);
        } catch (IllegalAccessException | InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


}
