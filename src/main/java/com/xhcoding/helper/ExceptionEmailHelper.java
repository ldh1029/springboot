package com.xhcoding.helper;


import com.xhcoding.rabbitmq.MqConfig;
import com.xhcoding.rabbitmq.MqHelper;
import com.xhcoding.rabbitmq.MqQueueEnum;
import com.xhcoding.rabbitmq.MqTagEnum;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;

/**
 * 异常短信发送
 */
@Component
@Slf4j
public class ExceptionEmailHelper {

    @Autowired
    MqHelper mqHelper;

    @Value("${gofobao.error-email}")
    String errorEmail; //发送警报邮件

    static ArrayList<String> emailAddress = null;

    /**
     * 获取邮件地址
     *
     * @return
     */
    private ArrayList<String> getEmailAddress() {
        if (!ObjectUtils.isEmpty(emailAddress)) {
            return emailAddress;
        }

        if (StringUtils.isEmpty(errorEmail)) {
            return emailAddress;
        }

        String[] split = errorEmail.split(",");
        emailAddress = new ArrayList<>(split.length);

        for (String item : split) {
            if (!StringUtils.isEmpty(item)) {
                emailAddress.add(item);
            }
        }

        return emailAddress;
    }

    /**
     * 发送异常信息
     *
     * @param subject 标题
     * @param e       异常信息
     */
    public void sendException(String subject, Exception e) {
        try {
            ArrayList<String> emailAddress = getEmailAddress();
            if (ObjectUtils.isEmpty(emailAddress)) {
                log.error("未配置警报邮件地址");
                return;
            }
            for (String email : emailAddress) {
                MqConfig config = new MqConfig();
                config.setQueue(MqQueueEnum.RABBITMQ_EMAIL);
                config.setTag(MqTagEnum.EXCEPTION_EMAIL);
                ImmutableMap<String, String> body = ImmutableMap
                        .of(MqConfig.EMAIL, email,
                                MqConfig.IP, "127.0.0.1",
                                "subject", subject,
                                "content", ExceptionUtils.getStackTrace(e));
                config.setMsg(body);
                mqHelper.convertAndSend(config);
            }

        } catch (Exception ex) {
            log.error("发送异常信息", ex);
        }
    }

    /**
     * 发送异常信息
     *
     * @param msg
     */
    public void sendErrorMessage(String subject, String msg) {
        try {
            ArrayList<String> emailAddress = getEmailAddress();
            if (ObjectUtils.isEmpty(emailAddress)) {
                log.error("未配置警报邮件地址");
                return;
            }
            for (String email : emailAddress) {
                MqConfig config = new MqConfig();
                config.setQueue(MqQueueEnum.RABBITMQ_EMAIL);
                config.setTag(MqTagEnum.EXCEPTION_EMAIL);
                ImmutableMap<String, String> body = ImmutableMap
                        .of(MqConfig.EMAIL, email,
                                MqConfig.IP, "127.0.0.1",
                                "subject", subject,
                                "content", msg);
                config.setMsg(body);
                mqHelper.convertAndSend(config);
            }
        } catch (Exception ex) {
            log.error("发送异常信息", ex);
        }
    }
}
