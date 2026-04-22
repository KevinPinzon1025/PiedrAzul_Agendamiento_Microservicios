package co.unicauca.Entity.state;

import co.unicauca.Entity.model.Appointment;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class AppointmentStateTest {

    private static Appointment appointmentWithState(AppointmentState initState) {
        return new Appointment(initState);
    }

    @Test
    void created_markConfirmed_transitionsToConfirmed() {
        Appointment appointment = appointmentWithState(new CreatedAppointment());

        appointment.markConfirmed();

        assertInstanceOf(ConfirmedAppointment.class, appointment.getState());
    }

    @Test
    void created_markExpired_transitionsToExpired() {
        Appointment appointment = appointmentWithState(new CreatedAppointment());

        appointment.getState().markExpired();

        assertInstanceOf(ExpiredAppointment.class, appointment.getState());
    }

    @Test
    void created_cancell_doesNotChangeState() {
        Appointment appointment = appointmentWithState(new CreatedAppointment());

        appointment.cancell();

        assertInstanceOf(CreatedAppointment.class, appointment.getState());
    }

    @Test
    void created_reschedule_doesNotChangeState() {
        Appointment appointment = appointmentWithState(new CreatedAppointment());

        appointment.reschedule();

        assertInstanceOf(CreatedAppointment.class, appointment.getState());
    }

    @Test
    void confirmed_cancell_transitionsToCancelled() {
        Appointment appointment = appointmentWithState(new ConfirmedAppointment());

        appointment.cancell();

        assertInstanceOf(CancelledAppointment.class, appointment.getState());
    }

    @Test
    void confirmed_reschedule_transitionsToReschedule() {
        Appointment appointment = appointmentWithState(new ConfirmedAppointment());

        appointment.reschedule();

        assertInstanceOf(RescheduleAppointment.class, appointment.getState());
    }

    @Test
    void confirmed_markCompleted_transitionsToCompleted() {
        Appointment appointment = appointmentWithState(new ConfirmedAppointment());

        appointment.markCompleted();

        assertInstanceOf(CompletedAppointment.class, appointment.getState());
    }

    @Test
    void confirmed_markExpired_transitionsToExpired() {
        Appointment appointment = appointmentWithState(new ConfirmedAppointment());

        appointment.getState().markExpired();

        assertInstanceOf(ExpiredAppointment.class, appointment.getState());
    }

    @Test
    void expired_reschedule_transitionsToReschedule() {
        Appointment appointment = appointmentWithState(new ExpiredAppointment());

        appointment.reschedule();

        assertInstanceOf(RescheduleAppointment.class, appointment.getState());
    }

    @Test
    void reschedule_markConfirmed_transitionsToConfirmed() {
        Appointment appointment = appointmentWithState(new RescheduleAppointment());

        appointment.markConfirmed();

        assertInstanceOf(ConfirmedAppointment.class, appointment.getState());
    }

    @Test
    void reschedule_cancell_transitionsToCancelled() {
        Appointment appointment = appointmentWithState(new RescheduleAppointment());

        appointment.cancell();

        assertInstanceOf(CancelledAppointment.class, appointment.getState());
    }
}

