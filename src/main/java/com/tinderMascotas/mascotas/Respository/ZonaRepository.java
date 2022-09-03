package com.tinderMascotas.mascotas.Respository;

import com.tinderMascotas.mascotas.Entity.Zona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZonaRepository extends JpaRepository<Zona, String> {
    
}
