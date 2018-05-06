package com.xhcoding.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * redis 工具类
 * Created by Max on 17/2/15.
 */
@Component
public class RedisHelper {
    /**
     * 失效时间默认：3600秒
     */
    public static final int DEFAULT_EXPIRE_TIME = 60 * 60;


    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    public RedisHelper(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 向Redis中插入String类型数据(默认失效时间：3600秒)；
     *
     * @param key    主键
     * @param value  值
     * @param expire 过期时间 默认时间：3600秒
     * @throws Exception
     */
    public void put(String key, String value, Integer expire) throws Exception {
        if ((StringUtils.isEmpty(key)) || (StringUtils.isEmpty(value))) {
            throw new Exception("key/value 为空");
        }

        if (expire == null) {
            expire = DEFAULT_EXPIRE_TIME;
        }
        try {
            stringRedisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
        } catch (Throwable e) {
            throw e;
        }
    }

    /**
     * 向Redis中插入String类型数据(永久缓存， 需要手动删除)
     *
     * @param key   主键
     * @param value 值
     * @throws Exception
     */
    public void put(String key, String value) throws Exception {
        if ((StringUtils.isEmpty(key)) || (StringUtils.isEmpty(value))) {
            throw new Exception("key/value 为空");
        }

        try {
            stringRedisTemplate.opsForValue().set(key, value);
        } catch (Throwable e) {
            throw e;
        }
    }


    /**
     * 从缓存中获取数据
     *
     * @param key          主键
     * @param defaultValue 默认返回值
     * @throws Exception
     */
    public String get(String key, String defaultValue) throws Exception {
        if (StringUtils.isEmpty(key)) {
            throw new Exception("key 为空");
        }

        try {
            String value = stringRedisTemplate.opsForValue().get(key);
            return value == null ? defaultValue : value.toString();
        } catch (Throwable e) {
            throw e;
        }
    }


    /**
     * 根据key删除缓存中的数据
     *
     * @param key 主键
     * @throws Exception
     */
    public void remove(String key) throws Exception {
        if (StringUtils.isEmpty(key)) {
            throw new Exception("key 为空");
        }

        try {
            stringRedisTemplate.delete(key);
        } catch (Throwable e) {
            throw e;
        }
    }


    /**
     * 判断key是否存在
     *
     * @param key 待验证key
     * @return true：存在， false：不存在
     * @throws Exception
     */
    public boolean hasKey(String key) throws Exception {
        if (StringUtils.isEmpty(key)) {
            throw new Exception("key 为空");
        }

        try {
            return stringRedisTemplate.hasKey(key);
        } catch (Throwable e) {
            throw e;
        }
    }


    /**
     * 如果redis中不存在该key则存入对应的key,value，设置expire
     *
     * @param key    key
     * @param value  value
     * @param expire 保存时间，单位为毫秒
     * @return       是否存入给定的key/value
     */
    public synchronized boolean putNX(String key, String value, long expire) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
            throw new IllegalArgumentException("Key/Value must not be null");
        }
        try {
            boolean flag;
            flag = stringRedisTemplate.opsForValue().setIfAbsent(key, value);
            if (flag) {
                stringRedisTemplate.opsForValue().set(key, value, expire, TimeUnit.MILLISECONDS);
            }
            return flag;
        } catch (Exception e) {
            throw e;
        }
    }

}
