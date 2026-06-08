package co.unicauca.adapter.in.web;

import co.unicauca.application.dto.HolidayRequest;
import co.unicauca.application.dto.HolidayResponse;
import co.unicauca.application.service.ProfessionalApplicationService;
import co.unicauca.port.in.web.HolidayWebPort;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class HolidayWebAdapter implements HolidayWebPort {

    private final ProfessionalApplicationService professionalApplicationService;

    public HolidayWebAdapter(
            ProfessionalApplicationService professionalApplicationService
    ) {
        this.professionalApplicationService = professionalApplicationService;
    }

    @Override
    public List<HolidayResponse> findAll() {
        return professionalApplicationService.findAllHolidays();
    }

    @Override
    public HolidayResponse create(HolidayRequest request) {
        return professionalApplicationService.createHoliday(request);
    }

    @Override
    public void delete(LocalDate date) {
        professionalApplicationService.deleteHoliday(date);
    }
}
