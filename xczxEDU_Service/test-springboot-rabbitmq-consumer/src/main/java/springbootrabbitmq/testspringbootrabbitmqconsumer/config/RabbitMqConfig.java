package springbootrabbitmq.testspringbootrabbitmqconsumer.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*配置RabbitMq生产者*/
@Configuration
public class RabbitMqConfig {
    public static final String QUEUE_INFORM_EAMIL = "queue_inform_email";
    public static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    public static final String EXCHANGE_TOPICS_INFORM = "exchange_topics_inform";

    public static final String ROUTINGKEY_EMAIL="inform.#.email.#";
    public static final String ROUTINGKEY_SMS="inform.#.sms.#";

    /*声明交换机
     * durable(true) 交换机持久化 ，消息队列重启或者关闭，交换机任然存在
     * topicExchange 是topic模
     * */
    @Bean(EXCHANGE_TOPICS_INFORM)
    public Exchange EXCHANGE_TOPICS_INFORM() {
        return ExchangeBuilder.topicExchange(EXCHANGE_TOPICS_INFORM).durable(true).build();
    }

    /*
     *声明email队列
     * */
    @Bean(QUEUE_INFORM_EAMIL)
    public Queue QUEUE_INFORM_EAMIL() {
        return new Queue(QUEUE_INFORM_EAMIL);
    }

    /*声明sms队列*/
    @Bean(QUEUE_INFORM_SMS)
    public Queue QUEUE_INFORM_SMS() {
        return new Queue(QUEUE_INFORM_SMS);
    }



    /*将eamil队列绑定到交换机指定route-key
    * 使用@Qualifier将指定名称的bean
    * */
    @Bean
    public Binding BINDING_QUEUE_INFORM_EMAIL(@Qualifier(QUEUE_INFORM_EAMIL) Queue eamil,
                                              @Qualifier(EXCHANGE_TOPICS_INFORM) Exchange exchange) {
        return BindingBuilder.bind(eamil).to(exchange).with(ROUTINGKEY_EMAIL).noargs();
    }


    @Bean
    /*将Sms队列绑定到交换机指定route-key*/
    public Binding BINDING_QUEUE_INFORM_SMS(@Qualifier(QUEUE_INFORM_SMS) Queue sms,
                                              @Qualifier(EXCHANGE_TOPICS_INFORM) Exchange exchange) {
        return BindingBuilder.bind(sms).to(exchange).with(ROUTINGKEY_SMS).noargs();
    }

}
