package com.sis.spring.coffix.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.sis.spring.coffix.model.Usuario;


public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByUsuario(String usuario);
}
