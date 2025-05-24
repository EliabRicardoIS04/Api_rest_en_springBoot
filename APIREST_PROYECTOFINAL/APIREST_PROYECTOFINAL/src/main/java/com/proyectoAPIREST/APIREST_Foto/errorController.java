
package com.proyectoAPIREST.APIREST_Foto;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class errorController implements org.springframework.boot.web.servlet.error.ErrorController {
    
     @RequestMapping("/error")
    public ResponseEntity<ErrorResponse> handleError(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        String message = "Error en la API";
        
        if(statusCode == null) {
            return ResponseEntity.internalServerError().body(new ErrorResponse(500, message));
        }
        
        return ResponseEntity.status(statusCode)
                .body(new ErrorResponse(statusCode, message));
    }

    // Clase interna para la respuesta de error
    private static class ErrorResponse {
        private int status;
        private String message;
        
        public ErrorResponse(int status, String message) {
            this.status = status;
            this.message = message;
        }
        
        // Getters
        public int getStatus() { return status; }
        public String getMessage() { return message; }
    }
}



