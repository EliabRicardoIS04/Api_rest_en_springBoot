/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyectoAPIREST.APIREST_Foto.servicio;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ldric
 */
@Service
public class PhotoService {

   @Autowired
    private AzureBlobService blobService;

    public String uploadPhoto(MultipartFile file, Long userId, List<String> tags) {
        String photoUrl = blobService.uploadFile(file);
        // Lógica adicional (ej: guardar en DB con userId y tags)
        return photoUrl;
    }
    
   
    
}
