package co.unicauca.infra.dataLoader;

import co.unicauca.Entity.model.*;
import co.unicauca.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private IAppointmentRepository appointmentRepository;
    @Autowired
    private IProfessionalRepository professionalRepository;
    @Autowired
    private ISchedulerRepository schedulerRepository;
    @Autowired
    private IPatientRepository patientRepository;
    @Autowired
    private IProfessionalWorkingDayRepository professionalWorkingDayRepository;

   @Override
   public void run(String... args) throws Exception {

        // Evitar duplicar datos si se reinicia la app
        if (appointmentRepository.count() > 0) {
            return;
        }

        List<Patient> patients = List.of(
                new Patient(1L, "Juan Pérez", null),
                new Patient(2L, "María Gómez", null),
                new Patient(3L, "Carlos Ruiz", null),
                new Patient(4L, "Ana Torres", null),
                new Patient(5L, "Luis Martínez", null)
        );
        patientRepository.saveAll(patients);

        List<Professional> professionals = List.of(
                new Professional(1L, "Dr. Jose", null),
                new Professional(2L, "Dra. Clara", null),
                new Professional(3L, "Dr. Ignacio", null),
                new Professional(4L, "Dra. Ibis", null),
                new Professional(5L, "Dr. Herrera", null)
        );
        professionalRepository.saveAll(professionals);

        List<Scheduler> schedulers = List.of(
                new Scheduler(1L, "Scheduler 1", null),
                new Scheduler(2L, "Scheduler 2", null),
                new Scheduler(3L, "Scheduler 3", null),
                new Scheduler(4L, "Scheduler 4", null),
                new Scheduler(5L, "Scheduler 5", null)
        );
        schedulerRepository.saveAll(schedulers);

        List<Appointment> appointments = List.of(
                new Appointment(0, LocalDateTime.now(), LocalDateTime.now().plusDays(1), "Consulta general", schedulers.get(0), patients.get(0), professionals.get(0), null),
                new Appointment(0, LocalDateTime.now(), LocalDateTime.now().plusDays(2), "Control", schedulers.get(1), patients.get(1), professionals.get(1), null),
                new Appointment(0, LocalDateTime.now(), LocalDateTime.now().plusDays(3), "Odontología", schedulers.get(2), patients.get(2), professionals.get(2), null),
                new Appointment(0, LocalDateTime.now(), LocalDateTime.now().plusDays(4), "Examen", schedulers.get(3), patients.get(3), professionals.get(3), null),
                new Appointment(0, LocalDateTime.now(), LocalDateTime.now().plusDays(5), "Revisión", schedulers.get(4), patients.get(4), professionals.get(4), null),
                new Appointment(0, LocalDateTime.now(), LocalDateTime.now().plusDays(6), "Consulta especializada", schedulers.get(0), patients.get(1), professionals.get(2), null),
                new Appointment(0, LocalDateTime.now(), LocalDateTime.now().plusDays(7), "Chequeo", schedulers.get(1), patients.get(2), professionals.get(3), null),
                new Appointment(0, LocalDateTime.now(), LocalDateTime.now().plusDays(8), "Urgencia", schedulers.get(2), patients.get(3), professionals.get(4), null)
        );
        appointmentRepository.saveAll(appointments);


       List<ProfessionalWorkingDay> workingDays =
               new ArrayList<>();

       for (Professional professional : professionals) {

           // Lunes
           workingDays.add(
                   buildWorkingDay(
                           professional,
                           DayOfWeek.MONDAY,
                           "08:00",
                           "12:00",
                           30
                   )
           );

           // Martes
           workingDays.add(
                   buildWorkingDay(
                           professional,
                           DayOfWeek.TUESDAY,
                           "08:00",
                           "12:00",
                           30
                   )
           );

           // Miércoles
           workingDays.add(
                   buildWorkingDay(
                           professional,
                           DayOfWeek.WEDNESDAY,
                           "14:00",
                           "18:00",
                           30
                   )
           );

           // Jueves
           workingDays.add(
                   buildWorkingDay(
                           professional,
                           DayOfWeek.THURSDAY,
                           "08:00",
                           "12:00",
                           20
                   )
           );

           // Viernes
           workingDays.add(
                   buildWorkingDay(
                           professional,
                           DayOfWeek.FRIDAY,
                           "09:00",
                           "13:00",
                           60
                   )
           );
       }

       professionalWorkingDayRepository.saveAll(workingDays);

        System.out.println("Datos de prueba cargados correctamente");
    }

    private ProfessionalWorkingDay buildWorkingDay(
            Professional professional,
            DayOfWeek dayOfWeek,
            String start,
            String end,
            Integer duration
    ) {

        ProfessionalWorkingDay workingDay =
                new ProfessionalWorkingDay();

        workingDay.setProfessional(professional);

        workingDay.setDayOfWeek(dayOfWeek);

        workingDay.setStartTime(
                LocalTime.parse(start)
        );

        workingDay.setEndTime(
                LocalTime.parse(end)
        );

        workingDay.setAppointmentDurationMinutes(
                duration
        );

        return workingDay;
    }

}