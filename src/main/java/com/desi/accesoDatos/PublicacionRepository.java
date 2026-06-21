package com.desi.accesoDatos;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.desi.entidades.EstadoPublicacion;
import com.desi.entidades.Publicacion;

public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {

	@Query("""
		SELECT COUNT(p) > 0
		FROM Publicacion p
		WHERE p.propiedad.id = :propiedadId
		  AND p.estado = :estado
		  AND p.eliminada = false
	""")
	boolean existePublicacionConEstadoParaPropiedad(
			@Param("propiedadId") Long propiedadId,
			@Param("estado") EstadoPublicacion estado);

	@Query("""
		SELECT p
		FROM Publicacion p
		WHERE p.eliminada = false
		ORDER BY p.id
	""")
	List<Publicacion> listarNoEliminadas();

	@Query("""
		SELECT p
		FROM Publicacion p
		WHERE p.id = :id
		  AND p.eliminada = false
	""")
	Optional<Publicacion> buscarPorIdNoEliminada(@Param("id") Long id);

	@Query("""
		SELECT p
		FROM Publicacion p
		WHERE p.eliminada = false
		  AND (:propiedadId IS NULL OR p.propiedad.id = :propiedadId)
		  AND (:ciudadId IS NULL OR p.propiedad.ciudad.id = :ciudadId)
		  AND (:estado IS NULL OR p.estado = :estado)
		  AND (:precioMin IS NULL OR p.precioMensual >= :precioMin)
		  AND (:precioMax IS NULL OR p.precioMensual <= :precioMax)
		ORDER BY p.id
	""")
	List<Publicacion> filtrar(
			@Param("propiedadId") Long propiedadId,
			@Param("ciudadId") Long ciudadId,
			@Param("estado") EstadoPublicacion estado,
			@Param("precioMin") BigDecimal precioMin,
			@Param("precioMax") BigDecimal precioMax);
}
