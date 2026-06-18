package com.desi.accesoDatos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.desi.entidades.Contrato;
import com.desi.entidades.EstadoContrato;
import com.desi.entidades.Propiedad;

public interface ContratoRepository extends JpaRepository<Contrato, Long> {

    boolean existsByPropiedadAndEstadoAndEliminadoFalse(Propiedad propiedad, EstadoContrato estado);

    Optional<Contrato> findByIdAndEliminadoFalse(Long id);
}