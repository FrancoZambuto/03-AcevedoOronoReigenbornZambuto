package com.desi.accesoDatos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.desi.entidades.Ciudad;

public interface CiudadRepository extends JpaRepository<Ciudad, Long> {
}
