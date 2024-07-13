package com.generation.NossoPomar.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.NossoPomar.model.Usuario;

public interface UserRepository extends JpaRepository<Usuario, Long> {

	public Optional<Usuario> findByEmail(String email);

}