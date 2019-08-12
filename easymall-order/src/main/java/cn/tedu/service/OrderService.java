package cn.tedu.service;


import cn.tedu.mapper.OrderMapper;
import com.jt.common.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;

    /* 查询订单 */
    public List<Order> queryOrders(String userId) {
        return orderMapper.queryOrders(userId);
    }

    /* 订单新增 */
    public void saveOrders(Order order) {
        //补齐数据 orderId payState(0未支付,1支付) orderTime
        order.setOrderId(UUID.randomUUID().toString());
        order.setOrderPaystate(0);
        order.setOrderTime(new Date());
        orderMapper.saveOrder(order);
    }

    /* 订单删除 */
    public void deleteOrder(String orderId) {
        orderMapper.deleteOrder(orderId);
    }
}
