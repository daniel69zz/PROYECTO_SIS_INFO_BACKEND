package com.sis.spring.coffix.exceptions;

public class InvalidEntryInt extends RuntimeException {
    public InvalidEntryInt(Integer nume){
        super("Entrada nulls o negativos: "+ nume);
    }
}
