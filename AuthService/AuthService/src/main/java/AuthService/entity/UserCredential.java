package AuthService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "user_credentials")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCredential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Para pacientes, el login es el mismo número de documento.
    @Column(nullable = false, unique = true, length = 80)
    private String login;

    @Column(name = "document_number", unique = true, length = 30)
    private String documentNumber;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false, length = 80)
    private String firstName;

    @Column(length = 80)
    private String secondName;

    @Column(nullable = false, length = 80)
    private String firstLastName;

    @Column(length = 80)
    private String secondLastName;

    @Column(length = 30)
    private String phone;

    @Column(length = 20)
    private String gender;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(length = 120)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Role role;
}
