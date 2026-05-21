package aulasenora.repository;

import aulasenora.model.RecursoAula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecursoAulaRepository extends JpaRepository<RecursoAula, Long> {
    List<RecursoAula> findByAula_IdOrderByFechaSubidaDesc(Long aulaId);
    void deleteByAula_Id(Long aulaId);
}
