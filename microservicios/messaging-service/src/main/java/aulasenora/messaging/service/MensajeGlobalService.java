package aulasenora.messaging.service;

import aulasenora.messaging.model.MensajeGlobal;
import aulasenora.messaging.repository.MensajeGlobalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MensajeGlobalService {

    private final MensajeGlobalRepository mensajeGlobalRepository;

    public MensajeGlobalService(MensajeGlobalRepository mensajeGlobalRepository) {
        this.mensajeGlobalRepository = mensajeGlobalRepository;
    }

    public List<MensajeGlobal> listarActivos() {
        return mensajeGlobalRepository.findByActivoTrueOrderByFechaCreacionDesc();
    }

    public List<MensajeGlobal> listarTodos() {
        return mensajeGlobalRepository.findAll();
    }

    @Transactional
    public MensajeGlobal crear(String titulo, String contenido) {
        MensajeGlobal msg = new MensajeGlobal();
        msg.setTitulo(titulo);
        msg.setContenido(contenido);
        msg.setActivo(true);
        return mensajeGlobalRepository.save(msg);
    }

    @Transactional
    public MensajeGlobal actualizar(Long id, String titulo, String contenido, Boolean activo) {
        MensajeGlobal msg = mensajeGlobalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mensaje no encontrado"));
        if (titulo != null) msg.setTitulo(titulo);
        if (contenido != null) msg.setContenido(contenido);
        if (activo != null) msg.setActivo(activo);
        return mensajeGlobalRepository.save(msg);
    }

    @Transactional
    public void eliminar(Long id) {
        mensajeGlobalRepository.deleteById(id);
    }
}
