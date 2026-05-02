package AuthService.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "agendadores")
@DiscriminatorValue("AGENDADOR")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Agendador extends User {

}
