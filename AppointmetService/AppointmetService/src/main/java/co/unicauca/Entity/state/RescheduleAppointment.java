package co.unicauca.Entity.state;

import co.unicauca.Entity.model.Appointment;

public class RescheduleAppointment implements AppointmentState{
    private Appointment context;

    @Override
    public void setContext(Appointment context) {
        this.context = context;
    }

    @Override
    public void cancell() {
        //de cita creada, pasamos a cita confirmada.
        if(context == null){
            return;
        }
        AppointmentState cancelledAppointmentState = new CancelledAppointment();
        context.setState(cancelledAppointmentState);
    }

    @Override
    public void reschedule() {}

    @Override
    public void markCompleted() {}

    @Override
    public void markConfirmed() {
        //de cita reagendada, pasamos a cita confirmada.
        if(context == null){
            return;
        }
        AppointmentState confirAppointmentState = new ConfirmedAppointment();
        context.setState(confirAppointmentState);
    }

    @Override
    public void markExpired() {
        if(context == null){
            return;
        }
        AppointmentState expiredAppointmentState = new ExpiredAppointment();
        context.setState(expiredAppointmentState);
    }

}
