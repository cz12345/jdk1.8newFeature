package com.xuecheng.cms.com.xuecheng.manage_cms_client.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    //定义队列Bean的名称
    private static final String QUEUE_CMS = "queue_cms";
    //定义交换机bean的名称
    private static final String EXCAHNGE_CMS = "exchange_direct_cms";
    //从配置文件注入队列名称
    @Value("${xuecheng.mq.queue}")
    private String queue_cms;
    @Value("${xuecheng.mq.routingKey}")
    private String routingKey;

    //声明交换机
    @Bean(EXCAHNGE_CMS)
    public Exchange EXCHANGE_TOPIC_INFORM() {
        return ExchangeBuilder.directExchange(EXCAHNGE_CMS).durable(true).build();
    }

    //声明队列
    @Bean(QUEUE_CMS)
    public Queue QUEUE_CMS() {
        return new Queue(queue_cms);
    }

    /*将队列和交换机绑定
    * 并制定路由key
    * */
    @Bean
    public Binding BINDING_QUEUE_INFORM_SMS(@Qualifier(EXCAHNGE_CMS) Exchange exchange,
                                            @Qualifier(QUEUE_CMS) Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey).noargs();
    }

}
