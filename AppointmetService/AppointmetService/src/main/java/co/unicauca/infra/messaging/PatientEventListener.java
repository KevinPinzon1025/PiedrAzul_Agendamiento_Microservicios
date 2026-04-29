package co.unicauca.infra.messaging;

import co.unicauca.Entity.model.Patient;
import co.unicauca.Repository.IPatientRepository;
import co.unicauca.infra.dto.PatientDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class PatientEventListener {
    @Autowired
    private IPatientRepository patientRepository;

    @Transactional
    @RabbitListener(queues = "patient.created.queue")
    public void handlePatientCreated(PatientDTO patient) {
        System.out.println("Paciente creado: " + patient.getId());

        //guardar el paciente recibido en el repositorio
        if (!patientRepository.existsById(patient.getId())) {
            Patient patientCreated = new Patient();
            patientCreated.setId(patient.getId());
            patientCreated.setPatName(patient.getPatName());
            patientRepository.save(patientCreated);
        }

    }
}
