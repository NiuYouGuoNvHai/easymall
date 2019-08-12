package cn.tedu.redis;


import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "redis.cluster")
public class RedisClusterConfig {

    private List<String> nodes;
    private Integer maxTotal;
    private Integer maxIdle;
    private Integer minIdle;

    @Bean
    public JedisCluster initJedisCluster(){
        // 1.收集节点信息
        HashSet<HostAndPort> set = new HashSet<>();
        for(String node : nodes){
            String host = node.split(":")[0];
            int port = Integer.parseInt(node.split(":")[1]);
            set.add(new HostAndPort(host, port));
        }
        // 2.创建配置对象
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);

        // 3.创建JedisCluster对象并返回
        return new JedisCluster(set, config);
    }

    public List<String> getNodes() {
        return nodes;
    }

    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
    }

    public Integer getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(Integer maxTotal) {
        this.maxTotal = maxTotal;
    }

    public Integer getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(Integer maxIdle) {
        this.maxIdle = maxIdle;
    }

    public Integer getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }
}
