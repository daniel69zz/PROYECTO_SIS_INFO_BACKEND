package com.sis.spring.coffix.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "pedidos")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_pedido;

    @Column(name = "cod_pedido", length = 100)
    private String cod_pedido;

    @Column(name = "fc_hora")
    private LocalDateTime fc_hora;

    @Column(name = "estado", length = 40)
    private String estado;

    @Column(name = "id_usuario")
    private Integer id_usuario;

    @OneToMany(
        mappedBy = "pedido",          
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Detalle_Pedido> detalles = new ArrayList<>();

    public Pedido(){

    }

   public Pedido(String cod_pedido, String estado, LocalDateTime fc_hora, Integer id_usuario) {
        this.cod_pedido = cod_pedido;
        this.estado = estado;
        this.fc_hora = fc_hora;
        this.id_usuario = id_usuario;
    }

    public Integer getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(Integer id_pedido) {
        this.id_pedido = id_pedido;
    }

    public String getCod_pedido() {
        return cod_pedido;
    }

    public void setCod_pedido(String cod_pedido) {
        this.cod_pedido = cod_pedido;
    }

    public LocalDateTime getFc_hora() {
        return fc_hora;
    }

    public void setFc_hora(LocalDateTime fc_hora) {
        this.fc_hora = fc_hora;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Integer id_usuario) {
        this.id_usuario = id_usuario;
    }

    public List<Detalle_Pedido> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<Detalle_Pedido> detalles) {
        this.detalles = detalles;
    }

    public void addDetalle(Detalle_Pedido detalle) {
        detalles.add(detalle);
        detalle.setPedido(this); 
    }
}
