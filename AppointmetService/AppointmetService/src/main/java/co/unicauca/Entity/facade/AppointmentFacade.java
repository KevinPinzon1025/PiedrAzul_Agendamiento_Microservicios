package co.unicauca.Entity.facade;

import co.unicauca.Entity.model.Appointment;
import co.unicauca.Entity.scheduling.ManualSchedule;
import co.unicauca.Entity.state.AppointmentState;
import co.unicauca.Entity.state.CreatedAppointment;
import co.unicauca.Service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppointmentFacade {

    @Autowired
    private AppointmentService appointmentService;

    public AppointmentFacade(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    public Appointment createAppointment(Appointment appointment) {

        // 1. Inicializar estado
        appointment.initState();
        AppointmentState state = new CreatedAppointment();
        state.setContext(appointment);

        // 2. Programar cita
        ManualSchedule scheduler = new ManualSchedule();
        scheduler.schedule(appointment);

        // 3. Guardar usando el service
        return appointmentService.save(appointment);
    }

    public AppointmentService getAppointmentService() {
        return appointmentService;
    }
}