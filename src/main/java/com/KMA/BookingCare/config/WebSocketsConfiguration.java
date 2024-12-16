package com.KMA.BookingCare.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Created by IntelliJ IDEA.
 * Project : springboot-webrtc
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 28/12/20
 * Time: 17.41
 */
@Configuration
@EnableWebSocket
public class WebSocketsConfiguration implements WebSocketConfigurer {

    @Autowired
    private SignalingSocketHandler signalingSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(signalingSocketHandler, "/room").setAllowedOrigins("*");
    }


}