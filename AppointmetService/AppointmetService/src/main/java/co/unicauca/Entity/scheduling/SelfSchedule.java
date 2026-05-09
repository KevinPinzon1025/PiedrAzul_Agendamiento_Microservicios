package co.unicauca.Entity.scheduling;

import co.unicauca.Entity.model.Appointment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SelfSchedule extends AppointmentScheduler {
    @Override
    protected boolean checkAvailability(Appointment appointment) {

        if (appointment.getAppointmenDate().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("No se puede agendar en el pasado");
        }
        // validacion, es horario laboral?
        int hour = appointment.getAppointmenDate().getHour();
        if (hour < 7 || hour > 12) {
            throw new IllegalStateException("Fuera del horario laboral");
        }

        return true;
    }

    @Override
    protected void assignProfessional(Appointment appointment) {
        //el agendamiento autonomo no tiene agendador asociado
        if (appointment.getScheduler()!= null) {
            throw new IllegalStateException("Error: No debe existir un agendador en el agendamiento autonomo");
        }
    }

    @Override
    protected void confirmAppointment(Appointment appointment) {
        appointment.markConfirmed();
    }
}
