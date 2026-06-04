package co.unicauca.port.in;

import co.unicauca.application.dto.HolidayRequest;
import co.unicauca.application.dto.HolidayResponse;

import java.time.LocalDate;
import java.util.List;

public interface HolidayManagementPort {
    List<HolidayResponse> findAllHolidays();
    HolidayResponse create(HolidayRequest request);
    void delete(LocalDate date);
}
