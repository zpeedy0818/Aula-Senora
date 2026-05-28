package aulasenora.admin.repository;

import aulasenora.admin.model.Aula;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AulaRepository extends JpaRepository<Aula, Long> {
    long count();
}
