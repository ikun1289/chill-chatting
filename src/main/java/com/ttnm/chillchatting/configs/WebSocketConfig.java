package com.ttnm.chillchatting.configs;

import java.util.Arrays;

import com.ttnm.chillchatting.utils.CustomHandshakeHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/message");
        config.setApplicationDestinationPrefixes("/chill-chatting");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setHandshakeHandler(new CustomHandshakeHandler())
                .setAllowedOriginPatterns("http://localhost:4200","http://localhost:3000", "https://lofi-music-omega.vercel.app", "https://lofi-chill.vercel.app").withSockJS();
//    	registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();

    }
}
