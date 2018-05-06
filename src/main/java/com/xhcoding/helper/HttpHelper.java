package com.xhcoding.helper;

import com.google.common.base.Preconditions;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Max on 17/5/31.
 */
public class HttpHelper {

    /**
     * 获取请求链接
     *
     * @param request 请求类
     * @return 链接
     */
    public static String getUrl(HttpServletRequest request) {
        Preconditions.checkNotNull(request,
                "HttpHelper getUrl: request is null");
        return request.getRequestURL().toString();
    }

    /**
     * 获取请求参数
     *
     * @param request
     * @return
     */
    public static String getRequestParameter(HttpServletRequest request) {
        StringBuffer params = new StringBuffer();
        if (ObjectUtils.isEmpty(request)) {
            return "";
        }
        Map map = new HashMap();
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();

            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length == 1) {
                String paramValue = paramValues[0];
                if (paramValue.length() != 0) {
                    map.put(paramName, paramValue);
                }
            }
        }

        Set<Map.Entry<String, String>> set = map.entrySet();
        for (Map.Entry entry : set) {
            params.append(entry.getKey()).append("->").append(entry.getValue());
        }
        return params.toString();
    }
}
