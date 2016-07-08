package com.dolphin.rpc.spring;

import java.lang.reflect.Field;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

import com.dolphin.rpc.proxy.RPCFactory;

/**
 * spring初始化Bean时注入远程资源类
 * @author wuque
 * @version $Id: RemotingBeanPostProcessor.java, v 0.1 2016年5月26日 下午2:34:12 wuque Exp $
 */
@Component
public class RemotingBeanPostProcessor implements BeanPostProcessor, PriorityOrdered {

    @Override
    public Object postProcessBeforeInitialization(Object bean,
                                                  String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(RPCResource.class)) {
                field.setAccessible(true);
                Object service = RPCFactory.getService(field.getType());
                try {
                    field.set(bean, service);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean,
                                                 String beanName) throws BeansException {
        return bean;
    }

    @Override
    public int getOrder() {
        return 0;
    }

}