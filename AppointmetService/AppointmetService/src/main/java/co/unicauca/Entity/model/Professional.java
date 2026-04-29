package co.unicauca.Entity.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @brief Clase para mapear un profesional, dado que este es el microservicio de Appointment
 * no guarda toda la informacion de un agendador, solo su id y su nombre, que debe ser
 * sincronizado con la BD del microservicio correspondiente a traves de
 * una suscripcion al professional service
 *
 */
@Entity
@Table(name = "professional")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Professional {
    @Id
    private long id;
    @Column(name = "prof_name", nullable = false)
    private String profName;
    @JsonIgnore
    @OneToMany(mappedBy = "professional")
    private List<Appointment> appointments;

}
