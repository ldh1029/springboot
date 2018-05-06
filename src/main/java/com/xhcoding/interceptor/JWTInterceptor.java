package com.xhcoding.interceptor;

import com.xhcoding.contants.SecurityContants;
import com.xhcoding.exception.LoginException;
import com.xhcoding.helper.DateHelper;
import com.xhcoding.helper.HttpHelper;
import com.xhcoding.helper.IpHelper;
import com.xhcoding.helper.JWTHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by xin on 2018/1/24.
 */
@Slf4j
public class JWTInterceptor extends HandlerInterceptorAdapter {
    private JWTHelper jwtHelper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestSource = request.getHeader("requestSource");
        if (StringUtils.isEmpty(requestSource)) {
            log.error("当前用户非法请求 记录当前ip:" + IpHelper.getIpAddress(request) + ",当前时间:" + DateHelper.dateToString(new Date())
                    + "当前请求路径" + request.getRequestURI());
        }
        String requestURI = request.getRequestURI();
        if (ObjectUtils.isEmpty(jwtHelper)) {  // 初始化 jwtTokenHelper
            ApplicationContext ac = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
            jwtHelper = (JWTHelper) ac.getBean("jwtTokenHelper");
        }

        String token = jwtHelper.getToken(request);
        if (StringUtils.isEmpty(token)) {
            return false;
        }

        try {
            jwtHelper.validateSign(token);
        } catch (Exception e) {
            throw new LoginException(e.getMessage());
        }

        Long userId = jwtHelper.getUserIdFromToken(token);  // 用户ID
        request.setAttribute(SecurityContants.USERID_KEY, userId);
        try {
            String requestSourceStr = StringUtils.isEmpty(requestSource) ? "未知来源" : requestSource;
            log.info(String.format("当前请求地址：%s，来源: %s , 终端ip: %s ,参数：%s",
                    requestURI,
                    requestSourceStr,
                    request.getRemoteAddr(),
                    HttpHelper.getRequestParameter(request)));
        } catch (Exception e) {
        }
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }
}
