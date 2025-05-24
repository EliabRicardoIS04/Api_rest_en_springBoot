/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyectoAPIREST.APIREST_Foto.servicio;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ldric
 */
@Service
public class AzureBlobService {
    
    private final BlobContainerClient blobContainerClient;

    public AzureBlobService( @Value("${azure.storage.blob.connection-string}") String connectionString,
        @Value("${azure.storage.blob.container-name}") String containerName
    ) {
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();

        this.blobContainerClient = blobServiceClient.getBlobContainerClient(containerName);
        
        if (!blobContainerClient.exists()) {
            blobContainerClient.create();
        }
    }

    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public String uploadFile(MultipartFile file) {
        String blobName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        BlobClient blobClient = blobContainerClient.getBlobClient(blobName);

        try (InputStream dataStream = file.getInputStream()) {
            blobClient.upload(dataStream, file.getSize(), true);
            return blobClient.getBlobUrl();
        } catch (IOException e) {
            throw new RuntimeException("Error al subir el archivo", e);
        }
    }

    @Recover
    public String recoverUpload(RuntimeException e, MultipartFile file) {
        // Lógica alternativa (ej: guardar en base de datos local)
        return "default-error-photo.jpg";
    }
    
}
