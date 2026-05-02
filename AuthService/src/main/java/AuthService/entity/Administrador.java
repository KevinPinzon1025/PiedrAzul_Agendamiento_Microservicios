package AuthService.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "administradores")
@DiscriminatorValue("ADMINISTRADOR")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Administrador extends User {

}
