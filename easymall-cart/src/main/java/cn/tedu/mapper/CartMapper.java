package cn.tedu.mapper;

import com.jt.common.pojo.Cart;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartMapper {

    /* 查询购物车 */
    List<Cart> queryCart(String userId);

    /* 查询单个购物车记录 */
    Cart queryOne(Cart cart);

    /* 添加购物车记录 */
    void saveCart(Cart cart);

    /* 修改购物车商品的数量 */
    void updateCartNum(Cart exist);

    /* 删除购物车记录 */
    void deleteCart(Cart cart);
}
