package aulasenora.messaging.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "aulas")
public class Aula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "codigo_voluntario", referencedColumnName = "id")
    private Voluntario voluntario;

    public Aula() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Voluntario getVoluntario() { return voluntario; }
    public void setVoluntario(Voluntario voluntario) { this.voluntario = voluntario; }
}
