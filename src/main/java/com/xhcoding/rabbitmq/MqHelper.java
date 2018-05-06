package com.xhcoding.rabbitmq;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Max on 17/5/26.
 */
@Component
@Slf4j
public class MqHelper {
    @Autowired
    private RabbitTemplate rabbitTemplate ;


    private final static Gson GSON = new Gson() ;

    public boolean convertAndSend(MqConfig config) {
        Preconditions.checkNotNull(config.getQueue(), "ApollomqConfig queue is null");
        Preconditions.checkNotNull(config.getMsg(), "ApollomqConfig msg is null");
        Map<String, Object> body = new HashMap<>(2) ;
        Object msg = config.getMsg();
        body.put(MqConfig.MSG_BODY, msg);
        body.put(MqConfig.MSG_TAG, config.getTag().getValue()) ;
        String json = GSON.toJson(body);

        Long delayTime = 0L ;
        if(!ObjectUtils.isEmpty(config.getSendTime()) ){
            long sendTime = config.getSendTime().getTime();
            long nowTime = new Date().getTime();
            delayTime = sendTime - nowTime ;
            delayTime = delayTime <= 0 ? 0 : delayTime ;
        }

        int finalDelayTime = delayTime.intValue();
        MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setDelay(finalDelayTime);
                message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                return message;
            }
        };
        rabbitTemplate.convertAndSend( MqExchangeContants.DELAY_EXCHANGE, config.getQueue().getValue(), json, messagePostProcessor );
        return true ;
    }


}
