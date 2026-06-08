package co.unicauca.port.out.db;

import co.unicauca.domain.model.SchedulingConfiguration;

public interface SchedulingConfigurationDbPort {

    SchedulingConfiguration getConfiguration();

    SchedulingConfiguration save(SchedulingConfiguration configuration);
}
