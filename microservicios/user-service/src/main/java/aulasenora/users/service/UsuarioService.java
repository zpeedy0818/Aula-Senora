package aulasenora.users.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import aulasenora.users.dto.RegistroDTO;
import aulasenora.users.dto.PerfilVoluntarioDTO;
import aulasenora.users.model.Usuario;
import aulasenora.users.model.Voluntario;
import aulasenora.users.repository.UsuarioRepository;
import aulasenora.users.repository.VoluntarioRepository;

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

        Usuario usuario = new Usuario();
        usuario.setUsername(dto.getUsername());
        usuario.setFirstName(dto.getFirstName().trim().toUpperCase());
        usuario.setLastName(dto.getLastName().trim().toUpperCase());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setProvider("LOCAL");
        
        String rol = (dto.getRol() == null || dto.getRol().equals("USER") || dto.getRol().isBlank()) ? "ESTUDIANTE" : dto.getRol();
        usuario.setRol(rol);
        usuario.setPerfilCompleto(true);

        Usuario savedUsuario = usuarioRepository.save(usuario);

        if ("VOLUNTARIO".equalsIgnoreCase(rol)) {
            Voluntario voluntario = new Voluntario();
            voluntario.setUsuario(savedUsuario);
            
            String institution = dto.getInstitution() != null && !dto.getInstitution().isBlank() 
                                 ? dto.getInstitution().trim().toUpperCase() : "NO ESPECIFICADO";
            String skills = dto.getSkills() != null && !dto.getSkills().isBlank() 
                            ? dto.getSkills().trim().toUpperCase() : "NO ESPECIFICADO";
            String materiaEspecializada = dto.getMateriaEspecializada() != null && !dto.getMateriaEspecializada().isBlank()
                                          ? dto.getMateriaEspecializada().trim() : "NO ESPECIFICADO";
                            
            voluntario.setInstitution(institution);
            voluntario.setSkills(skills);
            voluntario.setMateriaEspecializada(materiaEspecializada);
            voluntarioRepository.save(voluntario);
        }

        return savedUsuario;
    }

    public void actualizarPerfilVoluntario(String username, PerfilVoluntarioDTO dto) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                 
        if (!usuario.getEmail().equalsIgnoreCase(dto.getEmail()) && usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("El email ya está en uso por otra cuenta");
        }

        usuario.setFirstName(dto.getFirstName().trim().toUpperCase());
        usuario.setLastName(dto.getLastName().trim().toUpperCase());
        usuario.setEmail(dto.getEmail());
        usuarioRepository.save(usuario);

        if ("VOLUNTARIO".equalsIgnoreCase(usuario.getRol())) {
            Voluntario voluntario = voluntarioRepository.findByUsuario_Username(username)
                    .orElseThrow(() -> new RuntimeException("Voluntario no encontrado"));

            voluntario.setInstitution(dto.getInstitution() != null && !dto.getInstitution().isBlank() ? dto.getInstitution().trim().toUpperCase() : "NO ESPECIFICADO");
            voluntario.setSkills(dto.getSkills() != null && !dto.getSkills().isBlank() ? dto.getSkills().trim().toUpperCase() : "NO ESPECIFICADO");
            voluntario.setMateriaEspecializada(dto.getMateriaEspecializada() != null && !dto.getMateriaEspecializada().isBlank() ? dto.getMateriaEspecializada().trim() : "NO ESPECIFICADO");
            
            voluntarioRepository.save(voluntario);
        }
    }

    public void addTiempoAcumulado(String username, Long seconds) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Long current = usuario.getTiempoAcumulado() != null ? usuario.getTiempoAcumulado() : 0L;
        usuario.setTiempoAcumulado(current + seconds);
        usuarioRepository.save(usuario);
    }

    public Long getTiempoAcumulado(String username) {
        return usuarioRepository.findByUsername(username)
                .map(Usuario::getTiempoAcumulado)
                .orElse(0L);
    }
}
