/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyectoAPIREST.APIREST_Foto.servicio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class PhotoNotificationWebSocketHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(PhotoNotificationWebSocketHandler.class);
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("Nueva conexión WebSocket establecida: {}", session.getId());
        
        sessions.add(session);
        // Aquí podrías enviar un mensaje de bienvenida al cliente si lo deseas
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.info("Conexión WebSocket cerrada: {}, status: {}", session.getId(), status);
        sessions.remove(session);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        logger.info("Mensaje recibido del cliente {}: {}", session.getId(), message.getPayload());
        // Aquí podrías procesar mensajes enviados por el cliente si tu aplicación lo requiere
    }
    
   @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        logger.error("Error de transporte en la conexión WebSocket de {}: {}", session.getId(), exception.getMessage());
        sessions.remove(session);
    }

    // Método para enviar una notificación a todos los clientes conectados
    public void sendNotification(String message) {
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (IOException e) {
                    logger.error("Error al enviar mensaje WebSocket a {}: {}", session.getId(), e.getMessage());
                }
            }
        }
    }
    
}