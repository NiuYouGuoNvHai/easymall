package cn.tedu.controller;

import cn.tedu.service.UserService;
import com.jt.common.pojo.SysResult;
import com.jt.common.pojo.User;
import cn.tedu.sta.RedisRelative;
import com.jt.common.utils.CookieUtils;
import com.jt.common.utils.MapperUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("user/manage")
public class UserController {
    @Autowired
    private UserService userService;

    /* 检查用户名是否存在 */
    @RequestMapping("checkUserName")
    public SysResult checkUserName(String userName){
        int exist = userService.checkUserName(userName);
        if (exist == 1){
            // 存在
            return SysResult.build(201,"用户名不可用",null);
        }
        // 不存在
        return SysResult.ok();
    }

    /* 添加用户 */
    @RequestMapping("save")
    public SysResult saveUser(User user){
        try {
            userService.saveUser(user);
            return SysResult.ok();
        }catch (Exception e){
            e.printStackTrace();
            return SysResult.build(201, "", null);
        }
    }
    /* 用戶登录 */
    @RequestMapping("login")
    public SysResult doLogin(User user, HttpServletRequest req, HttpServletResponse res){
        // 登录成功,返回可用的redis的key值,失败则返回""
        String ticket = userService.doLogin(user);
        if(StringUtils.isNotEmpty(ticket)){
            // 登录成功-->使用cookie保存ticket的 EM_TICKET
            CookieUtils.setCookie(req, res, RedisRelative.cookieName, ticket);
            return SysResult.ok();
        }else {
            // 登录失败
            return SysResult.build(201, "", null);
        }
    }

    /* 用户登录数据状态验证 */
    @RequestMapping("query/{ticket}")
    public SysResult queryTicket(@PathVariable String ticket){
        // 从redis中查询是否保存有当前用户的ticket对应的userJson
        String userJson = userService.queryTicket(ticket);
        if (StringUtils.isNotEmpty(userJson)){
            // 存在用户登录信息
            return SysResult.build(200, "ok", userJson);
        }else{
            // 不存在用户登录信息
            return SysResult.build(201, "", null);
        }
    }


}
