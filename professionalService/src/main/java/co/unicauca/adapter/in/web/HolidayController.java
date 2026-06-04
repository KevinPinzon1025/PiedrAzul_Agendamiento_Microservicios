package co.unicauca.adapter.in.web;

import co.unicauca.application.dto.HolidayRequest;
import co.unicauca.application.dto.HolidayResponse;
import co.unicauca.port.in.HolidayManagementPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/holidays")
public class HolidayController {
    private final HolidayManagementPort holidayManagementPort;

    public HolidayController(HolidayManagementPort holidayManagementPort) {
        this.holidayManagementPort = holidayManagementPort;
    }

    @GetMapping
    public ResponseEntity<List<HolidayResponse>> findAll() {
        return ResponseEntity.ok(holidayManagementPort.findAllHolidays());
    }

    @PostMapping
    public ResponseEntity<HolidayResponse> create(@RequestBody HolidayRequest request) {
        HolidayResponse created = holidayManagementPort.create(request);
        return ResponseEntity.created(URI.create("/holidays/" + created.date())).body(created);
    }

    @DeleteMapping("/{date}")
    public ResponseEntity<Void> delete(@PathVariable LocalDate date) {
        holidayManagementPort.delete(date);
        return ResponseEntity.noContent().build();
    }
}
