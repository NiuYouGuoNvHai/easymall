package cn.tedu.service;

import cn.tedu.mapper.ProductMapper;
import com.jt.common.pojo.EasyUIResult;
import com.jt.common.pojo.Product;
import com.jt.common.utils.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private JedisCluster jedis;

    /* 分页数据查询 */
    public EasyUIResult queryByPage(Integer page, Integer rows) {
        //sql语句:select * from table limit #{start},#{rows}
        int start=(page-1)*rows;
        //封装EasyUIResult total select count(*)
        //List<Product>
        EasyUIResult result=new EasyUIResult();
        List<Product> pList=productMapper.queryProductByPage(start,rows);
        Integer total=productMapper.queryTotal();
        //封装数据
        result.setRows(pList);
        result.setTotal(total);
        return result;
    }

    /* 单个商品查询 */
    public Product queryById(String productId) {
        // 根据id生成一个Key
        String key = "product_query_id" + productId;
        // 防止更新商品时产生缓存与数据库数据不一致 --> 加锁
        String lock = "update" + productId +".lock";
        // 实现缓存逻辑
        if(jedis.exists(lock)){ // 数据库正在更新,直接访问数据库
            return productMapper.queryById(productId);
        }

        // 缓存有数据,从缓存拿;没有则访问数据库,并将查询结果同步到缓存
        try{
            if(jedis.exists(key)){
                // 缓存有数据,获取键值对
                String data = jedis.get(key);
                // 将value(jackson)转为product对象
                Product product = MapperUtil.MP.readValue(data, Product.class);
                return product;
            }else{
                // 缓存没有数据,从数据库获取键值对
                Product product = productMapper.queryById(productId);
                // 将product对象转为jackson
                String jsonData = MapperUtil.MP.writeValueAsString(product);
                // 并同步到缓存中(缓存有效时间两天-->60*60*24*2)
                jedis.setex(key, 60*60*24*2, jsonData);
                return product;
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /* 新增商品 */
    public void saveProduct(Product product) {
        //TODO redis缓存存储数据
        //TODO 补充完成product的数据
        String productId= UUID.randomUUID().toString();
        product.setProductId(productId);
        productMapper.saveProduct(product);
    }

    /* 商品数据更新 */
    public void updateProduct(Product product) {
        // 修改数据前必须先删除缓存,以防止更新商品时产生缓存与数据库数据不一致 --> 加锁

        // 加锁
        String lock = "update" + product.getProductId() + ".lock";
        // lock有效时间
        jedis.setex(lock, 60*60*24*2, "");
        // 删除缓存
        String key = "product_query_id" + product.getProductId();
        jedis.del(key);
        // 执行修改商品SQL
        productMapper.updateProduct(product);
        // 释放锁
        jedis.del(lock);
    }
}
