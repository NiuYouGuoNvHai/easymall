package cn.tedu.service;

import com.jt.common.pojo.PicUploadResult;
import com.jt.common.utils.UploadUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class PicService {

    /* 处理图片上传的路径请求 */
    public PicUploadResult picUpload(MultipartFile pic) {
        // 准备返回对象
        PicUploadResult result = new PicUploadResult();

        /* 获取图片名和内容,随机生成保存路径并保存,返回结果 */

        // 获取前端图片名
        String filename = pic.getOriginalFilename();
        // 截取后缀
        int index = filename.lastIndexOf(".");  // 获取最后一个 . 的下标
        String prefix = filename.substring(index);  // 从filename截取后缀(index,length-1)
        // 判断后缀是否合法
        boolean matches = prefix.matches(".(png|jpg|gif|)");
        if(!matches){
            // 不合法
            result.setError(1);
            return result;
        }
        // 合法:随机生成图片的路径 --> 八位随机路径 1/k/2k/2k/3/l/5/l
        String dir = UploadUtil.getUploadPath(filename, "upload") + "/";  // upload/1/k/2k/2k/3/l/5/l/
        // 图片保存的路径
        String path = "d://upload/" + dir;  // d:/upload/upload/1/k/2k/2k/3/l/5/l/
        // 图片的网络访问路径
        String urlPath = "http://www.image.jt.com/" + dir;  // http://www.image.jt.com/upload/1/k/2k/2k/3/l/5/l/
        //重命名图片名,使之唯一
        String name = UUID.randomUUID().toString()+prefix;  // 随机图片名+后缀
        // 保存图片到对应的路径
        // 判断文件夹是否存在
        File _dir = new File(path);
        if(!_dir.exists()){
            // 不存在,创建目录
            _dir.mkdirs();  // mkdir() --> 一级目录 | mkdirs() --> 多级目录
        }
        try {
            // 保存(上传) --> d:/upload/upload/1/k/2k/2k/3/l/5/l/
            pic.transferTo(new File(path+name));  // path+name --> 完整路径+完整文件名
            // http://www.image.jt.com/upload/1/k/2k/2k/3/l/5/l/完整文件名
            result.setUrl(urlPath+name);
        } catch (IOException e) {
            e.printStackTrace();
            result.setError(1);
            return result;
        }
        return result;
    }



}
