package aulasenora.model;

import jakarta.persistence.*;

@Entity
@Table(name = "voluntarios")
public class Voluntario {

    @Id
    private Long id;

    @OneToOne(optional = false)
    @MapsId
    @JoinColumn(name = "id")
    private Usuario usuario;

    @Column(nullable = false)
    private String institution;

    @Column(nullable = false)
    private String skills;

    // Constructores
    public Voluntario() {}

    public Voluntario(Usuario usuario, String institution, String skills) {
        this.usuario = usuario;
        this.institution = institution;
        this.skills = skills;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }
}
