package co.unicauca.port.in.web;

import co.unicauca.application.dto.ProfessionalRequest;
import co.unicauca.application.dto.ProfessionalResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface ProfessionalWebPort {

    @GetMapping("/professionals")
    List<ProfessionalResponse> findAll();

    @GetMapping("/professionals/{id}")
    ProfessionalResponse findById(@PathVariable Long id);

    @PostMapping("/professionals")
    ProfessionalResponse create(@RequestBody ProfessionalRequest request);

    @PutMapping("/professionals/{id}")
    ProfessionalResponse update(@PathVariable Long id,
                                @RequestBody ProfessionalRequest request);

    @DeleteMapping("/professionals/{id}")
    void delete(@PathVariable Long id);
}
