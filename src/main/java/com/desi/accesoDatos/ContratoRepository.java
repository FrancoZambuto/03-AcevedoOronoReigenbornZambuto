package com.desi.accesoDatos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.desi.entidades.Contrato;

public interface ContratoRepository extends JpaRepository<Contrato, Long> {

	@Query("SELECT COUNT(c) > 0 FROM Contrato c WHERE c.propiedad.id = :propiedadId AND c.estado = 'ACTIVO' AND c.eliminado = false")
	boolean tieneContratoActivo(@Param("propiedadId") Long propiedadId);
}
