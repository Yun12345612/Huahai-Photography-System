package com.cl.entity.view;

import com.baomidou.mybatisplus.annotations.TableName;
import com.cl.entity.SheyingshiyuyueEntity;
import org.apache.commons.beanutils.BeanUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;


/**
 * 摄影师预约
 * 后端返回视图实体辅助类
 * （通常后端关联的表或者自定义的字段需要返回使用）
 *
 * @author
 * @email
 * @date 2025-05-22 16:26:52
 */
@TableName("sheyingshiyuyue")
public class SheyingshiyuyueView extends SheyingshiyuyueEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    public SheyingshiyuyueView() {
    }

    public SheyingshiyuyueView(SheyingshiyuyueEntity sheyingshiyuyueEntity) {
        try {
            BeanUtils.copyProperties(this, sheyingshiyuyueEntity);
        } catch (IllegalAccessException | InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


}
