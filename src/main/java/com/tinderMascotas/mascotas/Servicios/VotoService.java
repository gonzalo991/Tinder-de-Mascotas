package com.tinderMascotas.mascotas.Servicios;

import com.tinderMascotas.mascotas.Entity.Mascota;
import com.tinderMascotas.mascotas.Entity.Voto;
import com.tinderMascotas.mascotas.Errors.ErrorService;
import com.tinderMascotas.mascotas.Respository.MascotaRepository;
import com.tinderMascotas.mascotas.Respository.VotoRepository;
import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VotoService {

    @Autowired
    private MascotaRepository mascotaRepository;

    @Autowired
    VotoRepository votoRepository;

    public void votar(String idUsuario, String idMascota1, String idMascota2) throws ErrorService {
        Voto voto = new Voto();
        voto.setFecha(new Date());

        if (idMascota1.equals(idMascota2)) {
            throw new ErrorService("No puede votarse a sí mismo");
        }

        Optional<Mascota> respuesta = mascotaRepository.findById(idMascota1);
        if (respuesta.isPresent()) {
            Mascota mascota1 = respuesta.get();
            if (mascota1.getUsuario().getId().equals(idUsuario)) {
                voto.setMascota1(mascota1);
            } else {
                throw new ErrorService("No tiene permisos para realizar la operación solicitada");
            }
        } else {
            throw new ErrorService("No existe una mascota vinculada a ese identificador");
        }

        Optional<Mascota> respuesta2 = mascotaRepository.findById(idMascota2);
        if (respuesta2.isPresent()) {
            Mascota mascota2 = respuesta2.get();
            voto.setMascota2(mascota2);
        } else {
            throw new ErrorService("No existe una mascota vinculada a ese identificador");

        }

        votoRepository.save(voto);
    }

    public void responder(String idUsuario, String idVoto) throws ErrorService {
        Optional<Voto> respuesta = votoRepository.findById(idVoto);
        if (respuesta.isPresent()) {
            Voto voto = respuesta.get();
            voto.setRespuesta(new Date());

            if (voto.getMascota2().getUsuario().getId().equals(idUsuario)) {
                votoRepository.save(voto);
            }else {
                throw new ErrorService("No tiene permisos para realizar la operación");
            }
        } else {
            throw new ErrorService("No existe el voto solicitado");
        }
    }
}
