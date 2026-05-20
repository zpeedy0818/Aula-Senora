package aulasenora.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import aulasenora.users.model.Voluntario;
import java.util.Optional;

public interface VoluntarioRepository extends JpaRepository<Voluntario, Long> {
    Optional<Voluntario> findByUsuario_Username(String username);
}
