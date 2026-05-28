package aulasenora.files.repository;

import aulasenora.files.model.Archivo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArchivoRepository extends JpaRepository<Archivo, Long> {
    Optional<Archivo> findByUuid(String uuid);
    boolean existsByUuid(String uuid);
}
