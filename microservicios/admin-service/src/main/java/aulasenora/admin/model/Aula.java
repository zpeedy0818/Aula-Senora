package aulasenora.admin.model;

import jakarta.persistence.*;

@Entity
@Table(name = "aulas")
public class Aula {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, name = "nombre_aula") private String nombreAula;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombreAula() { return nombreAula; }
    public void setNombreAula(String nombreAula) { this.nombreAula = nombreAula; }
}
