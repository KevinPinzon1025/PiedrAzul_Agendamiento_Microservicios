package co.unicauca.report.messaging;

import co.unicauca.report.entity.AppointmentReportRecord;
import co.unicauca.report.event.AppointmentCreatedEvent;
import co.unicauca.report.repository.AppointmentReportRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AppointmentCreatedEventListener {

    private final AppointmentReportRepository appointmentReportRepository;

    public AppointmentCreatedEventListener(AppointmentReportRepository appointmentReportRepository) {
        this.appointmentReportRepository = appointmentReportRepository;
    }

    @RabbitListener(queues = "${piedrAzul.rabbitmq.appointments.report-created-queue}")
    public void handle(AppointmentCreatedEvent event) {
        AppointmentReportRecord record = new AppointmentReportRecord();
        record.setIdAppointment(event.getIdAppointment());
        record.setSchedulingDate(event.getSchedulingDate());
        record.setAppointmentDate(event.getAppointmentDate());
        record.setObservation(event.getObservation());
        record.setSchedulerId(event.getScheduler());
        record.setPatientId(event.getPatient());
        record.setPatientName(event.getPatientName());
        record.setProfessionalId(event.getProfessional());
        record.setProfessionalName(event.getProfessionalName());

        appointmentReportRepository.save(record);
    }
}
