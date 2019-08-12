package cn.tedu.mapper;

import com.jt.common.pojo.Product;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductMapper {

    /* 分页数据查询 */
    List<Product> queryProductByPage(@Param("start") Integer start, @Param("rows") Integer rows);

    /* 查询product总数 */
    Integer queryTotal();

    /* 单个商品查询 */
    Product queryById(String productId);

    /* 新增商品 */
    void saveProduct(Product product);

    /* 商品数据更新 */
    void updateProduct(Product product);

}
