package aulasenora.repository;

import aulasenora.model.HorarioAula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HorarioAulaRepository extends JpaRepository<HorarioAula, Long> {
    List<HorarioAula> findByAula_Id(Long aulaId);
}
