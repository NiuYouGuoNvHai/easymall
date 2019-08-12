package cn.tedu.controller;


import cn.tedu.service.OrderService;
import com.jt.common.pojo.Order;
import com.jt.common.pojo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Order> queryOders(@PathVariable String userId){
        return orderService.queryOrders(userId);
    }

    /* 订单新增 */
    @RequestMapping("save")
    public SysResult saveOrders(Order order){
        try {
            orderService.saveOrders(order);
            return SysResult.ok();
        }catch(Exception e){
            e.printStackTrace();
            return SysResult.build(201, "not ok", null);
        }
    }

    /* 订单删除 */
    @RequestMapping("delete/{orderId}")
    public SysResult deleteOrder(@PathVariable String orderId){
        try {
            orderService.deleteOrder(orderId);
            return SysResult.ok();
        }catch(Exception e){
            e.printStackTrace();
            return SysResult.build(201, "not ok", null);
        }

    }

}
