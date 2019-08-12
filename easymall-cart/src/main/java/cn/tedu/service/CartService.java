package cn.tedu.service;

import cn.tedu.mapper.CartMapper;
import com.jt.common.pojo.Cart;
import com.jt.common.pojo.Product;
import com.jt.common.utils.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.JedisCluster;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CartService {
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private RestTemplate ribbon;
    @Autowired
    private JedisCluster cluster;

    /* 查询购物车 */
    public List<Cart> queryCart(String userId) throws IOException {
        // 构造set
        String cartUserKey = "cart_user_" + userId;
        // 判断购物车是否存在
        if(cluster.exists(cartUserKey)){
            // 存在
            Set<String> productIds = cluster.smembers(cartUserKey);
            // 准备返回的list
            ArrayList<Cart> cartList = new ArrayList<>();
            for (String productId : productIds){
                // 遍历每一个成员
                String cartKey = "cart_" + userId + productId;
                if(cluster.exists(cartKey)){
                    String cartJson = cluster.get(cartKey);
                    Cart cart = MapperUtil.MP.readValue(cartJson, Cart.class);
                    cartList.add(cart);
                    return cartList;
                }
            }
        }else {
            // 不存在
            return null;
        }
        return null;
    }

    /* 新增商品到购物车(新增购物车记录) */
    // 前端传来的Cart对象只有userId,producdId,num.需补全信息再存数据库
    public void saveCart(Cart cart) throws IOException {
        // 判断购物车记录是否存在
        String cartUserKey = "cart_user_" + cart.getUserId();
        // 购物车记录存在 --> 修改num
        if (cluster.sismember(cartUserKey, cart.getProductId())){
            String cartKey = "cart_" + cart.getUserId() + cart.getProductId();
            String cartJson = cluster.get(cartKey);
            Cart exist = MapperUtil.MP.readValue(cartJson, Cart.class);
            cart.setNum(cart.getNum() + exist.getNum());
            // 更新数据库
            cartMapper.updateCartNum(cart);
            // 更新缓存
            cluster.set(cartKey, MapperUtil.MP.writeValueAsString(exist));
        } else {
        // 购物记录不存在 --> 新增购物车
            // 先查询商品表
            Product product = ribbon.getForObject("http://productService/product/manage/item" + cart.getProductId(), Product.class);
            // 补齐商品信息
            cart.setProductName(product.getProductName());
            cart.setProductImage(product.getProductImgurl());
            cart.setProductPrice(product.getProductPrice());
            // 存数据库
            cartMapper.saveCart(cart);
            // 再存缓存
            String cartKey = "cart_" + cart.getUserId() + cart.getProductId();
            // 存set
            cluster.sadd(cartUserKey, cart.getProductId());
            // 存map
            cluster.set(cartKey, MapperUtil.MP.writeValueAsString(cart));
        }
    }

    /* 增加购物车商品的数量(购物车记录已经存在) */
    public void updateCart(Cart cart) {
        cartMapper.updateCartNum(cart);
    }

    /* 删除购物车记录 */
    public void deleteCart(Cart cart) {
        cartMapper.deleteCart(cart);
    }







}
