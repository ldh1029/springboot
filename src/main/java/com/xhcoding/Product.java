package com.xhcoding;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

/**
 * Created by xin on 2017/12/4.
 */
public class Product {
    public static void main(String[] argv) throws java.io.IOException, Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUsername("xhcoding");
        connectionFactory.setPassword("123456");
        connectionFactory.setHost("69.171.71.191");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        String exchange = "sms_exchange";
        channel.exchangeDeclare(exchange, "direct",true);

        String routingKey  = "rabbit";
        byte[] bytes = "test rabbitmq".getBytes();
        channel.basicPublish(exchange,routingKey,null,bytes);
        channel.close();
        connection.close();
    }
}
