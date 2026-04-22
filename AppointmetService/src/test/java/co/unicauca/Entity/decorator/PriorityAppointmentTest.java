package co.unicauca.Entity.decorator;

import co.unicauca.Entity.model.Appointment;
import co.unicauca.Entity.state.CreatedAppointment;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PriorityAppointmentTest {

    private static Appointment baseAppointment() {
        Appointment appointment = new Appointment(new CreatedAppointment());
        appointment.setObservation("Consulta general");
        appointment.setSchedulingDate(LocalDateTime.now());
        appointment.setAppointmenDate(LocalDateTime.now().plusDays(1));
        appointment.setPatient(1L);
        appointment.setProfessional(1L);
        appointment.setScheduler(1L);
        return appointment;
    }

    @Test
    void priorityDecorator_addsPriorityText() {
        Appointment appointment = baseAppointment();
        PriorityAppointment decorator = new PriorityAppointment(appointment);

        String result = decorator.obtainObservation();

        assertEquals("Consulta general [PRIORIDAD ALTA]", result);
    }

    @Test
    void priorityDecorator_doesNotModifyOriginalAppointment() {
        Appointment appointment = baseAppointment();
        PriorityAppointment decorator = new PriorityAppointment(appointment);

        decorator.obtainObservation();

        // Verifica que el original no cambia
        assertEquals("Consulta general", appointment.getObservation());
    }

    @Test
    void priorityDecorator_canBeAppliedMultipleTimes() {
        Appointment appointment = baseAppointment();

        PriorityAppointment decorator1 = new PriorityAppointment(appointment);
        PriorityAppointment decorator2 = new PriorityAppointment(appointment);

        String result1 = decorator1.obtainObservation();
        String result2 = decorator2.obtainObservation();

        assertEquals(result1, result2);
    }
}