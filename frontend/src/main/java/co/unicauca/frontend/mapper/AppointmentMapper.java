package co.unicauca.frontend.mapper;

import co.unicauca.frontend.dto.AppointmentDTO;
import co.unicauca.frontend.viewmodel.AppointmentViewModel;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class AppointmentMapper {

    public static AppointmentViewModel toViewModel(AppointmentDTO dto) {
        AppointmentViewModel vm = new AppointmentViewModel();

        vm.patientId = dto.getPatient() != null
                ? String.valueOf(dto.getPatient().getId())
                : "";

        vm.patientName = dto.getPatient() != null
                ? dto.getPatient().getPatName()
                : "";

        vm.doctorName = dto.getProfessional() != null
                ? dto.getProfessional().getProfName()
                : "";

        if (dto.getAppointmentDate() != null) {
            vm.date = dto.getAppointmentDate().toLocalDate().toString();
            vm.time = dto.getAppointmentDate()
                    .toLocalTime()
                    .format(DateTimeFormatter.ofPattern("HH:mm"));
        }

        return vm;
    }

    public static List<AppointmentViewModel> toViewModelList(List<AppointmentDTO> list) {
        return list.stream()
                .map(AppointmentMapper::toViewModel)
                .collect(Collectors.toList());
    }
}