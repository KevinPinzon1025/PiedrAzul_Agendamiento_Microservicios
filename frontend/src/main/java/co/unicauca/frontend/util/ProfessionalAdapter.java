package co.unicauca.frontend.util;

import co.unicauca.frontend.dto.ProfessionalDTO;

import java.util.List;
import java.util.stream.Collectors;

public class ProfessionalAdapter implements IAdapter<List<ProfessionalDTO>, List<String>>{

    @Override
    public List<String> adapt(List<ProfessionalDTO> professionals) {
        return professionals.stream()
                .map(ProfessionalDTO::getProfName)
                .collect(Collectors.toList());
    }
}
