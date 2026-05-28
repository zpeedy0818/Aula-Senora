package aulasenora.admin.service;

import aulasenora.admin.model.Usuario;
import aulasenora.admin.model.Voluntario;
import aulasenora.admin.repository.AulaRepository;
import aulasenora.admin.repository.UsuarioRepository;
import aulasenora.admin.repository.VoluntarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    private final UsuarioRepository usuarioRepository;
    private final VoluntarioRepository voluntarioRepository;
    private final AulaRepository aulaRepository;

    public AdminService(UsuarioRepository usuarioRepository,
                        VoluntarioRepository voluntarioRepository,
                        AulaRepository aulaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.voluntarioRepository = voluntarioRepository;
        this.aulaRepository = aulaRepository;
    }

    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalEstudiantes", usuarioRepository.countByRol("ESTUDIANTE"));
        stats.put("totalVoluntarios", voluntarioRepository.count());
        stats.put("totalAulas", aulaRepository.count());
        stats.put("totalUsuarios", usuarioRepository.count());
        return stats;
    }

    public List<Usuario> listUsers() {
        return usuarioRepository.findByRolNot("ADMIN");
    }

    @Transactional
    public void toggleUserStatus(Long id) {
        usuarioRepository.findById(id).ifPresent(u -> {
            if (!"ADMIN".equals(u.getRol())) {
                u.setActivo(!u.isActivo());
                usuarioRepository.save(u);
            }
        });
    }

    @Transactional
    public void verifyVolunteer(Long id) {
        voluntarioRepository.findById(id).ifPresent(v -> {
            v.setVerificado(true);
            voluntarioRepository.save(v);
        });
    }
}
