package com.desi.accesoDatos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.desi.entidades.Ciudad;
import com.desi.entidades.EstadoDisponibilidad;
import com.desi.entidades.Propiedad;
import com.desi.entidades.TipoPropiedad;

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

	@Query("""
		SELECT p
		FROM Propiedad p
		WHERE p.eliminada = false
		  AND (:direccion IS NULL OR :direccion = '' OR LOWER(p.direccion) LIKE LOWER(CONCAT('%', :direccion, '%')))
		  AND (:ciudadId IS NULL OR p.ciudad.id = :ciudadId)
		  AND (:tipo IS NULL OR p.tipo = :tipo)
		  AND (:estado IS NULL OR p.estadoDisponibilidad = :estado)
		ORDER BY p.id
	""")
	List<Propiedad> filtrarActivas(
			@Param("direccion") String direccion,
			@Param("ciudadId") Long ciudadId,
			@Param("tipo") TipoPropiedad tipo,
			@Param("estado") EstadoDisponibilidad estado);
}