package co.unicauca.Entity.state;

import co.unicauca.Entity.model.Appointment;

public class CreatedAppointment implements  AppointmentState {
    private Appointment context;

    @Override
    public void setContext(Appointment context) {
        this.context = context;
    }

    @Override
    public void cancell() {
        return; //no se puede cancelar sin confirmacion
    }

    @Override
    public void reschedule() {
        return; //no se puede reagendar sin haber confirmado
    }

    @Override
    public void markCompleted() {
        return;
    }

    @Override
    public void markConfirmed() {
        //de cita creada, pasamos a cita confirmada.
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
