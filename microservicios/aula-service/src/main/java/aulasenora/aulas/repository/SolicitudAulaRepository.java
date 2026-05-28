package aulasenora.aulas.repository;

import aulasenora.aulas.model.SolicitudAula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SolicitudAulaRepository extends JpaRepository<SolicitudAula, Long> {
    List<SolicitudAula> findByAulaId(Long aulaId);
    List<SolicitudAula> findByEstudianteId(Long estudianteId);
    Optional<SolicitudAula> findByAulaIdAndEstudianteId(Long aulaId, Long estudianteId);
    List<SolicitudAula> findByAulaIdAndEstado(Long aulaId, String estado);
    boolean existsByAulaIdAndEstudianteIdAndEstado(Long aulaId, Long estudianteId, String estado);
    List<SolicitudAula> findByEstudiante_Username(String username);
}
