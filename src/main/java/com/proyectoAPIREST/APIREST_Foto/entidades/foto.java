
package com.proyectoAPIREST.APIREST_Foto.entidades;

import javax.swing.ImageIcon;

public class foto {
    private int Id ;
    private int id_usuario;
    private ImageIcon image ;
    
    public foto (int id_U ,ImageIcon i ){
        this.id_usuario = id_U;
        this.image  = i;
    }
    
    public int getId() {
        return Id;
    }
    
    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public ImageIcon getImage() {
        return image;
    }

    public void setImage(ImageIcon image) {
        this.image = image;
    }
    
    
    
}
