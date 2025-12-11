package com.sis.spring.coffix.model;

import org.hibernate.annotations.Formula;

import jakarta.persistence.*;


@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_usuario;

    @Column(name = "usuario", length = 40)
    private String usuario;

    @Column(name = "password", length = 200)
    private String password;

    @Formula("(SELECT t_rol.nombre FROM tipo_rol t_rol WHERE t_rol.id_tipo_rol = id_tipo_rol)")
    private String rol;

    public Usuario(Integer id_usuario, String password, String rol, String usuario) {
        this.id_usuario = id_usuario;
        this.password = password;
        this.rol = rol;
        this.usuario = usuario;
    }

    public Usuario(){   
    }

    public Integer getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Integer id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    
    
}
