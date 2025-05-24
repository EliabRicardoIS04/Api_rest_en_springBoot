/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyectoAPIREST.APIREST_Foto.repositorio;

import com.proyectoAPIREST.APIREST_Foto.entidades.Usuario;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;


/**
 *
 * @author ldric
 */

public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    
    Optional<Usuario> findUsuarioByCorreo(String correo);
}
