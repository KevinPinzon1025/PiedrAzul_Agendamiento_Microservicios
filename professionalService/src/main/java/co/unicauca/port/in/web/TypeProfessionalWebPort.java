package co.unicauca.port.in.web;

import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public interface TypeProfessionalWebPort {

    @GetMapping("/types")
    List<String> listAll();
}
