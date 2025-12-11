package com.sis.spring.coffix.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pedido_pago")
public class PedidoPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_pago;

    @Column(name = "id_pedido", nullable = false)
    private Integer id_pedido;

    @Column(name = "fc_hora", nullable = false)
    private LocalDateTime fc_hora;

    @Column(name = "monto_pagado", nullable = false)
    private BigDecimal monto_pagado;

    @Column(name = "estado", length = 40, nullable = false)
    private String estado;

    public Integer getId_pago() {
        return id_pago;
    }

    public void setId_pago(Integer id_pago) {
        this.id_pago = id_pago;
    }

    public Integer getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(Integer id_pedido) {
        this.id_pedido = id_pedido;
    }

    public LocalDateTime getFc_hora() {
        return fc_hora;
    }

    public void setFc_hora(LocalDateTime fc_hora) {
        this.fc_hora = fc_hora;
    }

    public BigDecimal getMonto_pagado() {
        return monto_pagado;
    }

    public void setMonto_pagado(BigDecimal monto_pagado) {
        this.monto_pagado = monto_pagado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
