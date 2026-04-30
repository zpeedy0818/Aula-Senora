package aulasenora.repository;

import aulasenora.model.SolicitudHorarioAula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolicitudHorarioAulaRepository extends JpaRepository<SolicitudHorarioAula, Long> {
    List<SolicitudHorarioAula> findByHorarioAula_Aula_Id(Long aulaId);
    List<SolicitudHorarioAula> findByHorarioAula_Id(Long horarioAulaId);
    List<SolicitudHorarioAula> findByEstudiante_Username(String username);
    Optional<SolicitudHorarioAula> findByHorarioAula_IdAndEstudiante_Username(Long horarioAulaId, String username);
}
