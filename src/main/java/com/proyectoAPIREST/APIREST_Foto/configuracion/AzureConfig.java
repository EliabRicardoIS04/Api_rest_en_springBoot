/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyectoAPIREST.APIREST_Foto.configuracion;

import com.azure.storage.queue.QueueClient;
import com.azure.storage.queue.QueueClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author ldric
 */
@Configuration
public class AzureConfig {
    
    @Value("${azure.storage.queue.connection-string}")
    private String connectionString;

    @Value("${azure.storage.queue.name}")
    private String queueName;

    @Bean
    public QueueClient queueClient() {
        return new QueueClientBuilder()
            .connectionString(connectionString)
            .queueName(queueName)
            .buildClient();
    }
}