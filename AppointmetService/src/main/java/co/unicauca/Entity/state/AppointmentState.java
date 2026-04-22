package co.unicauca.Entity.state;

import co.unicauca.Entity.model.Appointment;

public interface AppointmentState {
    void setContext(Appointment context);
    void cancell();
    void reschedule();
    void markCompleted();
    void markConfirmed();
    void markExpired();
}
