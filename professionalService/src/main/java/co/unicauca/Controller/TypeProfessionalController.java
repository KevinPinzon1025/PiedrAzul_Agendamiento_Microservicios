package co.unicauca.Controller;

import co.unicauca.Entity.model.TypeProfessional;
import co.unicauca.Service.TypeProfessionalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/types")
public class TypeProfessionalController {

    private final TypeProfessionalService typeProfessionalService;

    public TypeProfessionalController(TypeProfessionalService typeProfessionalService) {
        this.typeProfessionalService = typeProfessionalService;
    }

    @GetMapping
    public ResponseEntity<List<TypeProfessional>> listAll() {
        return ResponseEntity.ok(typeProfessionalService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TypeProfessional> getById(@PathVariable("id") Long id) {
        return typeProfessionalService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TypeProfessional> create(@RequestBody TypeProfessional typeProfessional) {
        TypeProfessional created = typeProfessionalService.create(typeProfessional);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }
}
