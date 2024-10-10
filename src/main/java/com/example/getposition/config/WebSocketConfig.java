package com.example.getposition.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointConfigurator(){
        return new ServerEndpointExporter();
    }
    //如果打包成jar包运行，bean注入这个配置类，war包的不需要。
}


