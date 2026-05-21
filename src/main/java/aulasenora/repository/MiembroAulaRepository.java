package aulasenora.repository;

import aulasenora.model.MiembroAula;
import aulasenora.model.Voluntario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MiembroAulaRepository extends JpaRepository<MiembroAula, Long> {
    List<MiembroAula> findByAulaId(Long aulaId);
    List<MiembroAula> findByUsuarioId(Long usuarioId);
    Optional<MiembroAula> findByAulaIdAndUsuarioId(Long aulaId, Long usuarioId);
    void deleteByAulaId(Long aulaId);
    void deleteByAulaIdAndUsuarioId(Long aulaId, Long usuarioId);

    @Query("SELECT COUNT(DISTINCT ma.usuario.id) FROM MiembroAula ma WHERE ma.aula.voluntario = :voluntario AND ma.rol = 'ESTUDIANTE'")
    long countDistinctStudentsByVoluntario(@Param("voluntario") Voluntario voluntario);
}
