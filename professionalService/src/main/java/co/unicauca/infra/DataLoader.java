package co.unicauca.infra;

import co.unicauca.Entity.model.Professional;
import co.unicauca.Entity.model.TypeProfessional;
import co.unicauca.Repository.IProfessionalRepository;
import co.unicauca.Repository.ITypeProfessionalRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final ITypeProfessionalRepository typeRepository;
    private final IProfessionalRepository professionalRepository;

    public DataLoader(ITypeProfessionalRepository typeRepository,
                      IProfessionalRepository professionalRepository) {
        this.typeRepository = typeRepository;
        this.professionalRepository = professionalRepository;
    }

    @Override
    public void run(String... args) {

        // Tipos profesionales
        TypeProfessional medico = new TypeProfessional();
        medico.setName("Médico");
        typeRepository.save(medico);

        TypeProfessional terapista = new TypeProfessional();
        terapista.setName("Terapista");
        typeRepository.save(terapista);

        // Profesionales 
        Professional p1 = new Professional();
        p1.setName("Juan Pérez");
        p1.setEmail("juan.perez@unicauca.edu.co");
        p1.setType(medico);
        professionalRepository.save(p1);

        Professional p2 = new Professional();
        p2.setName("María López");
        p2.setEmail("maria.lopez@unicauca.edu.co");
        p2.setType(terapista);
        professionalRepository.save(p2);

        Professional p3 = new Professional();
        p3.setName("Carlos Rodríguez");
        p3.setEmail("carlos.rodriguez@unicauca.edu.co");
        p3.setType(terapista);
        professionalRepository.save(p3);

        Professional p4 = new Professional();
        p4.setName("Ana Martínez");
        p4.setEmail("ana.martinez@unicauca.edu.co");
        p4.setType(medico);
        professionalRepository.save(p4);

    }
}
