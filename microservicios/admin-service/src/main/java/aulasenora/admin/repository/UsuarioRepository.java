package aulasenora.admin.repository;

import aulasenora.admin.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    long countByRol(String rol);
    List<Usuario> findByRolNot(String rol);
}
