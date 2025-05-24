
package com.proyectoAPIREST.APIREST_Foto.entidades;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "usuarios")

public class Usuario{
    
    @Id
    private String id;
    
    private String nombre;
    private String correo;
    private String contrasena;
    private String[] listaContactos;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String[] getListaContactos() {
        return listaContactos;
    }

    public void setListaContactos(String[] listaContactos2) {
       
        
        /*
        for (int i =0 ; i < listaContactos.length; i++) {
            this.listaContactos[this.listaContactos.length] = listaContactos2[i] ;
        }*/
        
        this.listaContactos = listaContactos2;
    }
    
    
}
