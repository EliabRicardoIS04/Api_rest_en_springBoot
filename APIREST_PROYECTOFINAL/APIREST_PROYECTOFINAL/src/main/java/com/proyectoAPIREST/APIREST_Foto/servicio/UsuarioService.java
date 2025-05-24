/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyectoAPIREST.APIREST_Foto.servicio;

import com.proyectoAPIREST.APIREST_Foto.entidades.Usuario;
import com.proyectoAPIREST.APIREST_Foto.repositorio.UsuarioRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 *
 * @author ldric
 */
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> getUsuarios() {
        return usuarioRepository.findAll();
    }
    public Usuario getUsuariobyID(String correo) {
        Optional<Usuario> usuarioOptional= usuarioRepository.findUsuarioByCorreo(correo);
        Usuario usuario;
         if (usuarioOptional.isPresent()) {
             usuario = usuarioOptional.get();
            if (usuario.getCorreo()== correo  ){
            return usuario;
            }
             
        }else{
         return null;
         }
        return usuario;
         
         
    }

    public Usuario addUsuario(Usuario usuario) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findUsuarioByCorreo((usuario.getCorreo()));
        if (usuarioOptional.isPresent()) {

            throw new IllegalStateException("email taken");
        }
        usuario.setId(null);
        usuarioRepository.save(usuario);
        return usuario;
    }

    public Optional<Usuario> addContactoAUsuario(String id, String[] listaContactos8) {
        Optional<Usuario> updateLC = usuarioRepository.findById(id);

        if (updateLC.isPresent()) {

            Usuario usuario = updateLC.get();
            usuario.setListaContactos(listaContactos8);
            System.out.println("usuario actiualizado -_  " + usuarioRepository.save(usuario));
            Optional.of(usuarioRepository.save(usuario));
        }else{
            System.out.println("------- no se actualizo el usuario -------");
           
        }
         return null;

    }


    
}
