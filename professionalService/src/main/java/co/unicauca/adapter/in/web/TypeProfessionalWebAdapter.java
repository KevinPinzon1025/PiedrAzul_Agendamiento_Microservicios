package co.unicauca.adapter.in.web;

import co.unicauca.domain.valueobject.ProfessionalType;
import co.unicauca.port.in.web.TypeProfessionalWebPort;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class TypeProfessionalWebAdapter implements TypeProfessionalWebPort {

    @Override
    public List<String> listAll() {
        return Arrays.stream(ProfessionalType.values())
                .map(Enum::name)
                .toList();
    }
}
