package AuthService.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

// Represents administrative staff (role = ADMIN).
// Maps to the "administradores" table; shares all base columns from "users" via FK.
@Entity
@Table(name = "administradores")
@DiscriminatorValue("ADMINISTRADOR")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Administrador extends User {
    // Administrador-specific fields can be added here as the domain grows.
    // Currently inherits all authentication and identity fields from User.
}
