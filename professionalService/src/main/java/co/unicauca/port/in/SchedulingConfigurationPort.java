package co.unicauca.port.in;

import co.unicauca.application.dto.SchedulingConfigurationRequest;
import co.unicauca.application.dto.SchedulingConfigurationResponse;

public interface SchedulingConfigurationPort {
    SchedulingConfigurationResponse getConfiguration();
    SchedulingConfigurationResponse updateConfiguration(SchedulingConfigurationRequest request);
}
