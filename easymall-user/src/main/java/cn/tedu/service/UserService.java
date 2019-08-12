package cn.tedu.service;

import cn.tedu.mapper.UserMapper;
import cn.tedu.sta.RedisRelative;
import com.jt.common.pojo.User;
import com.jt.common.utils.MD5Util;
import com.jt.common.utils.MapperUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JedisCluster jedis;

    /* 检查用户名是否存在 */
    public int checkUserName(String userName) {
        return userMapper.queryExistUser(userName);
    }

    /* 添加用户 */
    public void saveUser(User user){
        // 生成用户Id
        String userId = UUID.randomUUID().toString();
        // 加密用户密码
        String md5Password = MD5Util.md5(user.getUserPassword());

        user.setUserId(userId);
        user.setUserPassword(md5Password);
        // 插入用户数据
        userMapper.insertUser(user);
    }

    /* 用戶登录 */
    public String doLogin(User user) {
        // 查询用户名和密码
        try {
            // 获取密码并MD5加密(用于数据库查询对比)
            user.setUserPassword(MD5Util.md5(user.getUserPassword()));
            // 数据库查询
            User exist = userMapper.queryOne(user);
            if(exist == null){
                // 登录失败
                return "";
            }else {
                // 登录成功 --> 使用redis实现用户唯一登录功能

                // 在redis中保存的唯一登录标识 login_UserName
                String oTicket = jedis.get("login_" + user.getUserName());
                // 判断是否被登录过
                if(StringUtils.isNotEmpty(oTicket)){
                    // 已经登录,则顶替掉
                    jedis.del(oTicket);
                }
                // 第一次登录,将ticket保存到cookie返回给客户端
                String ticket = RedisRelative.cookieName + System.currentTimeMillis() + user.getUserName();
                // 登录信息存入redis
                String userJson = MapperUtil.MP.writeValueAsString(exist);
                jedis.setex(ticket, 60*60*24*2, userJson);
                // 保存登录标识
                jedis.setex("login_" + user.getUserName(), 60*60*24*2, ticket);
                return ticket;
            }

        }catch(Exception e){
            e.printStackTrace();
            return "";
        }
    }

    /* 用户登录数据状态验证 */
    public String queryTicket(String ticket) {
        // 完成续租功能: 在ticket到期的前30分钟,如果用户还在操作状态,则将ticket的时长延长30分钟
        try {
            // 获取ticket的剩余时间
            Long expireTime = jedis.pttl(ticket);
            if (expireTime < 1000*60*30){
                // 超时时间小于30分钟,续租30分钟
                long time = expireTime + 1000*60*30;
                jedis.pexpire(ticket, time);
            }
            // 不需要续租,正常返回获取的ticket
            // 用户经过持久层,直接从redis中查询即可
            return jedis.get(ticket);
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }


}

