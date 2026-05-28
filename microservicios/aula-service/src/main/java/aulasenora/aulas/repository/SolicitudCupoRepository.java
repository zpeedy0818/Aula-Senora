package aulasenora.aulas.repository;

import aulasenora.aulas.model.SolicitudCupo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitudCupoRepository extends JpaRepository<SolicitudCupo, Long> {
    List<SolicitudCupo> findByEstudianteId(Long estudianteId);
    List<SolicitudCupo> findByHorarioId(Long horarioId);
    List<SolicitudCupo> findByHorario_VoluntarioId(Long voluntarioId);
}
