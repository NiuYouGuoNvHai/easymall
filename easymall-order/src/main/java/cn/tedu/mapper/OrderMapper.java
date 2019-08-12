package cn.tedu.mapper;

import com.jt.common.pojo.Order;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderMapper {
    /* 查询订单 */
    List<Order> queryOrders(String userId);

    /* 订单新增 */
    void saveOrder(Order order);

    /* 订单删除 */
    void deleteOrder(String orderId);
}
