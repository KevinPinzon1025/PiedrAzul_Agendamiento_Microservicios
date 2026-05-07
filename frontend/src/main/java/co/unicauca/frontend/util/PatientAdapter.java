package co.unicauca.frontend.util;

import co.unicauca.frontend.dto.PatientDTO;

import java.util.List;
import java.util.stream.Collectors;

public class PatientAdapter implements IAdapter<List<PatientDTO>,List<String>>{
    @Override
    public List<String> adapt(List<PatientDTO> patients) {
        return patients.stream()
                .map(PatientDTO::getPatName)
                .collect(Collectors.toList());
    }
}
