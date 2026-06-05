package co.unicauca.Service;

import co.unicauca.Entity.model.Appointment;
import co.unicauca.Entity.model.Professional;
import co.unicauca.Entity.model.ProfessionalWorkingDay;
import co.unicauca.Repository.IAppointmentRepository;
import co.unicauca.Repository.IProfessionalRepository;
import co.unicauca.Repository.IProfessionalWorkingDayRepository;
import co.unicauca.infra.dto.SlotResponseDTO;
import co.unicauca.infra.dto.WorkingDayDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Service
public class ProfessionalWorkingDayService {

    private final IProfessionalWorkingDayRepository workingDayRepository;
    private final IProfessionalRepository professionalRepository;
    private final IAppointmentRepository appointmentRepository;

    public ProfessionalWorkingDayService(
            IProfessionalWorkingDayRepository workingDayRepository,
            IProfessionalRepository professionalRepository,
            IAppointmentRepository appointmentRepository
    ) {
        this.workingDayRepository = workingDayRepository;
        this.professionalRepository = professionalRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Transactional
    public void configureAvailability(
            Long professionalId,
            List<WorkingDayDTO> workingDays
    ) {

        Professional professional = professionalRepository
                .findById(professionalId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Profesional no encontrado"
                        ));

        workingDayRepository
                .deleteByProfessionalId(professionalId);

        for (WorkingDayDTO dto : workingDays) {

            ProfessionalWorkingDay entity =
                    new ProfessionalWorkingDay();

            entity.setProfessional(professional);

            entity.setDayOfWeek(dto.getDayOfWeek());

            entity.setStartTime(dto.getStartTime());

            entity.setEndTime(dto.getEndTime());

            entity.setAppointmentDurationMinutes(
                    dto.getAppointmentDurationMinutes()
            );

            workingDayRepository.save(entity);
        }
    }

    public List<SlotResponseDTO> getAvailableSlots(
            Long professionalId,
            LocalDate date
    ) {

        DayOfWeek dayOfWeek =
                date.getDayOfWeek();

        List<ProfessionalWorkingDay> workingDays =
                workingDayRepository
                        .findByProfessionalIdAndDayOfWeek(
                                professionalId,
                                dayOfWeek
                        );

        List<SlotResponseDTO> availableSlots =
                new ArrayList<>();

        if (workingDays.isEmpty()) {
            return availableSlots;
        }

        List<Appointment> appointments =
                appointmentRepository
                        .findByProfessionalIdAndDate(
                                professionalId,
                                date
                        );

        for (ProfessionalWorkingDay workingDay : workingDays) {

            LocalTime current =
                    workingDay.getStartTime();

            LocalTime end =
                    workingDay.getEndTime();

            Integer duration =
                    workingDay
                            .getAppointmentDurationMinutes();

            while (
                    current.plusMinutes(duration)
                            .isBefore(end)
                            ||
                            current.plusMinutes(duration)
                                    .equals(end)
            ) {

                LocalTime slotEnd =
                        current.plusMinutes(duration);

                boolean occupied = false;

                for (Appointment appointment : appointments) {

                    LocalTime appointmentTime =
                            appointment
                                    .getAppointmentDate()
                                    .toLocalTime();

                    if (appointmentTime.equals(current)) {

                        occupied = true;
                        break;
                    }
                }

                if (!occupied) {

                    availableSlots.add(
                            new SlotResponseDTO(
                                    current,
                                    slotEnd,
                                    true
                            )
                    );
                }

                current =
                        current.plusMinutes(duration);
            }
        }

        return availableSlots;
    }

    public List<WorkingDayDTO> findAllConfiguredSlots() {

        return workingDayRepository.findAll()
                .stream()
                .sorted(
                        Comparator
                                .comparing((ProfessionalWorkingDay day) ->
                                        day.getProfessional().getProfName())
                                .thenComparing(ProfessionalWorkingDay::getDayOfWeek)
                                .thenComparing(ProfessionalWorkingDay::getStartTime)
                )
                .map(this::toDTO)
                .toList();
    }

    @Transactional
    public WorkingDayDTO updateWorkingDay(
            Long id,
            WorkingDayDTO dto
    ) {

        ProfessionalWorkingDay workingDay =
                workingDayRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Franja horaria no encontrada"
                                ));

        if (dto.getDayOfWeek() != null) {
            workingDay.setDayOfWeek(dto.getDayOfWeek());
        }

        if (dto.getStartTime() != null) {
            workingDay.setStartTime(dto.getStartTime());
        }

        if (dto.getEndTime() != null) {
            workingDay.setEndTime(dto.getEndTime());
        }

        if (dto.getAppointmentDurationMinutes() != null) {
            workingDay.setAppointmentDurationMinutes(
                    dto.getAppointmentDurationMinutes()
            );
        }

        return toDTO(workingDayRepository.save(workingDay));
    }

    @Transactional
    public void deleteWorkingDay(Long id) {

        if (!workingDayRepository.existsById(id)) {
            throw new RuntimeException(
                    "Franja horaria no encontrada"
            );
        }

        workingDayRepository.deleteById(id);
    }

    private WorkingDayDTO toDTO(ProfessionalWorkingDay workingDay) {

        WorkingDayDTO dto =
                new WorkingDayDTO();

        dto.setId(workingDay.getId());
        dto.setProfessionalId(
                workingDay.getProfessional().getId()
        );
        dto.setProfessionalName(
                workingDay.getProfessional().getProfName()
        );
        dto.setDayOfWeek(workingDay.getDayOfWeek());
        dto.setStartTime(workingDay.getStartTime());
        dto.setEndTime(workingDay.getEndTime());
        dto.setAppointmentDurationMinutes(
                workingDay.getAppointmentDurationMinutes()
        );

        return dto;
    }
}
