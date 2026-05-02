package AuthService.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

// JOINED strategy: base fields live in "users", each subclass gets its own table
// with a FK back to "users". Avoids null columns (SINGLE_TABLE) and column
// duplication (TABLE_PER_CLASS). Best fit when subtypes may grow their own fields.
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING, length = 30)
@DiscriminatorValue("USER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 80)
    private String login;

    @JsonIgnore
    @Column(nullable = false, name = "password_hash", length = 512)
    private String passwordHash;

    @JsonIgnore
    @Column(nullable = false, name = "password_salt", length = 255)
    private String passwordSalt;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false, length = 80)
    private String firstName;

    @Column(nullable = false, length = 80)
    private String firstLastName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Role role;
}
