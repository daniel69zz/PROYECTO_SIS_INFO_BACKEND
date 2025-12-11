package com.sis.spring.coffix.DTO;

import jakarta.validation.constraints.NotBlank;

public class UsuarioDTO {
    @NotBlank
    private String user;

    @NotBlank
    private String password;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
