package com.sis.spring.coffix.exceptions;

public class UserNotFound extends RuntimeException {
    public UserNotFound(String user){
        super("Usuario no encontrado " + user);
    }
}
