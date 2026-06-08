package co.unicauca.adapter.in.web;

import co.unicauca.application.dto.SchedulingConfigurationRequest;
import co.unicauca.application.dto.SchedulingConfigurationResponse;
import co.unicauca.application.service.ProfessionalApplicationService;
import co.unicauca.port.in.web.SchedulingConfigurationWebPort;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SchedulingConfigurationWebAdapter
        implements SchedulingConfigurationWebPort {

    private final ProfessionalApplicationService professionalApplicationService;

    public SchedulingConfigurationWebAdapter(
            ProfessionalApplicationService professionalApplicationService
    ) {
        this.professionalApplicationService = professionalApplicationService;
    }

    @Override
    public SchedulingConfigurationResponse getConfiguration() {
        return professionalApplicationService.getConfiguration();
    }

    @Override
    public SchedulingConfigurationResponse updateConfiguration(
            SchedulingConfigurationRequest request
    ) {
        return professionalApplicationService.updateConfiguration(request);
    }
}
