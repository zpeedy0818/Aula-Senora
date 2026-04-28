package aulasenora.repository;

import aulasenora.model.HorarioDisponible;
import aulasenora.model.Voluntario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HorarioDisponibleRepository extends JpaRepository<HorarioDisponible, Long> {
    List<HorarioDisponible> findByVoluntario(Voluntario voluntario);
}
