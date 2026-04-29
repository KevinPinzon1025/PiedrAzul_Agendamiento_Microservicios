package co.unicauca.Entity.scheduling;

import co.unicauca.Entity.model.Appointment;

import java.time.LocalDateTime;

public class SelfSchedule extends AppointmentScheduler {
    @Override
    protected boolean checkAvailability(Appointment appointment) {

        if (appointment.getAppointmenDate().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("No se puede agendar en el pasado");
        }
        // validacion sencilla, es horario laboral?
        int hour = appointment.getAppointmenDate().getHour();
        if (hour < 7 || hour > 12) {
            throw new IllegalStateException("Fuera del horario laboral");
        }

        return true;
    }

    @Override
    protected void assignProfessional(Appointment appointment) {
        if (appointment.getProfessional() == null) {
            throw new IllegalStateException("Debe seleccionar un profesional");
        }
    }

    @Override
    protected void confirmAppointment(Appointment appointment) {
        appointment.markConfirmed();
    }
}
