package aulasenora.repository;

import aulasenora.model.SolicitudAula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolicitudAulaRepository extends JpaRepository<SolicitudAula, Long> {
    List<SolicitudAula> findByAulaId(Long aulaId);
    List<SolicitudAula> findByAulaIdAndEstado(Long aulaId, String estado);
    List<SolicitudAula> findByEstudianteId(Long estudianteId);
    Optional<SolicitudAula> findByAulaIdAndEstudianteId(Long aulaId, Long estudianteId);
}
