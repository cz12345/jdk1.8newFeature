package springbootrabbitmq.testsprinbootrabbitmqproducer.com.xuecheng.rabbit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import springbootrabbitmq.testsprinbootrabbitmqproducer.config.RabbitMqConfig;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RabbitProducer {


    @Autowired
    private RabbitTemplate rabbitTemplate;



    /*
    * 参数明细：
    * 1:交换机名称
    * 2：routingKey
    * 3：消息内容
    * */
    @Test
    public void testSendMessage() {
        String message = "send message to queue";
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_TOPICS_INFORM,"inform.email",message);
    }


    @Test
    public void testSendMessage2() {
        String message = "send message2 to queue";
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_TOPICS_INFORM,"inform.sms",message);
    }




}
