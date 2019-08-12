package tedu;

import org.junit.Test;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.HostAndPort;

import java.util.HashSet;
import java.util.Set;

public class Demo {
    @Test
    public void test08(){
        // 1.收集节点信息
        Set<HostAndPort> set = new HashSet<>();
        // 2.至少提供一个可连接节点
        set.add(new HostAndPort("10.42.82.82",8000));
        // 3.构造一个配置内部连接池配置config
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();

        // 4.构造JedisCluster

    }



}
