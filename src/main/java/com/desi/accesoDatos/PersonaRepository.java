package com.desi.accesoDatos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.desi.entidades.Persona;

public interface PersonaRepository extends JpaRepository<Persona, Long> {

	@Query("SELECT p FROM Persona p WHERE p.eliminada = false")
	List<Persona> listarActivas();

	@Query("SELECT p FROM Persona p WHERE p.id = :id AND p.eliminada = false")
	Optional<Persona> buscarActivaPorId(@Param("id") Long id);
}
