package aulasenora.admin.repository;

import aulasenora.admin.model.Voluntario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoluntarioRepository extends JpaRepository<Voluntario, Long> {
    long count();
}
