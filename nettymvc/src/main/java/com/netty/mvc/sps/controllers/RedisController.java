package com.netty.mvc.sps.controllers;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import redis.clients.jedis.Jedis;

@Controller
@RequestMapping("redis")
public class RedisController {
	@Autowired
	@Qualifier("redisClient")
	private Jedis client;

	@RequestMapping("addStr/{key}/{value}")
	@ResponseBody
	public String addStr(@PathVariable("key") String key, @PathVariable("value") String value) {
		return client.set(key, value);
	}

	@RequestMapping("getStr/{key}")
	@ResponseBody
	public String getStr(@PathVariable("key") String key) {
		return client.get(key);
	}
	
	@RequestMapping("keys/{key}")
	@ResponseBody
	public String keys(@PathVariable("key")String key){
		Set<String> list=client.keys(key);
		return JSON.toJSONString(list);
		
	}
}
