package com.xhcoding.config;


import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Created by xin on 2018/1/12.
 */
@Configuration
public class RabbitMq {
    @Bean
    public Queue queue(){
        return new Queue("hello");
    }
}
