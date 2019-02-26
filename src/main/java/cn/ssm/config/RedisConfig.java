package cn.ssm.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis配置类 没有Redis，不使用
 **/
// @Configuration
// @EnableCaching // 开启注解
public class RedisConfig {

	@Value("${cache.default.expire-time:1800}")
	private int defaultExpireTime;
	@Value("${cache.test.expire-time:180}")
	private int testExpireTime;
	@Value("${cache.test.name:test}")
	private String testCacheName;

	// 缓存管理器
	@Bean
	public CacheManager cacheManager(RedisConnectionFactory lettuceConnectionFactory) {
		RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig();
		// 设置缓存管理器管理的缓存的默认过期时间
		defaultCacheConfig = defaultCacheConfig.entryTtl(Duration.ofSeconds(defaultExpireTime))
				// 设置 key为string序列化
				.serializeKeysWith(
						RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
				// 设置value为json序列化
				.serializeValuesWith(RedisSerializationContext.SerializationPair
						.fromSerializer(new GenericJackson2JsonRedisSerializer()))
				// 不缓存空值
				.disableCachingNullValues();

		Set<String> cacheNames = new HashSet<>();
		cacheNames.add(testCacheName);

		// 对每个缓存空间应用不同的配置
		Map<String, RedisCacheConfiguration> configMap = new HashMap<>();
		configMap.put(testCacheName, defaultCacheConfig.entryTtl(Duration.ofSeconds(testExpireTime)));
		configMap.put("message", defaultCacheConfig.entryTtl(Duration.ofSeconds(24 * 60 * 60)));// 消息缓存，一天

		RedisCacheManager cacheManager = RedisCacheManager.builder(lettuceConnectionFactory)
				.cacheDefaults(defaultCacheConfig).initialCacheNames(cacheNames)
				.withInitialCacheConfigurations(configMap).build();
		return cacheManager;
	}

	@Bean(name = "redisTemplate")
	public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);

		StringRedisSerializer keySerializer = new StringRedisSerializer();
		GenericJackson2JsonRedisSerializer valueSerializer = new GenericJackson2JsonRedisSerializer();
		redisTemplate.setKeySerializer(keySerializer);
		redisTemplate.setHashKeySerializer(keySerializer);
		redisTemplate.setValueSerializer(valueSerializer);
		redisTemplate.setHashValueSerializer(valueSerializer);

		return redisTemplate;
	}

	@Bean
	public KeyGenerator keyGenerator() {
		return (o, method, objects) -> {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(o.getClass().getSimpleName());
			stringBuilder.append(".");
			stringBuilder.append(method.getName());
			stringBuilder.append("[");
			for (Object obj : objects) {
				if (obj != null) {
					stringBuilder.append(obj.toString());
				}
			}
			stringBuilder.append("]");

			return stringBuilder.toString();
		};
	}

}