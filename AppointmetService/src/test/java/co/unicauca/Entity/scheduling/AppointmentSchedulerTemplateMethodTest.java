package co.unicauca.Entity.scheduling;

import co.unicauca.Entity.model.Appointment;
import co.unicauca.Entity.state.ConfirmedAppointment;
import co.unicauca.Entity.state.CreatedAppointment;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AppointmentSchedulerTemplateMethodTest {

    private static Appointment baseAppointment(LocalDateTime appointmentDate, long professionalId) {
        Appointment appointment = new Appointment(new CreatedAppointment());
        appointment.setAppointmenDate(appointmentDate);
        appointment.setProfessional(professionalId);
        appointment.setSchedulingDate(LocalDateTime.now());
        appointment.setObservation("test");
        appointment.setPatient(1L);
        appointment.setScheduler(1L);
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
        Appointment appointment = baseAppointment(nextWeekdayAt(9), 10L);

        scheduler.schedule(appointment);

        assertInstanceOf(ConfirmedAppointment.class, appointment.getState());
    }

    @Test
    void manualSchedule_whenInPast_throws() {
        AppointmentScheduler scheduler = new ManualSchedule();
        Appointment appointment = baseAppointment(LocalDateTime.now().minusMinutes(10), 10L);

        assertThrows(IllegalStateException.class, () -> scheduler.schedule(appointment));
    }

    @Test
    void manualSchedule_whenNoProfessional_throws() {
        AppointmentScheduler scheduler = new ManualSchedule();
        Appointment appointment = baseAppointment(nextWeekdayAt(9), 0L);

        assertThrows(IllegalStateException.class, () -> scheduler.schedule(appointment));
    }

    @Test
    void selfSchedule_whenValid_confirmsAppointment() {
        AppointmentScheduler scheduler = new SelfSchedule();
        Appointment appointment = baseAppointment(nextWeekdayAt(8), 10L);

        scheduler.schedule(appointment);

        assertInstanceOf(ConfirmedAppointment.class, appointment.getState());
    }

    @Test
    void selfSchedule_whenInPast_throws() {
        AppointmentScheduler scheduler = new SelfSchedule();
        Appointment appointment = baseAppointment(LocalDateTime.now().minusMinutes(10), 10L);

        assertThrows(IllegalStateException.class, () -> scheduler.schedule(appointment));
    }

    @Test
    void selfSchedule_whenOutsideWorkingHours_throws() {
        AppointmentScheduler scheduler = new SelfSchedule();
        Appointment appointment = baseAppointment(nextWeekdayAt(6), 10L);

        assertThrows(IllegalStateException.class, () -> scheduler.schedule(appointment));
    }

    @Test
    void selfSchedule_whenNoProfessional_throws() {
        AppointmentScheduler scheduler = new SelfSchedule();
        Appointment appointment = baseAppointment(nextWeekdayAt(8), 0L);

        assertThrows(IllegalStateException.class, () -> scheduler.schedule(appointment));
    }
}

