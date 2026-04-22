package co.unicauca.Entity.facade;

import co.unicauca.Entity.model.Appointment;
import co.unicauca.Entity.scheduling.ManualSchedule;
import co.unicauca.Entity.state.AppointmentState;
import co.unicauca.Entity.state.CreatedAppointment;
import co.unicauca.Service.AppointmentService;
import co.unicauca.infra.event.AppointmentCreatedEvent;        // NUEVO
import co.unicauca.infra.messaging.AppointmentEventPublisher;  // NUEVO
import org.slf4j.Logger;                                        // NUEVO
import org.slf4j.LoggerFactory;                                 // NUEVO
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppointmentFacade {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentFacade.class);

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private AppointmentEventPublisher eventPublisher;  // NUEVO

    public AppointmentFacade(AppointmentService appointmentService,
                             AppointmentEventPublisher eventPublisher) {
        this.appointmentService = appointmentService;
        this.eventPublisher     = eventPublisher;
    }

    public Appointment createAppointment(Appointment appointment) {

        appointment.initState();
        AppointmentState state = new CreatedAppointment();
        state.setContext(appointment);

        ManualSchedule scheduler = new ManualSchedule();
        scheduler.schedule(appointment);
      
        Appointment saved = appointmentService.save(appointment);
        logger.info("[FACADE] Cita id={} guardada exitosamente en BD.", saved.getIdAppointment());

       
        AppointmentCreatedEvent event = new AppointmentCreatedEvent(
                saved.getIdAppointment(),
                saved.getSchedulingDate(),
                saved.getAppointmenDate(),
                saved.getObservation(),
                saved.getScheduler(),
                saved.getPatient(),
                saved.getProfessional()
        );
        eventPublisher.publish(event);

        return saved;
    }

    public AppointmentService getAppointmentService() {
        return appointmentService;
    }
}
