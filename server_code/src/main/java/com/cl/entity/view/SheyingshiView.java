package com.cl.entity.view;

import com.baomidou.mybatisplus.annotations.TableName;
import com.cl.entity.SheyingshiEntity;
import org.apache.commons.beanutils.BeanUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;


/**
 * 摄影师
 * 后端返回视图实体辅助类
 * （通常后端关联的表或者自定义的字段需要返回使用）
 *
 * @author
 * @email
 * @date 2025-05-22 16:26:52
 */
@TableName("sheyingshi")
public class SheyingshiView extends SheyingshiEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    public SheyingshiView() {
    }

    public SheyingshiView(SheyingshiEntity sheyingshiEntity) {
        try {
            BeanUtils.copyProperties(this, sheyingshiEntity);
        } catch (IllegalAccessException | InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


}
