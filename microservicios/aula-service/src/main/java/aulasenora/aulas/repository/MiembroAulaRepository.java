package aulasenora.aulas.repository;

import aulasenora.aulas.model.MiembroAula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MiembroAulaRepository extends JpaRepository<MiembroAula, Long> {
    List<MiembroAula> findByAulaId(Long aulaId);
    List<MiembroAula> findByUsuarioId(Long usuarioId);
    Optional<MiembroAula> findByAulaIdAndUsuarioId(Long aulaId, Long usuarioId);
    boolean existsByAulaIdAndUsuarioId(Long aulaId, Long usuarioId);
    long countByAulaId(Long aulaId);
}
