package co.unicauca.application.service;

import co.unicauca.application.dto.*;
import co.unicauca.domain.exception.ProfessionalNotFoundException;
import co.unicauca.domain.model.Holiday;
import co.unicauca.domain.model.Professional;
import co.unicauca.domain.model.SchedulingConfiguration;
import co.unicauca.domain.model.WorkingDay;
import co.unicauca.domain.service.AvailableSlotsGenerator;
import co.unicauca.domain.valueobject.*;
import co.unicauca.port.out.db.HolidayDbPort;
import co.unicauca.port.out.db.ProfessionalDbPort;
import co.unicauca.port.out.db.SchedulingConfigurationDbPort;
import co.unicauca.port.out.notification.ProfessionalNotificationPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class ProfessionalApplicationService {

    private final ProfessionalDbPort professionalRepository;
    private final ProfessionalNotificationPort notificationPort;
    private final SchedulingConfigurationDbPort configurationRepository;
    private final HolidayDbPort holidayRepository;
    private final AvailableSlotsGenerator slotsGenerator;

    public ProfessionalApplicationService(ProfessionalDbPort professionalRepository,
                                          ProfessionalNotificationPort notificationPort,
                                          SchedulingConfigurationDbPort configurationRepository,
                                          HolidayDbPort holidayRepository,
                                          AvailableSlotsGenerator slotsGenerator) {
        this.professionalRepository = professionalRepository;
        this.notificationPort = notificationPort;
        this.configurationRepository = configurationRepository;
        this.holidayRepository = holidayRepository;
        this.slotsGenerator = slotsGenerator;
    }

    @Transactional(readOnly = true)
    public List<ProfessionalResponse> findAll() {
        return professionalRepository.findAll().stream().map(this::toProfessionalResponse).toList();
    }

    @Transactional(readOnly = true)
    public ProfessionalResponse findById(Long id) {
        return toProfessionalResponse(getProfessional(id));
    }

    public ProfessionalResponse create(ProfessionalRequest request) {
        Professional professional = Professional.createNew(
                new ProfessionalName(request.name()),
                new ProfessionalEmail(request.email()),
                ProfessionalType.from(request.type())
        );
        Professional saved = professionalRepository.save(professional);
        notificationPort.publishProfessionalCreated(saved);
        return toProfessionalResponse(saved);
    }

    public ProfessionalResponse update(Long id, ProfessionalRequest request) {
        Professional professional = getProfessional(id);
        professional.updateBasicInformation(
                new ProfessionalName(request.name()),
                new ProfessionalEmail(request.email()),
                ProfessionalType.from(request.type())
        );
        Professional saved = professionalRepository.save(professional);
        notificationPort.publishProfessionalUpdated(saved);
        return toProfessionalResponse(saved);
    }

    public void delete(Long id) {
        ProfessionalId professionalId = new ProfessionalId(id);
        if (!professionalRepository.existsById(professionalId)) {
            throw new ProfessionalNotFoundException(id);
        }
        professionalRepository.deleteById(professionalId);
    }

    public AvailabilityResponse configureAvailability(Long professionalId, List<WorkingDayRequest> workingDays) {
        Professional professional = getProfessional(professionalId);
        professional.configureAvailability(toDomainWorkingDays(workingDays));
        Professional saved = professionalRepository.save(professional);
        notificationPort.publishProfessionalAvailabilityConfigured(saved);
        return toAvailabilityResponse(saved);
    }

    @Transactional(readOnly = true)
    public AvailabilityResponse getAvailability(Long professionalId) {
        return toAvailabilityResponse(getProfessional(professionalId));
    }

    @Transactional(readOnly = true)
    public List<SlotResponse> getAvailableSlots(Long professionalId, LocalDate date) {
        Professional professional = getProfessional(professionalId);
        SchedulingConfiguration config = configurationRepository.getConfiguration();
        LocalDate today = LocalDate.now();
        Set<LocalDate> holidays = Set.copyOf(
                holidayRepository.findDatesBetween(
                        today,
                        today.plusWeeks(config.getAutonomousSchedulingWindow().weeks())
                )
        );
        return slotsGenerator.generateForDate(
                professional,
                date,
                today,
                config.getAutonomousSchedulingWindow(),
                holidays
        ).stream().map(slot -> new SlotResponse(slot.date(), slot.startTime(), slot.endTime(), slot.available())).toList();
    }

    @Transactional(readOnly = true)
    public SchedulingConfigurationResponse getConfiguration() {
        SchedulingConfiguration config = configurationRepository.getConfiguration();
        return new SchedulingConfigurationResponse(config.getAutonomousSchedulingWindow().weeks());
    }

    public SchedulingConfigurationResponse updateConfiguration(SchedulingConfigurationRequest request) {
        SchedulingConfiguration saved = configurationRepository.save(
                new SchedulingConfiguration(1L, new SchedulingWindow(request.autonomousSchedulingWindowWeeks()))
        );
        return new SchedulingConfigurationResponse(saved.getAutonomousSchedulingWindow().weeks());
    }

    @Transactional(readOnly = true)
    public List<HolidayResponse> findAllHolidays() {
        return holidayRepository.findAll().stream()
                .map(holiday -> new HolidayResponse(holiday.getDate(), holiday.getName()))
                .toList();
    }

    public HolidayResponse createHoliday(HolidayRequest request) {
        Holiday saved = holidayRepository.save(new Holiday(request.date(), request.name()));
        return new HolidayResponse(saved.getDate(), saved.getName());
    }

    public void deleteHoliday(LocalDate date) {
        holidayRepository.deleteByDate(date);
    }

    private Professional getProfessional(Long id) {
        return professionalRepository.findById(new ProfessionalId(id))
                .orElseThrow(() -> new ProfessionalNotFoundException(id));
    }

    private List<WorkingDay> toDomainWorkingDays(List<WorkingDayRequest> requests) {
        if (requests == null) {
            return List.of();
        }
        return requests.stream()
                .map(request -> new WorkingDay(
                        request.dayOfWeek(),
                        new TimeRange(request.startTime(), request.endTime()),
                        new AppointmentDuration(request.appointmentDurationMinutes())
                ))
                .toList();
    }

    private ProfessionalResponse toProfessionalResponse(Professional professional) {
        return new ProfessionalResponse(
                professional.getId() == null ? null : professional.getId().value(),
                professional.getName().value(),
                professional.getEmail().value(),
                professional.getType().name()
        );
    }

    private AvailabilityResponse toAvailabilityResponse(Professional professional) {
        List<WorkingDayResponse> workingDays = professional.getWorkingDays().stream()
                .map(day -> new WorkingDayResponse(
                        null,
                        day.getDayOfWeek(),
                        day.getTimeRange().startTime(),
                        day.getTimeRange().endTime(),
                        day.getAppointmentDuration().minutes()
                ))
                .toList();
        return new AvailabilityResponse(professional.getId().value(), workingDays);
    }
}

