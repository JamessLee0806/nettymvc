package com.baocloud.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

@Component
public class DataSourceBeanProcessor implements BeanFactoryPostProcessor {

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		BeanDefinition bdf = beanFactory.getBeanDefinition("simpleBean");
		System.out.println(bdf);
		if (bdf != null) {
			MutablePropertyValues prop = bdf.getPropertyValues();
			System.out.println(prop);
			prop.add("name", "lijinghua");

		}
	}

}
