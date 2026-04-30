package aulasenora.service;

import aulasenora.model.Aula;
import aulasenora.model.HorarioAula;
import aulasenora.model.HorarioDisponible;
import aulasenora.model.SolicitudCupo;
import aulasenora.model.SolicitudHorarioAula;
import aulasenora.model.Voluntario;
import aulasenora.repository.AulaRepository;
import aulasenora.repository.HorarioAulaRepository;
import aulasenora.repository.HorarioDisponibleRepository;
import aulasenora.repository.SolicitudCupoRepository;
import aulasenora.repository.SolicitudHorarioAulaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MigrationService {

    private static final Logger logger = LoggerFactory.getLogger(MigrationService.class);

    private final HorarioDisponibleRepository oldHorarioRepository;
    private final SolicitudCupoRepository oldSolicitudRepository;
    private final HorarioAulaRepository newHorarioRepository;
    private final SolicitudHorarioAulaRepository newSolicitudRepository;
    private final AulaRepository aulaRepository;

    public MigrationService(HorarioDisponibleRepository oldHorarioRepository,
                            SolicitudCupoRepository oldSolicitudRepository,
                            HorarioAulaRepository newHorarioRepository,
                            SolicitudHorarioAulaRepository newSolicitudRepository,
                            AulaRepository aulaRepository) {
        this.oldHorarioRepository = oldHorarioRepository;
        this.oldSolicitudRepository = oldSolicitudRepository;
        this.newHorarioRepository = newHorarioRepository;
        this.newSolicitudRepository = newSolicitudRepository;
        this.aulaRepository = aulaRepository;
    }

    @Transactional
    public void migrateOldSchedulesToAulas() {
        logger.info("Iniciando migración de horarios antiguos a Aulas...");

        List<HorarioDisponible> oldHorarios = oldHorarioRepository.findAll();
        for (HorarioDisponible oldHorario : oldHorarios) {
            Voluntario voluntario = oldHorario.getVoluntario();
            List<Aula> aulasVoluntario = aulaRepository.findByVoluntarioId(voluntario.getId());

            Aula targetAula = null;
            if (aulasVoluntario.isEmpty()) {
                // Auto-create a default Aula if the volunteer has none
                targetAula = new Aula();
                targetAula.setVoluntario(voluntario);
                targetAula.setNombreAula("Aula General de " + voluntario.getUsuario().getFirstName());
                targetAula.setDescripcion("Aula generada automáticamente durante la migración de horarios.");
                targetAula = aulaRepository.save(targetAula);
                logger.info("Creada Aula por defecto para voluntario: {}", voluntario.getUsuario().getUsername());
            } else {
                // Assign to the first available Aula for this volunteer
                targetAula = aulasVoluntario.get(0);
            }

            // Create new HorarioAula
            HorarioAula newHorario = new HorarioAula(
                    targetAula,
                    oldHorario.getDiaSemana(),
                    oldHorario.getHoraInicio(),
                    oldHorario.getHoraFin(),
                    oldHorario.getMateria()
            );
            newHorario = newHorarioRepository.save(newHorario);

            // Migrate pending/accepted requests
            List<SolicitudCupo> oldSolicitudes = oldSolicitudRepository.findByHorario(oldHorario);
            for (SolicitudCupo oldSolicitud : oldSolicitudes) {
                SolicitudHorarioAula newSolicitud = new SolicitudHorarioAula(oldSolicitud.getEstudiante(), newHorario);
                newSolicitud.setFechaSolicitud(oldSolicitud.getFechaSolicitud() != null ? oldSolicitud.getFechaSolicitud() : LocalDateTime.now());
                newSolicitud.setMensaje(oldSolicitud.getMensaje());
                
                String state = oldSolicitud.getEstado();
                if ("ACEPTADA".equals(state)) {
                    newSolicitud.setEstado("ACEPTADA");
                    newHorario.setEstado("OCUPADO");
                } else if ("RECHAZADA".equals(state)) {
                    newSolicitud.setEstado("RECHAZADA");
                } else {
                    newSolicitud.setEstado("PENDIENTE");
                    if (!"OCUPADO".equals(newHorario.getEstado())) {
                        newHorario.setEstado("PENDIENTE");
                    }
                }
                
                newHorarioRepository.save(newHorario);
                newSolicitudRepository.save(newSolicitud);
            }
            logger.info("Migrado horario {} al aula {}", oldHorario.getId(), targetAula.getId());
        }
        logger.info("Migración completada exitosamente.");
    }
}
