/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyectoAPIREST.APIREST_Foto.configuracion;



import com.proyectoAPIREST.APIREST_Foto.servicio.PhotoNotificationWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer { 

    private final PhotoNotificationWebSocketHandler photoNotificationHandler;

    public WebSocketConfig(PhotoNotificationWebSocketHandler photoNotificationHandler) {
        this.photoNotificationHandler = photoNotificationHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(photoNotificationHandler, "/ws/notifications")
                .setAllowedOrigins("*"); // Permite cualquier origen
    }
}