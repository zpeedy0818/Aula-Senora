package aulasenora.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import aulasenora.model.Usuario;
import aulasenora.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario registrar(Usuario usuario) {
        if (usuario.getUsername() == null || usuario.getUsername().isBlank()) {
            throw new RuntimeException("El nombre de usuario es obligatorio");
        }
        if (usuario.getFirstName() == null || usuario.getFirstName().isBlank()) {
            throw new RuntimeException("El nombre es obligatorio");
        }
        if (usuario.getLastName() == null || usuario.getLastName().isBlank()) {
            throw new RuntimeException("El apellido es obligatorio");
        }
        if (usuario.getEmail() == null || usuario.getEmail().isBlank()) {
            throw new RuntimeException("El email es obligatorio");
        }
        if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
            throw new RuntimeException("La contraseña es obligatoria");
        }

        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            throw new RuntimeException("El username ya está en uso");
        }
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("El email ya está en uso");
        }

        // Asignar rol. Si ya viene con uno (desde el controller), se respeta.
        if (usuario.getRol() == null || usuario.getRol().equals("USER")) {
            usuario.setRol("ESTUDIANTE");
        }

        // Encriptar la contraseña antes de guardar
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }
}