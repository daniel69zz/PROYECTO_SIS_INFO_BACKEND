package com.sis.spring.coffix.controllers;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sis.spring.coffix.service.UsuarioService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.sis.spring.coffix.DTO.UsuarioDTO;
import com.sis.spring.coffix.exceptions.UserNotFound;
import com.sis.spring.coffix.model.Usuario;


@RestController
@RequestMapping("/auth")
public class UsuarioController {
    private final UsuarioService us_serv;

    public UsuarioController(UsuarioService us_serv){
        this.us_serv = us_serv;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UsuarioDTO us_dto) {
        try {
            Usuario user = us_serv.iniciarSesion(us_dto.getUser(), us_dto.getPassword());
            return ResponseEntity.ok(user); 
        } catch(UserNotFound e){
            return ResponseEntity.status(404).body(
                Map.of(
                    "error", e.getMessage(),
                    "timestamp", LocalDateTime.now(),
                    "status", 404
                )
            );
        }catch(Exception e){
            return ResponseEntity. status(500).body(
                Map.of(
                    "error", "Error interno del servidor",
                    "timestamp", LocalDateTime.now(),
                    "status", 500
                )
            );
        }
    }
    
}
