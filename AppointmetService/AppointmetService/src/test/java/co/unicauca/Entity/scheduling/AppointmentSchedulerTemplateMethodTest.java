package co.unicauca.Entity.scheduling;

import co.unicauca.Entity.model.Appointment;
import co.unicauca.Entity.model.Patient;
import co.unicauca.Entity.model.Professional;
import co.unicauca.Entity.model.Scheduler;
import co.unicauca.Entity.state.ConfirmedAppointment;
import co.unicauca.Entity.state.CreatedAppointment;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AppointmentSchedulerTemplateMethodTest {

    private static Professional professionalWithId(long id) {
        Professional professional = new Professional();
        professional.setId(id);
        professional.setProfName("Profesional test");
        return professional;
    }

    private static Patient samplePatient() {
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setPatName("Paciente test");
        return patient;
    }

    private static Scheduler sampleScheduler() {
        Scheduler scheduler = new Scheduler();
        scheduler.setId(1L);
        scheduler.setSchedulerName("Agendador test");
        return scheduler;
    }

    private static Appointment baseManualAppointment(LocalDateTime appointmentDate, long professionalId) {
        Appointment appointment = new Appointment(new CreatedAppointment());
        appointment.setAppointmentDate(appointmentDate);
        appointment.setProfessional(professionalWithId(professionalId));
        appointment.setSchedulingDate(LocalDateTime.now());
        appointment.setObservation("test");
        appointment.setPatient(samplePatient());
        appointment.setScheduler(sampleScheduler());
        return appointment;
    }

    private static Appointment baseSelfAppointment(LocalDateTime appointmentDate, long professionalId) {
        Appointment appointment = new Appointment(new CreatedAppointment());
        appointment.setAppointmentDate(appointmentDate);
        appointment.setProfessional(professionalWithId(professionalId));
        appointment.setSchedulingDate(LocalDateTime.now());
        appointment.setObservation("test");
        appointment.setPatient(samplePatient());
        appointment.setScheduler(null);
        return appointment;
    }

    private static LocalDateTime nextWeekdayAt(int hour) {
        LocalDateTime dt = LocalDateTime.now().plusDays(1).withHour(hour).withMinute(0).withSecond(0).withNano(0);
        while (dt.getDayOfWeek() == DayOfWeek.SATURDAY || dt.getDayOfWeek() == DayOfWeek.SUNDAY) {
            dt = dt.plusDays(1);
        }
        return dt;
    }

    @Test
    void manualSchedule_whenValid_confirmsAppointment() {
        AppointmentScheduler scheduler = new ManualSchedule();
        Appointment appointment = baseManualAppointment(nextWeekdayAt(9), 10L);

        scheduler.schedule(appointment);

        assertInstanceOf(ConfirmedAppointment.class, appointment.getState());
    }

    @Test
    void manualSchedule_whenInPast_throws() {
        AppointmentScheduler scheduler = new ManualSchedule();
        Appointment appointment = baseManualAppointment(LocalDateTime.now().minusMinutes(10), 10L);

        assertThrows(IllegalStateException.class, () -> scheduler.schedule(appointment));
    }

    @Test
    void manualSchedule_whenMissingScheduler_throws() {
        AppointmentScheduler scheduler = new ManualSchedule();
        Appointment appointment = baseManualAppointment(nextWeekdayAt(9), 10L);
        appointment.setScheduler(null);

        assertThrows(IllegalStateException.class, () -> scheduler.schedule(appointment));
    }

    @Test
    void selfSchedule_whenValid_confirmsAppointment() {
        AppointmentScheduler scheduler = new SelfSchedule();
        Appointment appointment = baseSelfAppointment(nextWeekdayAt(8), 10L);

        scheduler.schedule(appointment);

        assertInstanceOf(ConfirmedAppointment.class, appointment.getState());
    }

    @Test
    void selfSchedule_whenInPast_throws() {
        AppointmentScheduler scheduler = new SelfSchedule();
        Appointment appointment = baseSelfAppointment(LocalDateTime.now().minusMinutes(10), 10L);

        assertThrows(IllegalStateException.class, () -> scheduler.schedule(appointment));
    }

    @Test
    void selfSchedule_whenOutsideWorkingHours_throws() {
        AppointmentScheduler scheduler = new SelfSchedule();
        Appointment appointment = baseSelfAppointment(nextWeekdayAt(6), 10L);

        assertThrows(IllegalStateException.class, () -> scheduler.schedule(appointment));
    }

    @Test
    void selfSchedule_whenSchedulerPresent_throws() {
        AppointmentScheduler scheduler = new SelfSchedule();
        Appointment appointment = baseSelfAppointment(nextWeekdayAt(8), 10L);
        appointment.setScheduler(sampleScheduler());

        assertThrows(IllegalStateException.class, () -> scheduler.schedule(appointment));
    }
}

