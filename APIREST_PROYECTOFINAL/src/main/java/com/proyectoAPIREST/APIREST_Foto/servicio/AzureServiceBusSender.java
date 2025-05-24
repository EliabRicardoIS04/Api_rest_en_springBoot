/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyectoAPIREST.APIREST_Foto.servicio;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author ldric
 */
@Service
public class AzureServiceBusSender {
      private final ServiceBusSenderClient senderClient;

    public AzureServiceBusSender(@Value("${azure.servicebus.connection-string}") String connectionString,
                                 @Value("${azure.servicebus.queue-name}") String queueName) {
        this.senderClient = new ServiceBusClientBuilder()
                .connectionString(connectionString)
                .sender()
                .queueName(queueName)
                .buildClient();
    }

    public void sendMessage(String message) {
        senderClient.sendMessage(new ServiceBusMessage(message));
    }
}
