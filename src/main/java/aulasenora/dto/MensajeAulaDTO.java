package aulasenora.dto;

import java.time.LocalDateTime;

public class MensajeAulaDTO {

    private Long id;
    private String contenido;
    private String remitenteNombre;
    private Long remitenteId;
    private String remitenteUsername;
    private String remitenteRol; // e.g. "VOLUNTARIO" or "ESTUDIANTE"
    private LocalDateTime fechaEnvio;

    public MensajeAulaDTO() {}

    public MensajeAulaDTO(Long id, String contenido, String remitenteNombre, Long remitenteId, String remitenteUsername, String remitenteRol, LocalDateTime fechaEnvio) {
        this.id = id;
        this.contenido = contenido;
        this.remitenteNombre = remitenteNombre;
        this.remitenteId = remitenteId;
        this.remitenteUsername = remitenteUsername;
        this.remitenteRol = remitenteRol;
        this.fechaEnvio = fechaEnvio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getRemitenteNombre() {
        return remitenteNombre;
    }

    public void setRemitenteNombre(String remitenteNombre) {
        this.remitenteNombre = remitenteNombre;
    }

    public Long getRemitenteId() {
        return remitenteId;
    }

    public void setRemitenteId(Long remitenteId) {
        this.remitenteId = remitenteId;
    }

    public String getRemitenteUsername() {
        return remitenteUsername;
    }

    public void setRemitenteUsername(String remitenteUsername) {
        this.remitenteUsername = remitenteUsername;
    }

    public String getRemitenteRol() {
        return remitenteRol;
    }

    public void setRemitenteRol(String remitenteRol) {
        this.remitenteRol = remitenteRol;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }
}
