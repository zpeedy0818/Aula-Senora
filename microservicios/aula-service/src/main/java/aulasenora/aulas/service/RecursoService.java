package aulasenora.aulas.service;

import aulasenora.aulas.model.RecursoAula;
import aulasenora.aulas.repository.RecursoAulaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RecursoService {

    private final RecursoAulaRepository recursoRepository;

    public RecursoService(RecursoAulaRepository recursoRepository) {
        this.recursoRepository = recursoRepository;
    }

    public List<RecursoAula> listarPorAula(Long aulaId) {
        return recursoRepository.findByAula_IdOrderByFechaSubidaDesc(aulaId);
    }

    @Transactional
    public RecursoAula crearMetadata(RecursoAula recurso) {
        return recursoRepository.save(recurso);
    }

    @Transactional
    public void eliminar(Long recursoId) {
        RecursoAula recurso = recursoRepository.findById(recursoId)
                .orElseThrow(() -> new RuntimeException("Recurso no encontrado"));
        recursoRepository.delete(recurso);
    }
}
