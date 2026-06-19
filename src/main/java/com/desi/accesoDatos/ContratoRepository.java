package com.desi.accesoDatos;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.desi.entidades.Contrato;
import com.desi.entidades.EstadoContrato;

public interface ContratoRepository extends JpaRepository<Contrato, Long> {

    @Query("""
		SELECT COUNT(c) > 0
		FROM Contrato c
		WHERE c.propiedad.id = :propiedadId
		  AND c.estado = com.desi.entidades.EstadoContrato.ACTIVO
		  AND c.eliminado = false
	""")
    boolean tieneContratoActivo(@Param("propiedadId") Long propiedadId);

    @Query("""
		SELECT c
		FROM Contrato c
		WHERE c.id = :id
		  AND c.eliminado = false
	""")
    Optional<Contrato> buscarPorIdNoEliminado(@Param("id") Long id);

    @Query("""
		SELECT c
		FROM Contrato c
		WHERE c.eliminado = false
	""")
    List<Contrato> listarActivos();

    @Query("""
		SELECT c
		FROM Contrato c
		WHERE c.eliminado = false
		  AND (:propiedadId IS NULL OR c.propiedad.id = :propiedadId)
		  AND (:inquilinoId IS NULL OR c.inquilino.id = :inquilinoId)
		  AND (:estado IS NULL OR c.estado = :estado)
		  AND (:fechaInicio IS NULL OR c.fechaInicio = :fechaInicio)
	""")
    List<Contrato> filtrar(
        @Param("propiedadId") Long propiedadId,
        @Param("inquilinoId") Long inquilinoId,
        @Param("estado") EstadoContrato estado,
        @Param("fechaInicio") LocalDate fechaInicio
    );
}