package aulasenora.aulas.repository;

import aulasenora.aulas.model.HorarioDisponible;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface HorarioDisponibleRepository extends JpaRepository<HorarioDisponible, Long> {
    List<HorarioDisponible> findByVoluntarioId(Long voluntarioId);
    List<HorarioDisponible> findByFecha(LocalDate fecha);
    List<HorarioDisponible> findByVoluntarioIdAndFechaBetween(Long voluntarioId, LocalDate start, LocalDate end);
}
