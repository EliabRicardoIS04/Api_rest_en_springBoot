package com.proyectoAPIREST.APIREST_Foto.controlador;

import com.proyectoAPIREST.APIREST_Foto.entidades.Usuario;
import com.proyectoAPIREST.APIREST_Foto.servicio.AzureBlobService;
import com.proyectoAPIREST.APIREST_Foto.servicio.AzureServiceBusSender;
import com.proyectoAPIREST.APIREST_Foto.servicio.PhotoService;
import com.proyectoAPIREST.APIREST_Foto.servicio.UsuarioService;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/usuario")
@CrossOrigin(origins = "http://localhost:4200")
public class Controlador {

    private final UsuarioService usuarioService;
    private final AzureBlobService blobService;
    private final AzureServiceBusSender serviceBusSender;
    private final PhotoService photoService;

    @Autowired
    public Controlador(
            UsuarioService usuarioService,
            AzureBlobService blobService,
            AzureServiceBusSender serviceBusSender,
            PhotoService photoService
    ) {
        this.usuarioService = usuarioService;
        this.blobService = blobService;
        this.serviceBusSender = serviceBusSender;
        this.photoService = photoService;
    }

    // Endpoint para obtener usuarios
    @GetMapping
    public ResponseEntity<?> getAllUsuarios() {
        return ResponseEntity.ok(usuarioService.getUsuarios());
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
    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @CrossOrigin
    public ResponseEntity<?> uploadPhotoSimple(
            @RequestParam("file") MultipartFile file
    ) {
        String photoUrl = blobService.uploadFile(file);
        serviceBusSender.sendMessage("Nueva foto subida: " + photoUrl);
        return ResponseEntity.ok().build();
    }

}
