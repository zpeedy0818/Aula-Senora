package aulasenora.repository;

import aulasenora.model.HorarioDisponible;
import aulasenora.model.SolicitudCupo;
import aulasenora.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudCupoRepository extends JpaRepository<SolicitudCupo, Long> {
    List<SolicitudCupo> findByEstudiante(Usuario estudiante);
    List<SolicitudCupo> findByHorario(HorarioDisponible horario);
    List<SolicitudCupo> findByHorarioIn(List<HorarioDisponible> horarios);
    List<SolicitudCupo> findByHorario_Voluntario(aulasenora.model.Voluntario voluntario);
}
