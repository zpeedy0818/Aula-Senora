package aulasenora.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import aulasenora.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByUsernameIgnoreCaseOrEmailIgnoreCase(String username, String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    long countByRol(String rol);

    java.util.List<Usuario> findByRolNot(String rol);
}
