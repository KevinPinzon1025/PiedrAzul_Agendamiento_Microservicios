package co.unicauca.adapter.in.web;

import co.unicauca.application.dto.ProfessionalRequest;
import co.unicauca.application.dto.ProfessionalResponse;
import co.unicauca.application.service.ProfessionalApplicationService;
import co.unicauca.port.in.web.ProfessionalWebPort;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProfessionalWebAdapter implements ProfessionalWebPort {

    private final ProfessionalApplicationService professionalApplicationService;

    public ProfessionalWebAdapter(
            ProfessionalApplicationService professionalApplicationService
    ) {
        this.professionalApplicationService = professionalApplicationService;
    }

    @Override
    public List<ProfessionalResponse> findAll() {
        return professionalApplicationService.findAll();
    }

    @Override
    public ProfessionalResponse findById(Long id) {
        return professionalApplicationService.findById(id);
    }

    @Override
    public ProfessionalResponse create(ProfessionalRequest request) {
        return professionalApplicationService.create(request);
    }

    @Override
    public ProfessionalResponse update(
            Long id,
            ProfessionalRequest request
    ) {
        return professionalApplicationService.update(id, request);
    }

    @Override
    public void delete(Long id) {
        professionalApplicationService.delete(id);
    }
}
