package com.baocloud.cfg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * spring boot configuration
 * 
 * @author lijinghua
 *
 */

@Configuration
public class AppCfg {
	@Bean(name = { "object" })
	public Object object() {
		return new Object();
	}

}
