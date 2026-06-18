package com.desi.accesoDatos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.desi.entidades.Ciudad;
import com.desi.entidades.EstadoDisponibilidad;
import com.desi.entidades.Propiedad;

public interface PropiedadRepository extends JpaRepository<Propiedad, Long> {

	@Query("""
		SELECT COUNT(p) > 0
		FROM Propiedad p
		WHERE p.direccion = :direccion
		  AND p.ciudad = :ciudad
		  AND p.eliminada = false
	""")
	boolean existeActivaPorDireccionYCiudad(
			@Param("direccion") String direccion,
			@Param("ciudad") Ciudad ciudad
	);

	@Query("""
		SELECT p
		FROM Propiedad p
		WHERE p.eliminada = false
		  AND p.estadoDisponibilidad = :estadoDisponibilidad
	""")
	List<Propiedad> listarPorEstadoDisponibilidadNoEliminadas(
			@Param("estadoDisponibilidad") EstadoDisponibilidad estadoDisponibilidad
	);

	@Query("""
		SELECT p
		FROM Propiedad p
		WHERE p.id = :id
		  AND p.eliminada = false
	""")
	Optional<Propiedad> buscarPorIdNoEliminada(@Param("id") Long id);

	@Query("""
		SELECT p
		FROM Propiedad p
		WHERE p.eliminada = false
	""")
	List<Propiedad> listarActivas();
}