package co.unicauca.Entity.decorator;

import co.unicauca.Entity.model.Appointment;

public class PriorityAppointment implements AppointmetDecorator {
    private Appointment appointment;

    public PriorityAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    @Override
    public String obtainObservation() {
        return appointment.obtainObservation() + " [PRIORIDAD ALTA]";
    }
}
