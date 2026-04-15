package co.unicauca.Entity.state;

import co.unicauca.Entity.model.Appointment;

public class ExpiredAppointment implements  AppointmentState {
    private Appointment context;

    @Override
    public void setContext(Appointment context) {
        this.context = context;
    }

    @Override
    public void cancell() {}

    @Override
    public void reschedule() {
        if(context == null){
            return;
        }
        AppointmentState rescheduleAppointmentState = new RescheduleAppointment();
        context.setState(rescheduleAppointmentState);
    }

    @Override
    public void markCompleted() {}

    @Override
    public void markConfirmed() {}

    @Override
    public void markExpired() {}

}
