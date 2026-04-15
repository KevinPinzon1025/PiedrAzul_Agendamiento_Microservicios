package co.unicauca.Entity.state;

import co.unicauca.Entity.model.Appointment;

public class ConfirmedAppointment implements AppointmentState {
    private Appointment context;

    @Override
    public void setContext(Appointment context) {
        this.context = context;
    }

    @Override
    public void cancell() {
        if(context == null){
            return;
        }
        AppointmentState cancelledAppointmentState = new CancelledAppointment();
        context.setState(cancelledAppointmentState);
    }

    @Override
    public void reschedule() {
        if(context == null){
            return;
        }
        AppointmentState rescheduleAppointmentState = new RescheduleAppointment();
        context.setState(rescheduleAppointmentState);
    }

    @Override
    public void markCompleted() {
        if(context == null){
            return;
        }
        AppointmentState completedAppointmentState = new CompletedAppointment();
        context.setState(completedAppointmentState);
    }

    @Override
    public void markConfirmed() {}

    @Override
    public void markExpired() {
        if(context == null){
            return;
        }
        AppointmentState expiredAppointmentState = new ExpiredAppointment();
        context.setState(expiredAppointmentState);
    }

}
