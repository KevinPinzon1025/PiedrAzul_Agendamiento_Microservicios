package co.unicauca.Entity.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @brief Clase para mapear un paciente, dado que este es el microservicio de Appointment
 * no guarda toda la informacion de un agendador, solo su id y su nombre, que debe ser
 * sincronizado con la BD del microservicio correspondiente a traves de una suscripcion al Patient service
 *
 */
@Entity
@Table(name = "patient")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    @Id
    private long id;
    @Column(name = "pat_name", nullable = false)
    private String patName;
    @JsonIgnore
    @OneToMany(mappedBy = "patient")
    private List<Appointment> appointments;
}
