package com.sis.spring.coffix.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sis.spring.coffix.exceptions.AuthIncorrect;
import com.sis.spring.coffix.exceptions.UserNotFound;
import com.sis.spring.coffix.model.Usuario;
import com.sis.spring.coffix.repository.UsuarioRepository;

@Service
public class UsuarioService {
    private final UsuarioRepository us_rep;
    private final PasswordEncoder pass_encode;

    public UsuarioService(UsuarioRepository us_rep, PasswordEncoder pass_encode) {
        this.pass_encode = pass_encode;
        this.us_rep = us_rep;
    }
    
    public Usuario iniciarSesion(String user, String password){
        Usuario user_valid = us_rep.findByUsuario(user)
        .orElseThrow(() -> new UserNotFound(user));

        try{
            if (!pass_encode.matches(password, user_valid.getPassword())) {
                throw new AuthIncorrect();
            }

            return user_valid;
            
        }catch(AuthIncorrect e){
            throw e;
        }catch(Exception e){
            throw new RuntimeException("Error del servidor", e);
        }
    }
    
}
