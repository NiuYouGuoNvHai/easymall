package cn.tedu.controller;

import cn.tedu.service.ProductService;
import com.jt.common.pojo.EasyUIResult;
import com.jt.common.pojo.Product;
import com.jt.common.pojo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("product/manage")
public class ProductController {

    @Autowired
    private ProductService productService;

    /* 分页数据查询 */
    @RequestMapping("/pageManage")
    public EasyUIResult queryByPage(Integer page, Integer rows){
        EasyUIResult result = productService.queryByPage(page,rows);
        return result;
    }

    /* 单个商品查询 */
    @RequestMapping("item/{productId}")
    public Product queryById(@PathVariable String productId){
        return productService.queryById(productId);
    }

    /* 新增商品 */
    @RequestMapping("save")
    public SysResult saveProduct(Product product){
        //利用一个异常逻辑判断新增成功失败
        try{
            productService.saveProduct(product);
            return SysResult.ok();//{"status":200,"msg":"ok"}
        }catch(Exception e){
            e.printStackTrace();
            return SysResult.build(201, e.getMessage(), null);
        }
    }

    /* 商品数据更新 */
    @RequestMapping("update")
    public SysResult updateProduct(Product product){
        //业务层调用持久层修改数据
        try{
            productService.updateProduct(product);
            return SysResult.ok();
        }catch(Exception e){
            e.printStackTrace();
            return SysResult.build(201, e.getMessage(), null);
        }
    }

    /* 商品删除 */
    // TODO

}
