package aulasenora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import aulasenora.model.Voluntario;

public interface VoluntarioRepository extends JpaRepository<Voluntario, Long> {
    java.util.Optional<Voluntario> findByUsuario_Username(String username);
}
