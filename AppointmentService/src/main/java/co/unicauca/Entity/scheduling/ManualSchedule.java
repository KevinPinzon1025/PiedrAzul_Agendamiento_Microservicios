package co.unicauca.Entity.scheduling;

import co.unicauca.Entity.model.Appointment;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Component
public class ManualSchedule extends AppointmentScheduler {
    @Override
    protected boolean checkAvailability(Appointment appointment) {
        if (appointment.getAppointmentDate().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("No se puede agendar en el pasado");
        }
        //validacion sencilla, no es fin de semana?
        DayOfWeek day = appointment.getAppointmentDate().getDayOfWeek();
        return day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY;
    }

    @Override
    protected void assignProfessional(Appointment appointment) {
        //validacion de negocio, el agendamiento manual debe tener un agendador asociado
        if (appointment.getScheduler() == null) {
            throw new IllegalStateException("Error: El agendamiento manual debe contar con un agendador");
        }
    }

    @Override
    protected void confirmAppointment(Appointment appointment) {
        appointment.markConfirmed();
    }
}
