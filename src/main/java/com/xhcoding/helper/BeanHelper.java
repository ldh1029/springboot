package com.xhcoding.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Zeke on 2017/5/27.
 */
@Slf4j
public class BeanHelper {

    public static void copyParamter(Object source,Object target,boolean replace){
        copyParamter(source, target, null,replace);
    }

    /**
     * @param source
     * @param target
     * @param excludeSet
     * @param replace true 覆盖原有参数  false原参数有值不覆盖
     */
    public static void copyParamter(Object source, Object target, Set<String> excludeSet, boolean replace) {
        if (ObjectUtils.isEmpty(source) || ObjectUtils.isEmpty(target) || source.getClass() != target.getClass()) {
            return;
        }

        if (CollectionUtils.isEmpty(excludeSet)){
            excludeSet = new HashSet<>();
        }

        try {
            Class clazz = source.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {

                String paramName = field.getName();
                if (!excludeSet.contains(paramName)) {
                    field.setAccessible(true);//获取私有属性值时  需要设置为true

                    Object sourceVal = field.get(source);
                    if (!ObjectUtils.isEmpty(sourceVal)) {

                        Object targetVal = field.get(target);
                        if (replace) {
                            field.set(target, sourceVal);
                        } else {
                            if (ObjectUtils.isEmpty(targetVal)) {
                                field.set(target, sourceVal);
                            }
                        }
                    }
                }
            }
        } catch (Throwable e) {
            log.error("BeanHelper error:", e);
        }
    }
}
