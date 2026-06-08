package co.unicauca.port.in.web;

import co.unicauca.application.dto.SchedulingConfigurationRequest;
import co.unicauca.application.dto.SchedulingConfigurationResponse;
import org.springframework.web.bind.annotation.*;

public interface SchedulingConfigurationWebPort {

    @GetMapping("/scheduling/configuration")
    SchedulingConfigurationResponse getConfiguration();

    @PutMapping("/scheduling/configuration")
    SchedulingConfigurationResponse updateConfiguration(
            @RequestBody SchedulingConfigurationRequest request
    );
}
