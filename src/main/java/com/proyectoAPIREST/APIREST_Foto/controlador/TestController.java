/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyectoAPIREST.APIREST_Foto.controlador;

import com.proyectoAPIREST.APIREST_Foto.servicio.PhotoNotificationWebSocketHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ldric
 */

@RestController
@RequestMapping("/test")
public class TestController {
    
    private final PhotoNotificationWebSocketHandler handler;
    
    public TestController( PhotoNotificationWebSocketHandler handler){
        this.handler = handler;
    }
    
    
    @GetMapping("/notify")
    public ResponseEntity<String> testNotification(){
        handler.sendNotification("prueba de notificacion");
        return ResponseEntity.ok("notification enviada");
        
    }
    
}
