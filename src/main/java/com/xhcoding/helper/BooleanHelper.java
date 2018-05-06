package com.xhcoding.helper;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.util.ObjectUtils;

/**
 * Created by Zeke on 2017/7/27.
 */
public class BooleanHelper {
    public static boolean isTrue(Boolean bool) {
        return ObjectUtils.isEmpty(bool) ? false : BooleanUtils.isTrue(bool);
    }

    public static boolean isFalse(Boolean bool) {
        return ObjectUtils.isEmpty(bool) ? true : BooleanUtils.isFalse(bool);
    }
}
