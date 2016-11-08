package com.netty.mvc.sps.cfg.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@PropertySource(value="classpath:redis.properties",encoding="UTF-8")
public class RedisProperty {
	@Value("${redis.pool.maxActive}")
	private int maxActive;
	@Value("${redis.pool.maxIdle}")
	private int maxIdle;
	@Value("${redis.pool.maxWait}")
	private int maxWait;
	@Value("${redis.pool.testOnBorrow}")
	private boolean testOnBorrow;
	@Value("${redis.pool.testOnReturn}")
	private boolean testOnReturn;
	@Value("${redis.pool.ip}")
	private String ip;
	@Value("${redis.pool.port}")
	private int port;
	@Value("${redis.pool.pwd}")
	private String pwd;
	@Value("${redis.pool.timeout}")
	private int timeout;
}
