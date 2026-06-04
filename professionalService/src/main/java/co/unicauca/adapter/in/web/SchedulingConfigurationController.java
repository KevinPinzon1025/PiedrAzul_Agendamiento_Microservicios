package co.unicauca.adapter.in.web;

import co.unicauca.application.dto.SchedulingConfigurationRequest;
import co.unicauca.application.dto.SchedulingConfigurationResponse;
import co.unicauca.port.in.SchedulingConfigurationPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/scheduling/configuration")
public class SchedulingConfigurationController {
    private final SchedulingConfigurationPort configurationPort;

    public SchedulingConfigurationController(SchedulingConfigurationPort configurationPort) {
        this.configurationPort = configurationPort;
    }

    @GetMapping
    public ResponseEntity<SchedulingConfigurationResponse> getConfiguration() {
        return ResponseEntity.ok(configurationPort.getConfiguration());
    }

    @PutMapping
    public ResponseEntity<SchedulingConfigurationResponse> updateConfiguration(@RequestBody SchedulingConfigurationRequest request) {
        return ResponseEntity.ok(configurationPort.updateConfiguration(request));
    }
}
