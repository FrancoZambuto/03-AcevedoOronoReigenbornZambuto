package com.desi.accesoDatos;

import com.desi.entidades.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IPersonaRepo extends JpaRepository<Persona, Long> {

    boolean existsByDniCuit(String dniCuit);

    boolean existsByDniCuitAndIdNot(String dniCuit, Long id);

    @Query("""
			SELECT p FROM Persona p
			WHERE (:nombre IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))
			  AND (:dniCuit IS NULL OR p.dniCuit = :dniCuit)
			  AND (:idCiudad IS NULL OR p.ciudad.id = :idCiudad)
			""")
    List<Persona> filter(
            @Param("nombre") String nombre,
            @Param("dniCuit") String dniCuit,
            @Param("idCiudad") Long idCiudad);
}
