package com.xuecheng.rabbitMq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/*
* 消费者 发布订阅模式
* */
public class Consumer2_Public_subscrite_Msg {
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

        try {
            connection = connectionFactory.newConnection();
            //消费者也要声明队列 ，表示消费者监听在哪个队列上
            channel = connection.createChannel();
             /*
              声明队列，如果Rabbit中没有此队列将自动创建
            * param1:队列名称
            * param2:是否持久化,true代表持久化，这样即使连接关闭这么rabbitMq重启，队列中的消息还是存在的
            * param3:队列是否独占此连接
            * param4:队列不再使用时是否自动删除此队列，也是和持久化搭配使用的，队列删了，消息也就没了，那么不存在持久化
            * param5:队列参数
            * */
            channel.queueDeclare(QUEUE_INFORM_MSG, true, false, false, null);

            /*声明交换机*/
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

            /*
            * 将交换机和队列绑定
            * */
            channel.queueBind(QUEUE_INFORM_MSG,EXCHANGE_INFORM,"");

            /*
             *定义消费方法*/

            DefaultConsumer consumer = new DefaultConsumer(channel) {
                /*
                * 消费者接收消息调用此方法
                * @param consumerTag 消费者的标签，在channel.basicConsume()去指定
                * @param envelope 消息包的内容，可从中获取消息id，消息routingkey，交换机，消息和重传标志
                (收到消息失败后是否需要重新发送)
                * @param properties
                * @param body 消息内容
                * */
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

            /*
            * 监听队列
            *1、队列名称
            * 2、是否自动回复，设置为true为表示消息接收到自动向mq回复接收到了，mq接收到回复会删除消息，设置
                为false则需要手动回复
            * 3、消费消息的方法，消费者接收到消息后调用此方法
            * */

            channel.basicConsume(QUEUE_INFORM_MSG, true, consumer);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }




    }
}
