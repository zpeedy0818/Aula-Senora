package aulasenora.aulas.repository;

import aulasenora.aulas.model.HorarioAula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface HorarioAulaRepository extends JpaRepository<HorarioAula, Long> {
    List<HorarioAula> findByAulaId(Long aulaId);
    List<HorarioAula> findByAulaIdAndFechaBetween(Long aulaId, LocalDate start, LocalDate end);
    List<HorarioAula> findByAulaIdAndEstado(Long aulaId, String estado);
    List<HorarioAula> findByFecha(LocalDate fecha);
}
