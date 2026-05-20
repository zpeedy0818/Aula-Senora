package aulasenora.users.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import aulasenora.users.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByUsernameIgnoreCaseOrEmailIgnoreCase(String username, String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    long countByRol(String rol);
}
