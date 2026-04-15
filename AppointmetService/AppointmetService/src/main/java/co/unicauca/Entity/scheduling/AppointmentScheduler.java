package co.unicauca.Entity.scheduling;

import co.unicauca.Entity.model.Appointment;

public abstract class AppointmentScheduler {
    public final void schedule(Appointment appointment) {
        //validateUser(appointment);
        checkAvailability(appointment);
        assignProfessional(appointment);
        confirmAppointment(appointment);
    }

    //protected abstract void validateUser(Appointment appointment);
    protected abstract boolean checkAvailability(Appointment appointment);
    protected abstract void assignProfessional(Appointment appointment);
    protected abstract void confirmAppointment(Appointment appointment);
}
