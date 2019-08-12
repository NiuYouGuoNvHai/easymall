package cn.tedu.controller;

import cn.tedu.service.CartService;
import com.jt.common.pojo.Cart;
import com.jt.common.pojo.SysResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("cart/manage")
public class CartController {
    @Autowired
    private CartService cartService;

    /* 查询购物车 */
    @RequestMapping("query")
    public List<Cart> queryCart(String userId) throws IOException {
        // 再次非空校验
        if(StringUtils.isNotEmpty(userId)){
            // userId不为空
            return cartService.queryCart(userId);
        }else{
            // userId为空
            return null;
        }
    }

    /* 新增商品到购物车(新增购物车记录) */
    // 前端传来的Cart对象只有userId,producdId,num.需补全信息再存数据库
    @RequestMapping("save")
    public SysResult saveCart(Cart cart){
        try {
            cartService.saveCart(cart);
            return SysResult.ok();
        }catch (Exception e){
            e.printStackTrace();
            return SysResult.build(201, "", null);
        }
    }

    /* 增加购物车商品的数量(购物车记录已经存在) */
    @RequestMapping("update")
    public SysResult updateCart(Cart cart){
        try {
            cartService.updateCart(cart);
            return SysResult.ok();
        }catch (Exception e){
            e.printStackTrace();
            return SysResult.build(201, "", null);
        }
    }

    /* 删除购物车记录 */
    @RequestMapping("delete")
    public SysResult deleteCart(Cart cart){
        try {
            cartService.deleteCart(cart);
            return SysResult.ok();
        }catch (Exception e){
            e.printStackTrace();
            return SysResult.build(201, "", null);
        }
    }



}
