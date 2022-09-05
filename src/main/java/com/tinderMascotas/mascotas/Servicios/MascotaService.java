package com.tinderMascotas.mascotas.Servicios;

import com.tinderMascotas.mascotas.Entity.Foto;
import com.tinderMascotas.mascotas.Entity.Mascota;
import com.tinderMascotas.mascotas.Entity.Usuario;
import com.tinderMascotas.mascotas.Enums.Genero;
import com.tinderMascotas.mascotas.Errors.ErrorService;
import com.tinderMascotas.mascotas.Respository.MascotaRepository;
import com.tinderMascotas.mascotas.Respository.UsuarioRepository;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MascotaService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private MascotaRepository mascotaRepository;
    
    @Autowired
    private FotoService fotoService;

    @Transactional
    public void agregarMascota(MultipartFile archivo, String idUsuario, String nombre, Genero genero) throws ErrorService, IOException {
        Usuario usaurio = usuarioRepository.findById(idUsuario).get();

        validar(nombre, genero);

        Mascota mascota = new Mascota();
        mascota.setNombre(nombre);
        mascota.setGenero(genero);
        mascota.setAlta(new Date());

        Foto foto = fotoService.guardar(archivo);
        mascota.setFoto(foto);
        
        mascotaRepository.save(mascota);
    }

    @Transactional
    public void modificar(MultipartFile archivo, String idUsuario, String idMascota, String nombre, Genero genero) throws ErrorService {
        validar(nombre, genero);

        Optional<Mascota> respuesta = mascotaRepository.findById(idMascota);
        if (respuesta.isPresent()) {
            Mascota mascota = respuesta.get();
            if (mascota.getUsuario().getId().equals(idUsuario)) {
                mascota.setNombre(nombre);
                mascota.setGenero(genero);
                
                String idFoto = null;
                if(mascota.getFoto() != null){
                    idFoto = mascota.getFoto().getId();
                }
                
                Foto foto = fotoService.actualizar(idFoto, archivo);
                mascota.setFoto(foto);
                
                mascotaRepository.save(mascota);
            } else {
                throw new ErrorService("No tiene permisos suficientes para realizar la operación");
            }
        } else {
            throw new ErrorService("No existe una mascota con el identificador solicitado");
        }
    }

    @Transactional
    public void eliminar(String idUsuario, String idMascota) throws ErrorService {
        Optional<Mascota> respuesta = mascotaRepository.findById(idMascota);
        if (respuesta.isPresent()) {
            Mascota mascota = respuesta.get();
            if (mascota.getUsuario().getId().equals(idUsuario)) {
                mascota.setBaja(new Date());
                mascotaRepository.save(mascota);
            }
        }
    }

    public void validar(String nombre, Genero genero) throws ErrorService {
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorService("El nombre de la mascota no puede ser nulo");
        }
        if (genero == null || genero.toString().isEmpty()) {
            throw new ErrorService("El genero de la mascota no puede ser nulo");
        }
    }
}
