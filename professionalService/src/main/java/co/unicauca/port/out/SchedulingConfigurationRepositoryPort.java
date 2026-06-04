package co.unicauca.port.out;

import co.unicauca.domain.model.SchedulingConfiguration;

public interface SchedulingConfigurationRepositoryPort {
    SchedulingConfiguration getConfiguration();
    SchedulingConfiguration save(SchedulingConfiguration configuration);
}
