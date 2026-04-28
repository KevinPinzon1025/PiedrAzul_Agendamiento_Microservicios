package co.unicauca.infra.dataLoader;

import co.unicauca.Entity.model.Appointment;
import co.unicauca.Repository.IAppointmentRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private IAppointmentRepository appointmentRepository;

   @Override
   public void run(String... args) throws Exception {

        // Evitar duplicar datos si se reinicia la app
        if (appointmentRepository.count() > 0) {
            return;
        }

        Appointment appointment1 = new Appointment();
        appointment1.setSchedulingDate(LocalDateTime.now());
        appointment1.setAppointmenDate(LocalDateTime.now().plusDays(1));
        appointment1.setObservation("Consulta general");
        appointment1.setScheduler(1L);     // usuario autenticado (simulado)
        appointment1.setPatient(101L);     // viene de microservicio paciente
        appointment1.setProfessional(201L); // viene de microservicio profesional

        Appointment appointment2 = new Appointment();
        appointment2.setSchedulingDate(LocalDateTime.now());
        appointment2.setAppointmenDate(LocalDateTime.now().plusDays(2));
        appointment2.setObservation("Control médico");
        appointment2.setScheduler(2L);
        appointment2.setPatient(102L);
        appointment2.setProfessional(202L);

        Appointment appointment3 = new Appointment();
        appointment3.setSchedulingDate(LocalDateTime.now());
        appointment3.setAppointmenDate(LocalDateTime.now().plusHours(5));
        appointment3.setObservation("Urgencia leve");
        appointment3.setScheduler(1L);
        appointment3.setPatient(103L);
        appointment3.setProfessional(201L);

        appointmentRepository.save(appointment1);
        appointmentRepository.save(appointment2);
        appointmentRepository.save(appointment3);

        System.out.println("Datos de prueba cargados correctamente");
    }


}