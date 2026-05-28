package aulasenora.messaging.repository;

import aulasenora.messaging.model.Aula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AulaRepository extends JpaRepository<Aula, Long> {
}
