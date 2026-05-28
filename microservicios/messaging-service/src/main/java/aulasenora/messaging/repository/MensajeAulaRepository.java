package aulasenora.messaging.repository;

import aulasenora.messaging.model.MensajeAula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MensajeAulaRepository extends JpaRepository<MensajeAula, Long> {
    List<MensajeAula> findByAulaIdOrderByFechaEnvioAsc(Long aulaId);
}
