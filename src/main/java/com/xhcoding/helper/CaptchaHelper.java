package com.xhcoding.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 图形验证码帮助类
 * Created by Max on 17/5/17.
 */
@Component
@Slf4j
public class CaptchaHelper {

    public static final String REDIS_CAPTCHA_PREFIX_KEY = "captcha_";

    @Autowired
    RedisHelper redisHelper;


    /**
     * 判断验证码是否正确（验证函数后都会删除图形验证码）
     *
     * @param key     redis key
     * @param captcha 验证码
     * @return boolean
     */
    public boolean match(String key, String captcha) {
        try {
            String redisKey = String.format("%s%s", REDIS_CAPTCHA_PREFIX_KEY, key);

            String redisCaptcha = redisHelper.get(redisKey, null);
            redisHelper.remove(redisKey);
            if (Objects.isNull(redisCaptcha)) {
                return false;
            }

            return captcha.equalsIgnoreCase(redisCaptcha);
        } catch (Throwable e) {
            log.error("CaptchaHelper match exception", e);
        }

        return false;
    }

    /**
     * 添加验证码
     *
     * @param key     redis key
     * @param captcha 验证码
     * @return boolean
     */
    public boolean add(String key, String captcha) {
        try {
            redisHelper.put(String.format("%s%s", REDIS_CAPTCHA_PREFIX_KEY, key), captcha, 15 * 60);
            return true;
        } catch (Throwable e) {
            log.error("CaptchaHelper add exception", e);
        }
        return false;
    }

}
