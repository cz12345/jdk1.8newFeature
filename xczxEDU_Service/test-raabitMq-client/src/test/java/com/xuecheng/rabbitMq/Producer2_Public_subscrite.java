package com.xuecheng.rabbitMq;

/*
* 发布订阅模式：
* 由生产者，交换机，队列，和消费者组成
* 如果有多个消费者监听同一个队列，那么会采用轮询机制来消费消息（这次是A接受消息，BC，不接受，下次B接受，AC不接受，以此类推）
*
* 、生产者将消息发给broker，由交换机将消息转发到绑定此交换机的每个队列，每个绑定交换机的队列都将接收
到消息，监听在指定队列上的消费者将接受到消息

 注意注意注意： 声明队列实际开发中是放在消费者声明的，之所以在生产者声明，是为了测试，包括队列和交换机绑定，
              声明交换机开发中是放在生产者的，之所以消费者会再次声明交换机也是为了测试需要

* */

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer2_Public_subscrite {
    private static final String QUEUE_INFORM_EMAIL = "email";
    private static final String QUEUE_INFORM_MSG = "msg";
    private static final String EXCHANGE_INFORM = "exchange_fanout_inform";



    public static void main(String[] args) {

        Connection connection = null;
        Channel channel = null;
        //创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设置连接参数 （这里的rabbitMq是安装在阿里云服务器的docker上的）
        connectionFactory.setHost("120.79.229.60");
        connectionFactory.setPort(5672); //因为docker安装的rabbitMq时的映射地址是8082 => 5672
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setVirtualHost("my_vhost");

        //创建一个连接
        try {
            connection = connectionFactory.newConnection();
            //创建与exchange连接的通道，一个连接可以创建多个通道，每个通道相当于一个会话，之间是相互独立，
            channel = connection.createChannel();
            //声明队列：声明两个队列：一个是邮箱队列，一个是信息队列
            channel.queueDeclare(QUEUE_INFORM_EMAIL, true, false, false, null);
            channel.queueDeclare(QUEUE_INFORM_MSG, true, false, false, null);
            //声明交换机
            /*
                参数明细：
                参数一：交换机名称
                参数二：交换机类型 :{
                     BuiltinExchangeType.FANOUT:对应的是发布订阅模式
                     BuiltinExchangeType.DIRECT:路由Routing模式
                     BuiltinExchangeType.TOPIC:topic模式
                     BuiltinExchangeType.HEADERS:head模式
                }

            *
            * */
            channel.exchangeDeclare(EXCHANGE_INFORM,BuiltinExchangeType.FANOUT);

            //将队列和交换机进行绑定，将两个队列和交换机绑定，交换机知道该
            /*
            参数明细：
             参数一：绑定哪个队列，队列名称
             参数二：交换机名称
             参数三：路由key，这里设置为空""，路由key的作用，根据key将消息发送到指定的队列,路由模式一般会用到该参数
            *
            * */
            channel.queueBind(QUEUE_INFORM_EMAIL,EXCHANGE_INFORM,"");
            channel.queueBind(QUEUE_INFORM_MSG,EXCHANGE_INFORM,"");


            //发送邮件信息(发五次)
            for (int i = 0; i < 5; i++) {
                String mesage ="这是发布订阅模式，消息过来了啊";
                /*
                * 参数明细
                *  消息将会发送给交换机，然后交换机会自动转发给绑定次此交换机上的所有队列（因为参数路由key为""，如果有值，那么
                *  会根据路由key转发给指定的队列，否则转给绑定在此交换机上的所有队列）
                * */
                channel.basicPublish(EXCHANGE_INFORM,"",null,mesage.getBytes());

                System.out.println("发布订阅模式生成者发布消息:"+mesage);
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }

            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
