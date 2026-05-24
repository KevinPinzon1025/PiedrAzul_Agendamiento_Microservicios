package co.unicauca.Controller;

import co.unicauca.Entity.model.Patient;
import co.unicauca.Repository.IPatientRepository;
import co.unicauca.Service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/appointment")
public class PatientController {


    private PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/patients")
    public ResponseEntity<List<Patient>> listAll() {
        return ResponseEntity.ok(patientService.findAll());
    }
}
