package co.unicauca.Entity.scheduling;

import co.unicauca.Entity.model.Appointment;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class ManualSchedule extends AppointmentScheduler {
    @Override
    protected boolean checkAvailability(Appointment appointment) {
        if (appointment.getAppointmenDate().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("No se puede agendar en el pasado");
        }
        //validacion sencilla, no es fin de semana?
        DayOfWeek day = appointment.getAppointmenDate().getDayOfWeek();
        return day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY;
    }

    @Override
    protected void assignProfessional(Appointment appointment) {
        //validacion sencilla, en Manual ya debe venir asignado un profesional
        if (appointment.getProfessional() == 0) {
            throw new IllegalStateException("Debe seleccionar un profesional");
        }
    }

    @Override
    protected void confirmAppointment(Appointment appointment) {
        appointment.markConfirmed();
    }
}
