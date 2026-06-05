package co.unicauca.infra.messaging;

import co.unicauca.Repository.IProfessionalRepository;
import co.unicauca.Entity.model.Professional;
import co.unicauca.infra.dto.ProfessionalDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfessionalEventListener {

    @Autowired
    private IProfessionalRepository professionalRepository;

    @Transactional
    @RabbitListener(queues = "professional.created.queue")
    public void handleProfessionalCreated(ProfessionalDTO professional) {
        System.out.println("Profesional creado: " + professional.getId());

        //guardar el profesional recibido en el repositorio
        if (!professionalRepository.existsById(professional.getId())) {
            Professional professionalCreated = new Professional();
            professionalCreated.setId(professional.getId());
            professionalCreated.setProfName(professional.getProfName());
            professionalRepository.save(professionalCreated);
        }

    }

    @Transactional
    @RabbitListener(queues = "professional.updated.queue")
    public void handleProfessionalUpdated(ProfessionalDTO professional) {
        System.out.println("Profesional actualizado recibido: " + professional);
        professionalRepository.findById(professional.getId()).ifPresent(existing -> {
            existing.setProfName(professional.getProfName());
            professionalRepository.save(existing);
        });
    }

}