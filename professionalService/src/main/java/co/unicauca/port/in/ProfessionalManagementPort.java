package co.unicauca.port.in;

import co.unicauca.application.dto.ProfessionalRequest;
import co.unicauca.application.dto.ProfessionalResponse;

import java.util.List;

public interface ProfessionalManagementPort {
    List<ProfessionalResponse> findAll();
    ProfessionalResponse findById(Long id);
    ProfessionalResponse create(ProfessionalRequest request);
    ProfessionalResponse update(Long id, ProfessionalRequest request);
    void delete(Long id);
}
