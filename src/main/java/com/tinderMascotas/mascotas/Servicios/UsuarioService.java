package com.tinderMascotas.mascotas.Servicios;

import com.tinderMascotas.mascotas.Entity.Foto;
import com.tinderMascotas.mascotas.Entity.Usuario;
import com.tinderMascotas.mascotas.Errors.ErrorService;
import com.tinderMascotas.mascotas.Respository.UsuarioRepository;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private FotoService fotoService;
    
    public void registrar(MultipartFile archivo, String nombre, String apellido, String mail, String clave) throws ErrorService, IOException {
        
        validar(nombre, apellido, mail, clave);
        
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setMail(mail);
        usuario.setClave(clave);
        usuario.setAlta(new Date());
        
        Foto foto = fotoService.guardar(archivo);
        usuario.setFoto(foto);
        
        usuarioRepository.save(usuario);
        
    }
    
    public void modificar(MultipartFile archivo, String id, String nombre, String apellido, String mail, String clave) throws ErrorService {
        
        validar(nombre, apellido, mail, clave);
        
        Optional<Usuario> respuesta = usuarioRepository.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setMail(mail);
            usuario.setClave(clave);
            
            String idFoto = null;
            if (usuario.getFoto() != null) {
                idFoto = usuario.getFoto().getId();
            }
            Foto foto = fotoService.actualizar(idFoto, archivo);
            usuario.setFoto(foto);
            usuarioRepository.save(usuario);
        } else {
            throw new ErrorService("No se encontró el usuario solicitado");
        }
    }
    
    public void deshabilitar(String id) throws ErrorService {
        Optional<Usuario> respuesta = usuarioRepository.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setBaja(new Date());
            usuarioRepository.save(usuario);
        } else {
            throw new ErrorService("No se encontró el usuario");
        }
    }
    
    public void habilitar(String id) throws ErrorService {
        Optional<Usuario> respuesta = usuarioRepository.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setBaja(null);
            usuarioRepository.save(usuario);
        } else {
            throw new ErrorService("No se encontró el usuario");
        }
    }
    
    public void validar(String nombre, String apellido, String mail, String clave) throws ErrorService {
        
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorService("El nombre del Usuario no puede ser nulo");
        }
        
        if (apellido == null || apellido.isEmpty()) {
            throw new ErrorService("El mail no puede ser nulo");
        }
        
        if (mail == null || mail.isEmpty()) {
            throw new ErrorService("El e-mail no puede ser nulo");
        }
        
        if (clave == null || clave.isEmpty() || clave.length() <= 6) {
            throw new ErrorService("La clave no puede ser nula o menor a 6 caracteres");
        }
    }
}
