package co.unicauca.application.service;

import co.unicauca.application.dto.*;
import co.unicauca.domain.exception.ProfessionalNotFoundException;
import co.unicauca.domain.model.Holiday;
import co.unicauca.domain.model.Professional;
import co.unicauca.domain.model.SchedulingConfiguration;
import co.unicauca.domain.model.WorkingDay;
import co.unicauca.domain.service.AvailableSlotsGenerator;
import co.unicauca.domain.valueobject.*;
import co.unicauca.port.in.AvailabilityManagementPort;
import co.unicauca.port.in.HolidayManagementPort;
import co.unicauca.port.in.ProfessionalManagementPort;
import co.unicauca.port.in.SchedulingConfigurationPort;
import co.unicauca.port.out.HolidayRepositoryPort;
import co.unicauca.port.out.ProfessionalRepositoryPort;
import co.unicauca.port.out.ProfessionalEventPublisherPort;
import co.unicauca.port.out.SchedulingConfigurationRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class ProfessionalApplicationService implements ProfessionalManagementPort,
        AvailabilityManagementPort,
        SchedulingConfigurationPort,
        HolidayManagementPort {

    private final ProfessionalRepositoryPort professionalRepository;
    private final ProfessionalEventPublisherPort eventPublisher;
    private final SchedulingConfigurationRepositoryPort configurationRepository;
    private final HolidayRepositoryPort holidayRepository;
    private final AvailableSlotsGenerator slotsGenerator;

    public ProfessionalApplicationService(ProfessionalRepositoryPort professionalRepository,
                                          ProfessionalEventPublisherPort eventPublisher,
                                          SchedulingConfigurationRepositoryPort configurationRepository,
                                          HolidayRepositoryPort holidayRepository,
                                          AvailableSlotsGenerator slotsGenerator) {
        this.professionalRepository = professionalRepository;
        this.eventPublisher = eventPublisher;
        this.configurationRepository = configurationRepository;
        this.holidayRepository = holidayRepository;
        this.slotsGenerator = slotsGenerator;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProfessionalResponse> findAll() {
        return professionalRepository.findAll().stream().map(this::toProfessionalResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProfessionalResponse findById(Long id) {
        return toProfessionalResponse(getProfessional(id));
    }

    @Override
    public ProfessionalResponse create(ProfessionalRequest request) {
        Professional professional = Professional.createNew(
                new ProfessionalName(request.name()),
                new ProfessionalEmail(request.email()),
                ProfessionalType.from(request.type())
        );
        Professional saved = professionalRepository.save(professional);
        eventPublisher.publishProfessionalCreated(saved);
        return toProfessionalResponse(saved);
    }

    @Override
    public ProfessionalResponse update(Long id, ProfessionalRequest request) {
        Professional professional = getProfessional(id);
        professional.updateBasicInformation(
                new ProfessionalName(request.name()),
                new ProfessionalEmail(request.email()),
                ProfessionalType.from(request.type())
        );
        Professional saved = professionalRepository.save(professional);
        eventPublisher.publishProfessionalUpdated(saved);
        return toProfessionalResponse(saved);
    }

    @Override
    public void delete(Long id) {
        ProfessionalId professionalId = new ProfessionalId(id);
        if (!professionalRepository.existsById(professionalId)) {
            throw new ProfessionalNotFoundException(id);
        }
        professionalRepository.deleteById(professionalId);
    }

    @Override
    public AvailabilityResponse configureAvailability(Long professionalId, List<WorkingDayRequest> workingDays) {
        Professional professional = getProfessional(professionalId);
        professional.configureAvailability(toDomainWorkingDays(workingDays));
        Professional saved = professionalRepository.save(professional);
        return toAvailabilityResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AvailabilityResponse getAvailability(Long professionalId) {
        return toAvailabilityResponse(getProfessional(professionalId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SlotResponse> getAvailableSlots(Long professionalId, LocalDate date) {
        Professional professional = getProfessional(professionalId);
        SchedulingConfiguration config = configurationRepository.getConfiguration();
        LocalDate today = LocalDate.now();
        Set<LocalDate> holidays = holidayRepository.findDatesBetween(today, today.plusWeeks(config.getAutonomousSchedulingWindow().weeks()));
        return slotsGenerator.generateForDate(
                professional,
                date,
                today,
                config.getAutonomousSchedulingWindow(),
                holidays
        ).stream().map(slot -> new SlotResponse(slot.date(), slot.startTime(), slot.endTime(), slot.available())).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SchedulingConfigurationResponse getConfiguration() {
        SchedulingConfiguration config = configurationRepository.getConfiguration();
        return new SchedulingConfigurationResponse(config.getAutonomousSchedulingWindow().weeks());
    }

    @Override
    public SchedulingConfigurationResponse updateConfiguration(SchedulingConfigurationRequest request) {
        SchedulingConfiguration saved = configurationRepository.save(
                new SchedulingConfiguration(1L, new SchedulingWindow(request.autonomousSchedulingWindowWeeks()))
        );
        return new SchedulingConfigurationResponse(saved.getAutonomousSchedulingWindow().weeks());
    }

    @Override
    @Transactional(readOnly = true)
    public List<HolidayResponse> findAllHolidays() {
        return holidayRepository.findAll().stream()
                .map(holiday -> new HolidayResponse(holiday.getDate(), holiday.getName()))
                .toList();
    }

    @Override
    public HolidayResponse create(HolidayRequest request) {
        Holiday saved = holidayRepository.save(new Holiday(request.date(), request.name()));
        return new HolidayResponse(saved.getDate(), saved.getName());
    }

    @Override
    public void delete(LocalDate date) {
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
