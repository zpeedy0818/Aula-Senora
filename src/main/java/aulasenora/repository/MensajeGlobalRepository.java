package aulasenora.repository;

import aulasenora.model.MensajeGlobal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MensajeGlobalRepository extends JpaRepository<MensajeGlobal, Long> {
    List<MensajeGlobal> findByActivoTrueOrderByFechaCreacionDesc();
}
