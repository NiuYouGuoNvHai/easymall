package cn.tedu.controller;

import cn.tedu.service.PicService;
import com.jt.common.pojo.PicUploadResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class PicController {
    @Autowired
    private PicService picService;

    /* 处理图片上传的路径请求 */
    @RequestMapping("pic/upload")
    public PicUploadResult picUpload(MultipartFile pic){
        return picService.picUpload(pic);
    }

    /*  MutipartFile: 图片上传对象 --> 图片名,图片内容
    *
    *   PicUploadResult: 上传结果对象 --> 是否抛异常,前端访问图片的路径
    *
    */









}
