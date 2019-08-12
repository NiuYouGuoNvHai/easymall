package cn.tedu;


import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "es")
public class TransportConfig {

    private List<String> nodes;

    @Bean
    public TransportClient initClient(){
        PreBuiltTransportClient transportClient = new PreBuiltTransportClient(Settings.EMPTY);
        for (String node : nodes){
            try {


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public List<String> getNodes() {
        return nodes;
    }

    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
    }

}
