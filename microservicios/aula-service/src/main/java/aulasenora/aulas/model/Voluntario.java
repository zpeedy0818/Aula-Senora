package aulasenora.aulas.model;

import jakarta.persistence.*;

@Entity
@Table(name = "voluntarios")
public class Voluntario {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private Usuario usuario;

    @Column(name = "materia_especializada", length = 100)
    private String materiaEspecializada;

    public Voluntario() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public String getMateriaEspecializada() { return materiaEspecializada; }
    public void setMateriaEspecializada(String materiaEspecializada) { this.materiaEspecializada = materiaEspecializada; }
}
