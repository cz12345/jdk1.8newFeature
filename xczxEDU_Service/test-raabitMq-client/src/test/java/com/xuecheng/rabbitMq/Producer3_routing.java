package com.xuecheng.rabbitMq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/*
 *路由模式：
 *
 * 有生产者将消息发送给交换机，由交换机根据routing-key将消息发送给指定的队列，注意跟发布订阅不一样，路由队列也需要和交换机
 * 进行绑定，但需要设置routing-key，这样发送消息时，指名roting-key，然后将根据routing-key发送给指定队列，二发布订阅模式
 * 是不需要routin-key，交换机会发送给绑定此交换机的所有队列
 * 发送消息时，会指定路由ke发送的，那么绑定了路由key的队列都将会收到消息
 * 这里场景：
 * 一个短信 一个邮箱。
 * 消费者：监听在指定队列，将会接受到消息（如果这个队列绑定了多个路由key，那么消费者都会接受到来自不同路由key的消息）。
 *
 *  注意注意注意： 声明队列实际开发中是放在消费者声明的，之所以在生产者声明，是为了测试，包括队列和交换机绑定也是在消费者做的。              声明交换机开发中是放在生产者的，之所以消费者会再次声明交换机也是为了测试需要
 *
 * */
public class Producer3_routing {
    private static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    private static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    private static final String EXCHANGE_ROUTING_INFORM = "exchange_routing_inform";


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


        try {
            connection = connectionFactory.newConnection();
            channel = connection.createChannel();
            //声明交换机
            channel.exchangeDeclare(EXCHANGE_ROUTING_INFORM, BuiltinExchangeType.DIRECT);
            //声明队列
            channel.queueDeclare(QUEUE_INFORM_EMAIL, true, false, false, null);
            channel.queueDeclare(QUEUE_INFORM_SMS, true, false, false, null);

            //交换机和队列进行绑定
            /*
             参数明细：
             参数一
             参数二
             参数三：路由key，这里跟队列名称一致
            * */
            channel.queueBind(QUEUE_INFORM_EMAIL, EXCHANGE_ROUTING_INFORM, QUEUE_INFORM_EMAIL);
            channel.queueBind(QUEUE_INFORM_SMS, EXCHANGE_ROUTING_INFORM, QUEUE_INFORM_SMS);
            channel.queueBind(QUEUE_INFORM_SMS, EXCHANGE_ROUTING_INFORM, "error");
            channel.queueBind(QUEUE_INFORM_SMS, EXCHANGE_ROUTING_INFORM, "waring");


            //发送消息
            //1发送邮件消息
            for (int i = 0; i < 5; i++) {
                String message = "send message to user by email" + i;
                /*
                 * 参数一：交换机名称
                 * 参数二: route-key
                 * 参数三：消息属性
                 * 参数四：消息内容
                 * */

                channel.basicPublish(EXCHANGE_ROUTING_INFORM, QUEUE_INFORM_EMAIL, null, message.getBytes());
                System.out.println("发送邮件消息:" + message);
            }
            //2 发送短信消息
            //            for (int i = 0; i < 5; i++) {
            //                String message = "send message to user by email" + i;
            //                /*
            //                 * 参数一：交换机名称
            //                 * 参数二: route-key
            //                 * 参数三：消息属性
            //                 * 参数四：消息内容
            //                 * */
            //
            //                channel.basicPublish(EXCHANGE_ROUTING_INFORM, QUEUE_INFORM_SMS, null, message.getBytes());
            //                System.out.println("发送邮件消息:" + message);
            //            }

            //给同一个队列绑定不同的roteing-key，然后发送发送消息时，监听在此队列上的消费者将接受到不同route-key的消息
            // error，waring
            /*
             * */
            //            给信息队列发一个错误信息(路由key为error)
            //            channel.basicPublish(EXCHANGE_ROUTING_INFORM, "error", null, "这是一条错误信息".getBytes());
            //            再给信息队列发一个警告信息（路由key为waring）
            //            channel.basicPublish(EXCHANGE_ROUTING_INFORM, "waring", null, "这是一条错误信息".getBytes());
            //            这样只有监听在指定路由的队列，才会接收到消息


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
