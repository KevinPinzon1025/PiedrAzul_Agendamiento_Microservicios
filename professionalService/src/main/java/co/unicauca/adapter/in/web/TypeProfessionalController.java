package co.unicauca.adapter.in.web;

import co.unicauca.domain.valueobject.ProfessionalType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/types")
public class TypeProfessionalController {
    @GetMapping
    public ResponseEntity<List<String>> listAll() {
        return ResponseEntity.ok(Arrays.stream(ProfessionalType.values()).map(Enum::name).toList());
    }
}
