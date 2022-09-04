package com.tinderMascotas.mascotas.Servicios;

import com.tinderMascotas.mascotas.Entity.Foto;
import com.tinderMascotas.mascotas.Entity.Usuario;
import com.tinderMascotas.mascotas.Errors.ErrorService;
import com.tinderMascotas.mascotas.Respository.UsuarioRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioService implements UserDetailsService {

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

        String encClave = new BCryptPasswordEncoder().encode(clave);
        usuario.setClave(encClave);
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
            String encClave = new BCryptPasswordEncoder().encode(clave);
            usuario.setClave(encClave);

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

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.buscarPorMail(mail);
        if (usuario != null) {

            List<String> permisos = new ArrayList<>();
            GrantedAuthority p1 = new SimpleGrantedAuthority("MODULO_FOTOS");
            permisos.add(p1.toString());

            GrantedAuthority p2 = new SimpleGrantedAuthority("MODULO_MASCOTAS");
            permisos.add(p2.toString());

            GrantedAuthority p3 = new SimpleGrantedAuthority("MODULO_VOTOS");
            permisos.add(p3.toString());

            User user = new User();
            user.setName(usuario.getMail());
            user.setPassword(usuario.getClave());
            user.setRoles(permisos);

            return (UserDetails) user;
        } else {
            return null;
        }
    }
}
