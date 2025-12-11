package com.sis.spring.coffix.exceptions;

public class PedidoNotFound extends RuntimeException {
    public PedidoNotFound(Integer id) {
        super("Pedido no encontrado con ID: " + id);
    }
}
