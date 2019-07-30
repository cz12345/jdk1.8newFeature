package com.xuecheng.rabbitMq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/*简单rabbitMq消费者入门
* 注意注意注意： 生命队列实际开发中是放在消费者声明的，之所以在生产者声明，是为了测试
* */
public class Producer1 {

    private static final String QUEUE = "helloword";

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
        connectionFactory.setVirtualHost("my_vhost");//rabbitMq的默认虚拟机名称是"/"，每个VirtualHost相当于一个相对独立的RabbitMQ服务器；
        // 每个VirtualHost之间是相互隔离的，exchange、queue、message不能互通。
        // 拿数据库（用MySQL）来类比：RabbitMq相当于MySQL，RabbitMq中的VirtualHost就相当于MySQL中的一个库。
        //注意因为我docker启动RabbitMq时指定了虚拟机名称：my_vhost
        //docker run -d --name rabbitmq3.7.7 -p 5672:5672 -p 8082:15672 -v `pwd`/data:/var/lib/rabbitmq --hostname myRabbit -e RABBITMQ_DEFAULT_VHOST=my_vhost  -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=admin d69a5113ceae

        try {
            //创建一个连接
            connection = connectionFactory.newConnection();
            //创建与exchange连接的通道，一个连接可以创建多个通道，每个通道相当于一个会话，之间是相互独立，
            channel = connection.createChannel();
            /*
              声明队列，如果Rabbit中没有此队列将自动创建
            * param1:队列名称
            * param2:是否持久化,true代表持久化，这样即使连接关闭这么rabbitMq重启，队列中的消息还是存在的
            * param3:队列是否独占此连接
            * param4:队列不再使用时是否自动删除此队列，也是和持久化搭配使用的，队列删了，消息也就没了，那么不存在持久化
            * param5:队列参数
            * */
            channel.queueDeclare(QUEUE, true, false, false, null);
            //定义一个消息
            String message = "张雨薇的洞察力很强势" + System.currentTimeMillis();
            /*
             * 消息发布方法
             * param1:Exchange的名称，如果没有指定，则使用Default Exchange,这里没有指定交换机(默认就填一个"")，消息将发送给默认交换机，
             * 每个队列也会绑定那个默认的交换机，但是不能显示绑定或解除绑定
             * param2:routingKey,消息的路由Key，是用于Exchange（交换机）将消息转发到指定的消息队列,routeKey一般和队列名称一致，否则会有问题
             * param3:消息包含的属性
             * param4:消息体
             * */
            channel.basicPublish("", QUEUE, null, message.getBytes());
            System.out.println("Send Message is:'" + message + "'");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            //先关闭通道
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
            //再关闭连接s
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }
}
