package cn.ssm.shiro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.CollectionUtils;
import org.crazycake.shiro.IRedisManager;
import org.crazycake.shiro.ObjectSerializer;
import org.crazycake.shiro.RedisCache;
import org.crazycake.shiro.RedisSerializer;
import org.crazycake.shiro.SerializationException;
import org.crazycake.shiro.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 根据开源crazycake shiro+redis缓存插件的RedisCache进行重写，实现授权管理
 * 
 * @param <K>
 * @param <V>
 */
public class MyRedisCache<K, V> implements Cache<K, V> {

	private static Logger logger = LoggerFactory.getLogger(RedisCache.class);

	private RedisSerializer keySerializer = new StringSerializer();
	private RedisSerializer valueSerializer = new ObjectSerializer();
	private IRedisManager redisManager;
	private String keyPrefix = "";
	private int expire = 0;

	/**
	 * Construction
	 * 
	 * @param redisManager
	 */
	public MyRedisCache(IRedisManager redisManager, RedisSerializer keySerializer, RedisSerializer valueSerializer,
			String prefix, int expire) {
		if (redisManager == null) {
			throw new IllegalArgumentException("redisManager cannot be null.");
		}
		this.redisManager = redisManager;
		if (keySerializer == null) {
			throw new IllegalArgumentException("keySerializer cannot be null.");
		}
		this.keySerializer = keySerializer;
		if (valueSerializer == null) {
			throw new IllegalArgumentException("valueSerializer cannot be null.");
		}
		this.valueSerializer = valueSerializer;
		if (prefix != null && !"".equals(prefix)) {
			this.keyPrefix = prefix;
		}
		if (expire != -1) {
			this.expire = expire;
		}
	}

	@Override
	public V get(K key) throws CacheException {
		logger.debug("get key [" + key + "]");

		if (key == null) {
			return null;
		}

		try {
			Object redisCacheKey = getRedisCacheKey(key);
			byte[] rawValue = redisManager.get(keySerializer.serialize(redisCacheKey));
			if (rawValue == null) {
				return null;
			}
			V value = (V) valueSerializer.deserialize(rawValue);
			return value;
		} catch (SerializationException e) {
			throw new CacheException(e);
		}
	}

	@Override
	public V put(K key, V value) throws CacheException {
		logger.debug("put key [" + key + "]");
		if (key == null) {
			logger.warn("Saving a null key is meaningless, return value directly without call Redis.");
			return value;
		}
		try {
			Object redisCacheKey = getRedisCacheKey(key);
			redisManager.set(keySerializer.serialize(redisCacheKey),
					value != null ? valueSerializer.serialize(value) : null, expire);
			return value;
		} catch (SerializationException e) {
			throw new CacheException(e);
		}
	}

	@Override
	public V remove(K key) throws CacheException {
		logger.debug("remove key [" + key + "]");
		if (key == null) {
			return null;
		}
		try {
			Object redisCacheKey = getRedisCacheKey(key);
			byte[] rawValue = redisManager.get(keySerializer.serialize(redisCacheKey));
			V previous = (V) valueSerializer.deserialize(rawValue);
			redisManager.del(keySerializer.serialize(redisCacheKey));
			return previous;
		} catch (SerializationException e) {
			throw new CacheException(e);
		}
	}

	private Object getRedisCacheKey(K key) {
		if (key == null) {
			return null;
		}
		Object redisKey = key;
		if (keySerializer instanceof StringSerializer) {
			redisKey = getStringRedisKey(key);
		}
		if (redisKey instanceof String) {
			return this.keyPrefix + (String) redisKey;
		}
		return redisKey;
	}

	private Object getStringRedisKey(K key) {
		Object redisKey;
		if (key instanceof PrincipalCollection) {
			redisKey = getRedisKeyFromPrincipalCollection((PrincipalCollection) key);
		} else {
			redisKey = key.toString();
		}
		return redisKey;
	}

	private Object getRedisKeyFromPrincipalCollection(PrincipalCollection key) {
		Object redisKey = key.getPrimaryPrincipal();
		/**
		 * 开源组件写法
		 */
		// List<String> realmNames = getRealmNames(key);
		// Collections.sort(realmNames);
		// redisKey = joinRealmNames(realmNames);
		return redisKey;
	}

	private List<String> getRealmNames(PrincipalCollection key) {
		List<String> realmArr = new ArrayList<String>();
		Set<String> realmNames = key.getRealmNames();
		for (String realmName : realmNames) {
			realmArr.add(realmName);
		}
		return realmArr;
	}

	private Object joinRealmNames(List<String> realmArr) {
		Object redisKey;
		StringBuilder redisKeyBuilder = new StringBuilder();
		for (int i = 0; i < realmArr.size(); i++) {
			String s = realmArr.get(i);
			redisKeyBuilder.append(s);
		}
		redisKey = redisKeyBuilder.toString();
		return redisKey;
	}

	@Override
	public void clear() throws CacheException {
		logger.debug("clear cache");
		Set<byte[]> keys = null;
		try {
			keys = redisManager.keys(keySerializer.serialize(this.keyPrefix + "*"));
		} catch (SerializationException e) {
			logger.error("get keys error", e);
		}
		if (keys == null || keys.size() == 0) {
			return;
		}
		for (byte[] key : keys) {
			redisManager.del(key);
		}
	}

	@Override
	public int size() {
		Long longSize = new Long(redisManager.dbSize());
		return longSize.intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<K> keys() {
		Set<byte[]> keys = null;
		try {
			keys = redisManager.keys(keySerializer.serialize(this.keyPrefix + "*"));
		} catch (SerializationException e) {
			logger.error("get keys error", e);
			return Collections.emptySet();
		}

		if (CollectionUtils.isEmpty(keys)) {
			return Collections.emptySet();
		}

		Set<K> convertedKeys = new HashSet<K>();
		for (byte[] key : keys) {
			try {
				convertedKeys.add((K) keySerializer.deserialize(key));
			} catch (SerializationException e) {
				logger.error("deserialize keys error", e);
			}
		}
		return convertedKeys;
	}

	@Override
	public Collection<V> values() {
		Set<byte[]> keys = null;
		try {
			keys = redisManager.keys(keySerializer.serialize(this.keyPrefix + "*"));
		} catch (SerializationException e) {
			logger.error("get values error", e);
			return Collections.emptySet();
		}

		if (CollectionUtils.isEmpty(keys)) {
			return Collections.emptySet();
		}

		List<V> values = new ArrayList<V>(keys.size());
		for (byte[] key : keys) {
			V value = null;
			try {
				value = (V) valueSerializer.deserialize(redisManager.get(key));
			} catch (SerializationException e) {
				logger.error("deserialize values= error", e);
			}
			if (value != null) {
				values.add(value);
			}
		}
		return Collections.unmodifiableList(values);
	}

	public String getKeyPrefix() {
		return keyPrefix;
	}

	public void setKeyPrefix(String keyPrefix) {
		this.keyPrefix = keyPrefix;
	}

}
