package com.desi.accesoDatos;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.desi.entidades.Contrato;

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
}