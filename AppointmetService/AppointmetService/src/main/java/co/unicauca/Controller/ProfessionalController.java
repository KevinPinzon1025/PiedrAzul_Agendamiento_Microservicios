package co.unicauca.Controller;

import co.unicauca.Entity.model.Professional;
import co.unicauca.Service.ProfessionalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/appointment")
public class ProfessionalController {

    private final ProfessionalService professionalService;

    public ProfessionalController(ProfessionalService professionalService) {
        this.professionalService = professionalService;
    }

    @GetMapping("/professionals")
    public ResponseEntity<List<Professional>> listAllProfessionals() {return ResponseEntity.ok(professionalService.findAll());}
}
