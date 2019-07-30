package springbootrabbitmq.testspringbootrabbitmqconsumer.mq;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import springbootrabbitmq.testspringbootrabbitmqconsumer.config.RabbitMqConfig;

/*
 * 消费者监听 队列
 *
 * */
@Component
public class ReceiveHandler {


    /*监听email队列*/
    @RabbitListener(queues = {RabbitMqConfig.QUEUE_INFORM_EAMIL})
    public void listen_email(String msg, Message message, Channel channel) {
        System.out.println("##Email_msg:"+msg);
//        System.out.println("##message:"+new Gson().toJson(message));
//        System.out.println("##channel:"+new Gson().toJson(channel));
    }



    /*监听SMS队列*/
    @RabbitListener(queues = {RabbitMqConfig.QUEUE_INFORM_SMS})
    public void listen_sms(String msg, Message message, Channel channel) {
        System.out.println("##SMS_msg:"+msg);
//        System.out.println("##message:"+new Gson().toJson(message));
//        System.out.println("##channel:"+new Gson().toJson(channel));
    }



}
