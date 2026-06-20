package com.desi.accesoDatos;

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
}
