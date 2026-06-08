package co.unicauca.port.in.web;

import co.unicauca.application.dto.HolidayRequest;
import co.unicauca.application.dto.HolidayResponse;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

public interface HolidayWebPort {

    @GetMapping("/holidays")
    List<HolidayResponse> findAll();

    @PostMapping("/holidays")
    HolidayResponse create(@RequestBody HolidayRequest request);

    @DeleteMapping("/holidays/{date}")
    void delete(@PathVariable LocalDate date);
}
