//FACTORY Y SINGLETON EN UNO

package co.unicauca.Entity.factory;

import co.unicauca.Entity.scheduling.AppointmentScheduler;
import co.unicauca.Entity.scheduling.ManualSchedule;
import co.unicauca.Entity.scheduling.SelfSchedule;
import co.unicauca.infra.dto.SchedulingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppointmentSchedulerFactoryImpl implements AppointmentSchedulerFactory{
    private final ManualSchedule manualSchedule;

    private final SelfSchedule selfSchedule;

    @Autowired
    public AppointmentSchedulerFactoryImpl(
            ManualSchedule manualSchedule,
            SelfSchedule selfSchedule
    ) {
        this.manualSchedule = manualSchedule;
        this.selfSchedule = selfSchedule;
    }

    @Override
    public AppointmentScheduler getScheduler(SchedulingType type) {
        return switch (type) {

            case MANUAL -> manualSchedule;

            case SELFSERVICE -> selfSchedule;

            default -> throw new IllegalArgumentException(
                    "Tipo de agendamiento no soportado"
            );
        };
    }
}
