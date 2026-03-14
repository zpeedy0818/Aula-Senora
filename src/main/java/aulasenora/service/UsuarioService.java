package aulasenora.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import aulasenora.dto.RegistroDTO;
import aulasenora.model.Usuario;
import aulasenora.model.Voluntario;
import aulasenora.repository.UsuarioRepository;
import aulasenora.repository.VoluntarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final VoluntarioRepository voluntarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, VoluntarioRepository voluntarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.voluntarioRepository = voluntarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario registrar(RegistroDTO dto) {
        if (dto.getUsername() == null || dto.getUsername().isBlank()) {
            throw new RuntimeException("El nombre de usuario es obligatorio");
        }
        if (dto.getFirstName() == null || dto.getFirstName().isBlank()) {
            throw new RuntimeException("El nombre es obligatorio");
        }
        if (dto.getLastName() == null || dto.getLastName().isBlank()) {
            throw new RuntimeException("El apellido es obligatorio");
        }
        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new RuntimeException("El email es obligatorio");
        }
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new RuntimeException("La contraseña es obligatoria");
        }

        if (usuarioRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("El username ya está en uso");
        }
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("El email ya está en uso");
        }

        // 1. Crear el Usuario base
        Usuario usuario = new Usuario();
        usuario.setUsername(dto.getUsername());
        usuario.setFirstName(dto.getFirstName().trim().toUpperCase());
        usuario.setLastName(dto.getLastName().trim().toUpperCase());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        
        // Asignar rol. Si ya viene con uno, se respeta.
        String rol = (dto.getRol() == null || dto.getRol().equals("USER") || dto.getRol().isBlank()) ? "ESTUDIANTE" : dto.getRol();
        usuario.setRol(rol);

        // Guardar el usuario (necesario para tener ID antes de asociar al voluntario)
        Usuario savedUsuario = usuarioRepository.save(usuario);

        // 2. Si es voluntario, crear los datos adicionales en la otra tabla
        if ("VOLUNTARIO".equalsIgnoreCase(rol)) {
            Voluntario voluntario = new Voluntario();
            voluntario.setUsuario(savedUsuario);
            
            String institution = dto.getInstitution() != null && !dto.getInstitution().isBlank() 
                                 ? dto.getInstitution().trim().toUpperCase() : "NO ESPECIFICADO";
            String skills = dto.getSkills() != null && !dto.getSkills().isBlank() 
                            ? dto.getSkills().trim().toUpperCase() : "NO ESPECIFICADO";
                            
            voluntario.setInstitution(institution);
            voluntario.setSkills(skills);
            voluntarioRepository.save(voluntario);
        }

        return savedUsuario;
    }
}