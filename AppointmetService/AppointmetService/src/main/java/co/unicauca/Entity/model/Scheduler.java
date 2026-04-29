package co.unicauca.Entity.model;

/**
 * @brief Clase para mapear un agendador, dado que este es el microservicio de Appointment
 * no guarda toda la informacion de un agendador, solo su id y su nombre, que debe ser
 * sincronizado con la BD del microservicio correspondiente a traves de una suscripcion al user service
 *
 */
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "scheduler")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Scheduler {
    @Id
    private long id;
    @Column(name = "sch_name", nullable = false)
    private String schedulerName;
    @JsonIgnore
    @OneToMany(mappedBy = "scheduler")
    private List<Appointment> appointments;
}
