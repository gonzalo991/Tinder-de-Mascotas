
package com.tinderMascotas.mascotas.Respository;

import com.tinderMascotas.mascotas.Entity.Foto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FotoRepository extends JpaRepository<Foto, String> {
    
}
