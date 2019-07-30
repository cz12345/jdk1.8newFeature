package com.xuecheng.rabbitMq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/*
 *
 * 路由模式  消费者，将会监听消费指定路由的队列 ,监听路由是短信的队列
 * */
public class Consumer3_routing_Sms_error {
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
            //声明交换机（实际开发中是放在生产者声明，这里是为了测试）
            channel.exchangeDeclare(EXCHANGE_ROUTING_INFORM, BuiltinExchangeType.DIRECT);
            //声明队列
            channel.queueDeclare(QUEUE_INFORM_SMS, true, false, false, null);
            //交换机和队列绑定,指明route-key，监听指定路由的队列
            channel.queueBind(QUEUE_INFORM_SMS, EXCHANGE_ROUTING_INFORM, "error");


            //定义消费方法
            DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {

                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                           byte[] body) throws IOException {

                    System.out.println("###consumerTag:" + consumerTag);
                    System.out.println("###Exchange(交换机):" + envelope.getExchange());
                    System.out.println("###routingKey(路由key):" + envelope.getRoutingKey());
                    System.out.println("###getDeliveryTag(消息id):" + envelope.getDeliveryTag());
                    System.out.println("###消息体：" + new String(body, "utf-8"));

                }
            };

            //监听队列
            /**
             * 监听队列String queue, boolean autoAck,Consumer callback
             * 参数明细
             * 1、队列名称
             * 2、是否自动回复，设置为true为表示消息接收到自动向mq回复接收到了，mq接收到回复会删除消息，设置
             为false则需要手动回复
             * 3、消费消息的方法，消费者接收到消息后调用此方法
             */
            channel.basicConsume(QUEUE_INFORM_SMS, true, defaultConsumer);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }


    }


}
