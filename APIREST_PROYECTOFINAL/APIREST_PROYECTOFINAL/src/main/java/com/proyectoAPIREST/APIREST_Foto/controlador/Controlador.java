package com.proyectoAPIREST.APIREST_Foto.controlador;

import com.azure.storage.queue.QueueClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectoAPIREST.APIREST_Foto.DTO.FotoMessage;
import com.proyectoAPIREST.APIREST_Foto.entidades.Usuario;
import com.proyectoAPIREST.APIREST_Foto.servicio.AzureBlobService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectoAPIREST.APIREST_Foto.servicio.PhotoNotificationWebSocketHandler;
import com.proyectoAPIREST.APIREST_Foto.servicio.PhotoService;
import com.proyectoAPIREST.APIREST_Foto.servicio.UsuarioService;
import java.util.Base64;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/usuario")
public class Controlador {

      private final ObjectMapper mapper = new ObjectMapper();
    private final UsuarioService usuarioService;
    private final AzureBlobService blobService;
    //private final AzureServiceBusSender serviceBusSender;
    private final PhotoService photoService;
    private final QueueClient queueClient;
    //private final ObjectMapper mapper;
     private final PhotoNotificationWebSocketHandler notificationHandler;

    @Autowired
    public Controlador(
            UsuarioService usuarioService,
            AzureBlobService blobService,
            //AzureServiceBusSender serviceBusSender,
            PhotoService photoService,
            QueueClient queueClient, ObjectMapper mapper,
            PhotoNotificationWebSocketHandler notificationHandler
            
    ) {
        this.queueClient = queueClient;
        //this.mapper = mapper;
        this.usuarioService = usuarioService;
        this.blobService = blobService;
        //this.serviceBusSender = serviceBusSender;
        this.photoService = photoService;
        this.notificationHandler = notificationHandler;
    }

    // Endpoint para obtener usuarios
    @GetMapping
    public ResponseEntity<?> getAllUsuarios() {
        return ResponseEntity.ok(usuarioService.getUsuarios());
    }
    
    @GetMapping("/{correo}")
    public ResponseEntity<?> getUsuarioById(@PathVariable String correo) {
        
        try {
            System.out.println("Usuario por correo: "+ usuarioService.getUsuariobyID(correo).toString());
              Usuario userSave = usuarioService.getUsuariobyID(correo);
            return new ResponseEntity<Usuario>(userSave, HttpStatus.CREATED) ;
            
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getCause().toString(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    // Endpoint para crear un usuario (POST sin archivo)
    @PostMapping
    public ResponseEntity<?> registerNewUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario userSave = usuarioService.addUsuario(usuario);

            return new ResponseEntity<Usuario>(userSave, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getCause().toString(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarContactostoUsuario(
            @PathVariable String id,
            @RequestBody String[] listaContactos) {

        try {

            usuarioService.addContactoAUsuario(id, listaContactos);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getCause().toString(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return null;

    }

    // Endpoint para subir foto con metadata (userId, tags)
    @PostMapping(
            value = "/photo",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> uploadPhotoWithMetadata(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") Long userId,
            @RequestParam("tags") List<String> tags
    ) {
        String photoUrl = photoService.uploadPhoto(file, userId, tags);
        return ResponseEntity.ok("Foto con metadata subida: " + photoUrl);
    }

    // Endpoint para subir foto simple (solo archivo)
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadPhoto(@RequestParam("file") MultipartFile file) {
        try {
            // 1. Subir a Blob y obtener la URL pública
            String photoUrl = blobService.uploadFile(file);

            // 2. Preparar el mensaje JSON con base64 y mime
            byte[] content = file.getBytes();
            String base64 = Base64.getEncoder().encodeToString(content);
            String mime = file.getContentType();

            FotoMessage fotoMsg = new FotoMessage();
            fotoMsg.setNombreArchivo(file.getOriginalFilename());
            fotoMsg.setContenidoBase64(base64);
            fotoMsg.setTipoMime(mime);
            

            String json = mapper.writeValueAsString(fotoMsg);

            // 3. Enviar el JSON a la cola
            queueClient.sendMessage(json);

            // 4. Notificar inmediatamente por WebSocket (y el listener también lo recogerá)
            //notificationHandler.sendNotification(json);

            return ResponseEntity.ok(json);
        } catch (Exception e) {
            return ResponseEntity
                .internalServerError()
                .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

}
