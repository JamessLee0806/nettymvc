package com.netty.mvc.sps.cfg.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {
	@Autowired
	RedisProperty property;

	@Bean
	public JedisPool redisPool() {
		JedisPool pool = null;
		JedisPoolConfig poolCfg = new JedisPoolConfig();
		poolCfg.setMaxTotal(property.getMaxActive());
		poolCfg.setMaxIdle(property.getMaxIdle());
		poolCfg.setMaxWaitMillis(property.getMaxWait());
		poolCfg.setTestOnBorrow(property.isTestOnBorrow());
		poolCfg.setTestOnReturn(property.isTestOnReturn());
		System.out.println("ip="+property.getIp());
		if (StringUtils.isEmpty(property.getPwd())) {
			pool = new JedisPool(poolCfg, property.getIp(), property.getPort());
		} else {
			pool = new JedisPool(poolCfg, property.getIp(), property.getPort(), property.getTimeout(),
					property.getPwd());
		}
		return pool;
	}

	@Bean
	@Scope("prototype")
	public Jedis redisClient(@Autowired JedisPool pool) {
		if (pool == null) {
			throw new RuntimeException("Redis Pool is null...");
		}
		return pool.getResource();
	}

}
