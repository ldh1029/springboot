package com.xhcoding;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by xin on 2018/1/12.
 */
@Component
public class Sender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(){
        String context = "hello" + new Date();
        System.out.println("sender:" + context);
        rabbitTemplate.convertAndSend("hello",context);
    }
}
