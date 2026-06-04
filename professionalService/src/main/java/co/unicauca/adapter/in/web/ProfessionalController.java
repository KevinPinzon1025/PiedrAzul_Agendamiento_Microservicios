package co.unicauca.adapter.in.web;

import co.unicauca.application.dto.ProfessionalRequest;
import co.unicauca.application.dto.ProfessionalResponse;
import co.unicauca.port.in.ProfessionalManagementPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/professionals")
public class ProfessionalController {
    private final ProfessionalManagementPort professionalManagementPort;

    public ProfessionalController(ProfessionalManagementPort professionalManagementPort) {
        this.professionalManagementPort = professionalManagementPort;
    }

    @GetMapping
    public ResponseEntity<List<ProfessionalResponse>> findAll() {
        return ResponseEntity.ok(professionalManagementPort.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessionalResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(professionalManagementPort.findById(id));
    }

    @PostMapping
    public ResponseEntity<ProfessionalResponse> create(@RequestBody ProfessionalRequest request) {
        ProfessionalResponse created = professionalManagementPort.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfessionalResponse> update(@PathVariable Long id, @RequestBody ProfessionalRequest request) {
        return ResponseEntity.ok(professionalManagementPort.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        professionalManagementPort.delete(id);
        return ResponseEntity.noContent().build();
    }
}
