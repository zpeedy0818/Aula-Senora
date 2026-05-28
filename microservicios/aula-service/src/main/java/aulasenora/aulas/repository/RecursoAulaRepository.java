package aulasenora.aulas.repository;

import aulasenora.aulas.model.RecursoAula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecursoAulaRepository extends JpaRepository<RecursoAula, Long> {
    List<RecursoAula> findByAula_IdOrderByFechaSubidaDesc(Long aulaId);
}
