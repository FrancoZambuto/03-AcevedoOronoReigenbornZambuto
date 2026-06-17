package com.desi.accesoDatos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.desi.entidades.Persona;

public interface PersonaRepository extends JpaRepository<Persona, Long> {

	List<Persona> findByEliminadaFalse();

	Optional<Persona> findByIdAndEliminadaFalse(Long id);
}
