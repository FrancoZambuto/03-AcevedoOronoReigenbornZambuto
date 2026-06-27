package com.desi.accesoDatos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.desi.entidades.Contrato;
import com.desi.entidades.EstadoContrato;
import com.desi.entidades.Factura;

public interface FacturaRepository extends JpaRepository<Factura, Long> {

    @Query("""
        SELECT f
        FROM Factura f
        WHERE f.id = :id
          AND f.eliminada = false
    """)
    Optional<Factura> buscarPorIdNoEliminada(@Param("id") Long id);

    @Query("""
        SELECT c
        FROM Contrato c
        WHERE c.eliminado = false
          AND c.estado = :estado
    """)
    List<Contrato> listarContratosActivos(@Param("estado") EstadoContrato estado);
}