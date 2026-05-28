package aulasenora.aulas.repository;

import aulasenora.aulas.model.SolicitudHorarioAula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitudHorarioAulaRepository extends JpaRepository<SolicitudHorarioAula, Long> {
    List<SolicitudHorarioAula> findByEstudianteId(Long estudianteId);
    List<SolicitudHorarioAula> findByHorarioAula_Aula_Id(Long aulaId);
    List<SolicitudHorarioAula> findByHorarioAulaId(Long horarioAulaId);
}
