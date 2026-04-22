package co.unicauca.Entity.model;

import co.unicauca.Entity.decorator.AppointmetDecorator;
import co.unicauca.Entity.state.AppointmentState;
import co.unicauca.Entity.state.CreatedAppointment;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment implements AppointmetDecorator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idAppointment;

    @Column(name = "schedule_date", nullable = false)
    private LocalDateTime schedulingDate;

    @Column(name = "appointment_date", nullable = false)
    private LocalDateTime appointmenDate;

    @Column(name = "observation", nullable = false, length = 255)
    private String observation;

    @Column(name = "scheduler_id")
    private long scheduler;

    @Column(name = "patient_id", nullable = false)
    private long patient;

    @Column(name = "professional_id", nullable = false)
    private long professional;

    @JsonIgnore
    @Transient
    private AppointmentState state;

    @PostLoad
    @PostPersist
    public void initState() {
        if (this.state == null) {
            this.state = new CreatedAppointment();
            this.state.setContext(this);
        }
    }

    public Appointment(AppointmentState initState) {
        this.state = initState;
        this.state.setContext(this);
    }

    public void cancell() {
        state.cancell();
    }

    public void reschedule() {
        state.reschedule();
    }

    public void markCompleted() {
        state.markCompleted();
    }

    public void markConfirmed() {
        state.markConfirmed();
    }

    void markExpired() {
        state.markExpired();
    }

    @Override
    public String obtainObservation() {
        return this.observation;
    }
}