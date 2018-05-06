package com.xhcoding.helper;

import com.xhcoding.entity.TopicsUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * Created by xin on 2017/11/28.
 */
@Component
public class RedisUtils {
    private static StringRedisTemplate stringRedisTemplate;

    private static RedisTemplate<String, TopicsUsers> redisTemplate;

    /**
     * 失效时间默认：3600秒
     */
    public static final int DEFAULT_EXPIRE_TIME = 60 * 60;

    //    @Autowired
//    public RedisUtils(StringRedisTemplate stringRedisTemplate) {
//        this.stringRedisTemplate = stringRedisTemplate;
//    }
    @Autowired
    public RedisUtils(RedisTemplate<String, TopicsUsers> template) {
        this.redisTemplate = template;
    }

    /**
     * redis获取缓存数据
     *
     * @param key
     * @return
     */
    public static String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 删除redis缓存数据
     *
     * @param key
     */
    public static void remove(String key) {
        stringRedisTemplate.delete(key);
    }

    /**
     * 向Redis中插入String类型数据(默认失效时间：3600秒)；
     *
     * @param key    主键
     * @param value  值
     * @param expire 过期时间 默认时间：3600秒
     * @throws Exception
     */
    public static void put(String key, String value, Integer expire) throws Exception {
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
    public static void put(String key, String value) throws Exception {
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
     * 判断key是否存在
     *
     * @param key 待验证key
     * @return true：存在， false：不存在
     * @throws Exception
     */
    public static boolean hasKey(String key) throws Exception {
        if (StringUtils.isEmpty(key)) {
            throw new Exception("key 为空");
        }

        try {
            return stringRedisTemplate.hasKey(key);
        } catch (Throwable e) {
            throw e;
        }
    }


}
