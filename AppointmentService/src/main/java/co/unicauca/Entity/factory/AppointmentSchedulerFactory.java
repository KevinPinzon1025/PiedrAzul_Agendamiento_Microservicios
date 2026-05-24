package co.unicauca.Entity.factory;

import co.unicauca.Entity.scheduling.AppointmentScheduler;
import co.unicauca.infra.dto.SchedulingType;

public interface AppointmentSchedulerFactory {
    AppointmentScheduler getScheduler(SchedulingType type);
}
