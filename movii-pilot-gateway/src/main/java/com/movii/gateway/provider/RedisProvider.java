package com.movii.gateway.provider;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisProvider {

	private StringRedisTemplate template;

	public RedisProvider(StringRedisTemplate template) {
		this.template = template;
	}
	
	public String getBy(String key) {
		return template.opsForValue().get(key);
	}
}
