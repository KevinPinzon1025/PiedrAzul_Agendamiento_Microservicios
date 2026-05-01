package AuthService.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

// Represents scheduling staff (role = SCHEDULER).
// Maps to the "agendadores" table; shares all base columns from "users" via FK.
@Entity
@Table(name = "agendadores")
@DiscriminatorValue("AGENDADOR")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Agendador extends User {
    // Agendador-specific fields can be added here as the domain grows.
    // Currently inherits all authentication and identity fields from User.
}
