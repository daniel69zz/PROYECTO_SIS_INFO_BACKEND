package com.sis.spring.coffix.exceptions;

public class AuthIncorrect extends RuntimeException {
    public AuthIncorrect(){
        super("Inicio de sesion fallido");
    }
}
