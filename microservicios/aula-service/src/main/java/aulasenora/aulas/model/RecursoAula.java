package aulasenora.aulas.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "recursos_aula")
public class RecursoAula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "aula_id", nullable = false)
    private Aula aula;

    @Column(name = "voluntario_username", nullable = false, length = 100)
    private String voluntarioUsername;

    @Column(name = "nombre_original", nullable = false, length = 255)
    private String nombreOriginal;

    @Column(length = 300)
    private String descripcion;

    @Column(nullable = false, length = 20)
    private String tipo;

    @Column(name = "mime_type", length = 120)
    private String mimeType;

    @Column(name = "ruta_relativa", nullable = false, length = 500)
    private String rutaRelativa;

    @Column(name = "tamano_bytes")
    private Long tamanoBytes;

    @Column(name = "fecha_subida", nullable = false, updatable = false)
    private LocalDateTime fechaSubida;

    @PrePersist
    protected void onCreate() {
        this.fechaSubida = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Aula getAula() { return aula; }
    public void setAula(Aula aula) { this.aula = aula; }
    public String getVoluntarioUsername() { return voluntarioUsername; }
    public void setVoluntarioUsername(String voluntarioUsername) { this.voluntarioUsername = voluntarioUsername; }
    public String getNombreOriginal() { return nombreOriginal; }
    public void setNombreOriginal(String nombreOriginal) { this.nombreOriginal = nombreOriginal; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getMimeType() { return mimeType; }
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }
    public String getRutaRelativa() { return rutaRelativa; }
    public void setRutaRelativa(String rutaRelativa) { this.rutaRelativa = rutaRelativa; }
    public Long getTamanoBytes() { return tamanoBytes; }
    public void setTamanoBytes(Long tamanoBytes) { this.tamanoBytes = tamanoBytes; }
    public LocalDateTime getFechaSubida() { return fechaSubida; }
}
