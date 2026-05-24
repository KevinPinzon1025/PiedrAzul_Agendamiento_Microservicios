package co.unicauca.infra.messaging;

import co.unicauca.Entity.model.Scheduler;
import co.unicauca.Repository.ISchedulerRepository;
import co.unicauca.infra.dto.SchedulerDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class SchedulerEventListener {
    @Autowired
    private ISchedulerRepository schedulerRepository;

    @Transactional
    @RabbitListener(queues = "scheduler.created.queue")
    public void handleSchedulerCreated(SchedulerDTO scheduler) {
        System.out.println("Agendador creado: " + scheduler.getId());

        //guardar el scheduler recibido en el repositorio
        if (!schedulerRepository.existsById(scheduler.getId())) {
            Scheduler schedulerCreated = new Scheduler();
            schedulerCreated.setId(scheduler.getId());
            schedulerCreated.setSchedulerName(scheduler.getProfName());
            schedulerRepository.save(schedulerCreated);
        }

    }
}
