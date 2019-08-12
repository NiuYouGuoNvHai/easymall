package cn.tedu.controller;


import cn.tedu.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("order/manage")

public class OrderController {
    @Autowired
    private OrderService orderService;

    /* 查询订单 */
    @RequestMapping("query/{userId}")
    public List<Order> queryOder(@PathVariable String userId){


        return null;
    }



}
