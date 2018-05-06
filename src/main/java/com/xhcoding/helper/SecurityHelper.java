package com.xhcoding.helper;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * Created by Zeke on 2017/6/19.
 */
@Slf4j
public class SecurityHelper {
    private final static String requestPrefix = "gfb_req_";

    public static boolean checkSign(String sign, String paramStr) {
        if (StringUtils.isEmpty(sign) || StringUtils.isEmpty(paramStr)) {
            log.error("SecurityHelper detail：缺少必填请求参数！");
            return false;
        }
        return sign.equals(getSign(paramStr));
    }

    public static String getSign(String paramStr) {
        return DigestUtils.md5Hex((requestPrefix + paramStr).getBytes());
    }

    /**
     * 加签
     *
     * @param data
     * @return
     */
    public static MultiValueMap<String, String> doSign(Map<String, String> data) {
        Gson gson = new Gson();
        String dataStr = gson.toJson(data);
        String sign = DigestUtils.md5Hex((requestPrefix + dataStr).getBytes());
        MultiValueMap<String, String> result = new LinkedMultiValueMap<String, String>(2);
        result.add("sign", sign);
        result.add("paramStr", dataStr);
        return result;
    }
}
