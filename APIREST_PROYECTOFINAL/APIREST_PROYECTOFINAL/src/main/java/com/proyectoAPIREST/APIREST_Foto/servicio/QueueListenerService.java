package com.proyectoAPIREST.APIREST_Foto.servicio;

import com.azure.storage.queue.QueueClient;
import com.azure.storage.queue.QueueClientBuilder;
import com.azure.storage.queue.models.QueueMessageItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectoAPIREST.APIREST_Foto.DTO.FotoMessage;
import com.proyectoAPIREST.APIREST_Foto.servicio.PhotoNotificationWebSocketHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.extern.slf4j.Slf4j;

@Slf4j

@Service
public class QueueListenerService {

    private final QueueClient queueClient;
    private final ObjectMapper mapper = new ObjectMapper();
    private final PhotoNotificationWebSocketHandler webSocketHandler;


    public QueueListenerService(
            @Value("${azure.storage.queue.connection-string}") String connString,
            @Value("${azure.storage.queue.name}") String queueName,
            PhotoNotificationWebSocketHandler webSocketHandler
    ) {
        this.queueClient = new QueueClientBuilder()
                .connectionString(connString)
                .queueName(queueName)
                .buildClient();
        this.queueClient.createIfNotExists();
         this.webSocketHandler = webSocketHandler;
        System.out.printf("[Init] Escuchando cola '%s'...%n", queueName);
    }

    @Scheduled(fixedDelayString = "${listener.poll-interval}")
   @Scheduled(fixedDelayString = "${listener.poll-interval}")
public void pollQueue() {
    try {
        List<QueueMessageItem> messages = StreamSupport
                .stream(queueClient.receiveMessages(5, Duration.ofSeconds(30), null, null).spliterator(), false)
                .collect(Collectors.toList());

        for (QueueMessageItem msg : messages) {
            FotoMessage foto = mapper.readValue(msg.getMessageText(), FotoMessage.class);
            String json = mapper.writeValueAsString(foto); // Convertimos a JSON string

            // 🔔 Enviar el mensaje completo por WebSocket
            webSocketHandler.sendNotification(json);
            System.out.println("Mensaje completo recibido: " + msg.getMessageText());


            System.out.println("Recibido y enviado a WebSocket: " + foto.getNombreArchivo());
            queueClient.deleteMessage(msg.getMessageId(), msg.getPopReceipt());
            System.out.println(" Eliminado de la cola.");
        }
    } catch (Exception e) {
        System.err.println(" Error al procesar: " + e.getMessage());
    }
}

    
}



