package com.baocloud.processor;

import java.util.Date;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.baocloud.beans.SimpleBean;

@Component
public class CustomBeanProcessor implements BeanPostProcessor {
	// private final Logger logger=Logger.getLogger(CustomBeanProcessor.class);

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean.getClass().equals(SimpleBean.class)) {
			SimpleBean tmp = (SimpleBean) bean;
			if (StringUtils.isEmpty(tmp.getName()))
				tmp.setName("李景华");
			tmp.setCreateDate(new Date());
		}
		return bean;
	}

}
