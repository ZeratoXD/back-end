package com.generation.NossoPomar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.generation.NossoPomar.model.Categoria;

public interface CategoryRepository extends JpaRepository<Categoria, Long> {

	public List<Categoria> findAllByNomeContainingIgnoreCase(@Param("nome") String nome);

}
