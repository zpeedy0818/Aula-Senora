package aulasenora.admin.model;

import jakarta.persistence.*;

@Entity
@Table(name = "voluntarios")
public class Voluntario {
    @Id
    private Long id;
    private Boolean verificado = false;
    @Column(name = "diploma_url", length = 500) private String diplomaUrl;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Boolean isVerificado() { return verificado != null && verificado; }
    public void setVerificado(Boolean verificado) { this.verificado = verificado; }
    public String getDiplomaUrl() { return diplomaUrl; }
    public void setDiplomaUrl(String diplomaUrl) { this.diplomaUrl = diplomaUrl; }
}
