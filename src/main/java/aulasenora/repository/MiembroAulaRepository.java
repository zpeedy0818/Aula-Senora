package aulasenora.repository;

import aulasenora.model.MiembroAula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MiembroAulaRepository extends JpaRepository<MiembroAula, Long> {
    List<MiembroAula> findByAulaId(Long aulaId);
    List<MiembroAula> findByUsuarioId(Long usuarioId);
    Optional<MiembroAula> findByAulaIdAndUsuarioId(Long aulaId, Long usuarioId);
    void deleteByAulaId(Long aulaId);
    void deleteByAulaIdAndUsuarioId(Long aulaId, Long usuarioId);
}
