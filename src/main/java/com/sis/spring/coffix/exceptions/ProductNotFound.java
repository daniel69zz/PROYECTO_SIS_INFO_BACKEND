package com.sis.spring.coffix.exceptions;

public class ProductNotFound extends RuntimeException{
    public ProductNotFound(Integer id) {
        super("Producto no encontrado con ID: " + id);
    }
}
