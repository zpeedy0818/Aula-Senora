package aulasenora.aulas.repository;

import aulasenora.aulas.model.Aula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AulaRepository extends JpaRepository<Aula, Long> {
    List<Aula> findByVoluntario_Id(Long voluntarioId);
    List<Aula> findByNombreAulaContainingIgnoreCase(String nombre);
}
