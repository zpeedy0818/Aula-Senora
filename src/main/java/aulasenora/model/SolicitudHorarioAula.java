package aulasenora.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "solicitudes_horario_aula")
public class SolicitudHorarioAula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Usuario estudiante;

    @ManyToOne(optional = false)
    @JoinColumn(name = "horario_aula_id", nullable = false)
    private HorarioAula horarioAula;

    @Column(nullable = false)
    private String estado = "PENDIENTE"; // PENDIENTE, ACEPTADA, RECHAZADA

    @Column(nullable = false, name = "fecha_solicitud")
    private LocalDateTime fechaSolicitud = LocalDateTime.now();

    @Column(columnDefinition = "TEXT")
    private String mensaje;

    public SolicitudHorarioAula() {}

    public SolicitudHorarioAula(Usuario estudiante, HorarioAula horarioAula) {
        this.estudiante = estudiante;
        this.horarioAula = horarioAula;
        this.estado = "PENDIENTE";
        this.fechaSolicitud = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Usuario estudiante) {
        this.estudiante = estudiante;
    }

    public HorarioAula getHorarioAula() {
        return horarioAula;
    }

    public void setHorarioAula(HorarioAula horarioAula) {
        this.horarioAula = horarioAula;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(LocalDateTime fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
