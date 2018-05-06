package com.xhcoding;



import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * Created by xin on 2017/12/4.
 */
public class Consumer {

    public static void main(String[] argv) throws Exception {
       ConnectionFactory connectionFactory = new ConnectionFactory();
       connectionFactory.setUsername("xhcoding");
       connectionFactory.setPassword("123456");
       connectionFactory.setHost("69.171.71.191");
       Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        String exchange = "sms_exchange";
        channel.exchangeDeclare(exchange,"direct",true);
        //声明队列
        String queueName = channel.queueDeclare().getQueue();
        String routingKey = "rabbit";
        channel.queueBind(queueName,exchange,routingKey);

        while (true) {
             boolean autoAck = false;
             String comsumerTag = "";
             channel.basicConsume(queueName,autoAck,new DefaultConsumer(channel){
                 @Override
                 public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                     String routingKey = envelope.getRoutingKey();
                     String contentType = properties.getContentType();
                     System.out.println("消费的路由键:" + routingKey);
                     System.out.println("消费的内容类型" + contentType);
                     long deliveryTag = envelope.getDeliveryTag();
                     channel.basicAck(deliveryTag,false);
                     System.out.println("消费的消息体");
                     String bodyStr = new String(body, "UTF-8");
                     System.out.println(bodyStr);
                 }
             });
        }
    }
}
