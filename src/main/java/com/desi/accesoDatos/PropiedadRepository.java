package com.desi.accesoDatos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.desi.entidades.Ciudad;
import com.desi.entidades.EstadoDisponibilidad;
import com.desi.entidades.Propiedad;

public interface PropiedadRepository extends JpaRepository<Propiedad, Long> {

	boolean existsByDireccionAndCiudadAndEliminadaFalse(String direccion, Ciudad ciudad);

	List<Propiedad> findByEliminadaFalse();

	List<Propiedad> findByEliminadaFalseAndEstadoDisponibilidad(EstadoDisponibilidad estadoDisponibilidad);

	Optional<Propiedad> findByIdAndEliminadaFalse(Long id);
}