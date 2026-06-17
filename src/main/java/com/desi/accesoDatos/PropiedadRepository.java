package com.desi.accesoDatos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.desi.entidades.Ciudad;
import com.desi.entidades.Propiedad;

public interface PropiedadRepository extends JpaRepository<Propiedad, Long> {

	boolean existsByDireccionAndCiudadAndEliminadaFalse(String direccion, Ciudad ciudad);
}
