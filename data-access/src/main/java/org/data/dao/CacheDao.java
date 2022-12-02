package org.data.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.json.Path;

@Repository
public class CacheDao<T> {

    @Autowired
    private JedisPooled jedisClient;

    public String save(String key, T t) {
        return save(key, "$", t);
    }

    public String save(String key, String path, T t) {
        return jedisClient.jsonSet(key, new Path(path), t);
    }

    public T get(String key, Class<T> clazz) {
        return jedisClient.jsonGet(key, clazz);
    }

    public Long expire(String key, long seconds) {
        return jedisClient.expire(key, seconds);
    }

    public String set(String key, String value) {
        return jedisClient.set(key, value);
    }

    public String setEx(String key, long seconds, String value) {
        return jedisClient.setex(key, seconds, value);
    }

    public String get(String key) {
        return jedisClient.get(key);
    }
}
