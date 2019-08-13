package cn.tedu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SecController {

    @Autowired
    private RabbitTemplate sender;
    @RequestMapping("send")
    public String send(String msg){
        sender.convertAndSend("seckillEX", "seckill","测试发送整合代码");
        return "success";
    }

}
