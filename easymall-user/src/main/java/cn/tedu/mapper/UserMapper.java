package cn.tedu.mapper;

import com.jt.common.pojo.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

    /* 检查用户名是否存在 */
    int queryExistUser(String userName);

    /* 添加用户 */
    void insertUser(User user);

    /* 用戶登录 */
    User queryOne(User user);
}
