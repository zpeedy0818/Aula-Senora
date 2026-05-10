package aulasenora.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "horarios_aula")
public class HorarioAula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "aula_id", nullable = false)
    private Aula aula;

    @Column(name = "dia_semana", nullable = true)
    private String diaSemana; // Deprecated, kept only to allow Hibernate to drop the NOT NULL constraint in DB

    @Column(nullable = false, columnDefinition = "date default CURRENT_DATE")
    private LocalDate fecha;

    @Column(nullable = false, name = "hora_inicio")
    private LocalTime horaInicio;

    @Column(nullable = false, name = "hora_fin")
    private LocalTime horaFin;

    @Column(nullable = false)
    private String materia;

    @Column(nullable = false)
    private String estado = "DISPONIBLE"; // DISPONIBLE, PENDIENTE, OCUPADO

    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean esGrupal = false;

    @Version
    private Long version;

    public HorarioAula() {}

    public HorarioAula(Aula aula, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, String materia, Boolean esGrupal) {
        this.aula = aula;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.materia = materia;
        this.estado = "DISPONIBLE";
        this.esGrupal = esGrupal != null ? esGrupal : false;
        this.diaSemana = "N/A"; // Valor por defecto para la base de datos
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Aula getAula() {
        return aula;
    }

    public void setAula(Aula aula) {
        this.aula = aula;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Boolean getEsGrupal() {
        return esGrupal;
    }

    public void setEsGrupal(Boolean esGrupal) {
        this.esGrupal = esGrupal;
    }
}
