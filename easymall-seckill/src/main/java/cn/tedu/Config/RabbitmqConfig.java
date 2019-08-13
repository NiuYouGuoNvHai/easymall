package cn.tedu.Config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

    /* 使用路由模式实现 秒杀功能 */
    //声明队列对象 路由模式解决秒杀

    @Bean
    public Queue queue01(){
        return new Queue("seckillQ01", false, false, false, null);
    }
    //声明交换机 路由模式
    @Bean
    public DirectExchange ex01(){
        return new DirectExchange("seckillEX");
    }

    //队列与交换机的绑定关系
    @Bean
    public Binding bind01(){
        return BindingBuilder.bind(queue01()).to(ex01()).with("seckill");
    }

}
