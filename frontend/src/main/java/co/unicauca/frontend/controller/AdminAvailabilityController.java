package co.unicauca.frontend.controller;

import co.unicauca.frontend.client.AdminHttpClient;
import co.unicauca.frontend.dto.WorkingDayDTO;
import co.unicauca.frontend.view.AdminAvailabilityFrame;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AdminAvailabilityController {

    private final AdminAvailabilityFrame view;
    private final AdminHttpClient client;

    public AdminAvailabilityController(AdminAvailabilityFrame view) {
        this.view = view;
        this.client = new AdminHttpClient();
    }

    public void initData() {

        try {
            view.setProfessionals(client.getAllProfessionals());
        } catch (Exception e) {
            view.lblFeedback.setText("Error cargando profesionales.");
        }
    }

    public void saveAvailability() {

        try {
            String professionalName = view.getProfessionalSearchText();

            if (professionalName == null ||
                    professionalName.isBlank()) {

                view.lblFeedback.setText("Seleccione un profesional.");
                return;
            }

            Long professionalId =
                    client.getProfessionalIdByName(professionalName);

            List<WorkingDayDTO> workingDays = buildWorkingDays();

            if (workingDays.isEmpty()) {
                view.lblFeedback.setText(
                        "Debe agregar al menos una franja."
                );
                return;
            }

            if (!hasValidTimeRanges(workingDays)) {
                view.lblFeedback.setText(
                        "La hora de inicio debe ser menor que la hora de fin."
                );
                return;
            }

            List<WorkingDayDTO> existingWorkingDays =
                    findAvailabilityForProfessional(professionalId);

            if (hasRepeatedDays(existingWorkingDays, workingDays)) {
                view.lblFeedback.setText(
                        "Solo se permite una franja horaria por dia."
                );
                return;
            }

            List<WorkingDayDTO> availabilityToSave =
                    new ArrayList<>(existingWorkingDays);
            availabilityToSave.addAll(workingDays);

            client.configureAvailability(
                    professionalId,
                    availabilityToSave
            );

            view.lblFeedback.setText(
                    "Disponibilidad configurada correctamente."
            );

            clearRows();
            loadAvailabilityForProfessional(professionalName);

        } catch (Exception e) {
            e.printStackTrace();
            view.lblFeedback.setText(
                    "Error configurando disponibilidad."
            );
        }
    }

    private List<WorkingDayDTO> buildWorkingDays() {

        List<WorkingDayDTO> list = new ArrayList<>();

        view.rows.forEach(row -> {

            try {
                String day = row.cbDay.getValue();
                String start = row.cbStartHour.getValue();
                String end = row.cbEndHour.getValue();
                String duration = row.cbDuration.getValue();

                if (day == null ||
                        start == null ||
                        end == null ||
                        duration == null) {

                    return;
                }

                WorkingDayDTO dto = new WorkingDayDTO();
                dto.setDayOfWeek(parseDayOfWeek(day));
                dto.setStartTime(LocalTime.parse(start));
                dto.setEndTime(LocalTime.parse(end));
                dto.setAppointmentDurationMinutes(
                        Integer.parseInt(duration)
                );

                list.add(dto);

            } catch (Exception ignored) {
            }
        });

        return list;
    }

    public void loadSelectedProfessionalAvailability() {

        String professionalName = view.getProfessionalSearchText();

        if (professionalName == null ||
                professionalName.isBlank()) {

            view.clearConfiguredAvailability();
            return;
        }

        loadAvailabilityForProfessional(professionalName);
    }

    private void loadAvailabilityForProfessional(String professionalName) {

        try {
            Long professionalId =
                    client.getProfessionalIdByName(professionalName);

            List<WorkingDayDTO> workingDays =
                    findAvailabilityForProfessional(professionalId);

            view.setConfiguredAvailability(workingDays);

        } catch (Exception e) {
            e.printStackTrace();
            view.showConfiguredAvailabilityError(
                    "Seleccione un profesional valido para consultar sus franjas."
            );
        }
    }

    private List<WorkingDayDTO> findAvailabilityForProfessional(
            Long professionalId
    ) {

        return client.getConfiguredAvailability()
                .stream()
                .filter(workingDay ->
                        workingDay.getProfessionalId() != null &&
                                workingDay.getProfessionalId()
                                        .equals(professionalId)
                )
                .sorted(
                        Comparator
                                .comparing(
                                        WorkingDayDTO::getDayOfWeek,
                                        Comparator.nullsLast(
                                                Comparator.naturalOrder()
                                        )
                                )
                                .thenComparing(
                                        WorkingDayDTO::getStartTime,
                                        Comparator.nullsLast(
                                                Comparator.naturalOrder()
                                        )
                                )
                )
                .toList();
    }

    private boolean hasValidTimeRanges(
            List<WorkingDayDTO> workingDays
    ) {

        return workingDays.stream()
                .allMatch(workingDay ->
                        workingDay.getStartTime() != null &&
                                workingDay.getEndTime() != null &&
                                workingDay.getStartTime()
                                        .isBefore(
                                                workingDay.getEndTime()
                                        )
                );
    }

    private boolean hasRepeatedDays(
            List<WorkingDayDTO> existingWorkingDays,
            List<WorkingDayDTO> newWorkingDays
    ) {

        Set<DayOfWeek> usedDays = new HashSet<>();

        for (WorkingDayDTO workingDay : existingWorkingDays) {
            if (workingDay.getDayOfWeek() != null &&
                    !usedDays.add(workingDay.getDayOfWeek())) {

                return true;
            }
        }

        for (WorkingDayDTO workingDay : newWorkingDays) {
            if (workingDay.getDayOfWeek() != null &&
                    !usedDays.add(workingDay.getDayOfWeek())) {

                return true;
            }
        }

        return false;
    }

    public void updateAvailability(
            WorkingDayDTO workingDay,
            LocalTime startTime,
            LocalTime endTime,
            Integer duration
    ) {

        try {
            if (workingDay.getId() == null) {
                view.showConfiguredAvailabilityError(
                        "La franja seleccionada no tiene identificador."
                );
                return;
            }

            if (startTime == null ||
                    endTime == null ||
                    duration == null) {

                view.showConfiguredAvailabilityError(
                        "Complete hora de inicio, hora de fin y duracion."
                );
                return;
            }

            if (!startTime.isBefore(endTime)) {
                view.showConfiguredAvailabilityError(
                        "La hora de inicio debe ser menor que la hora de fin."
                );
                return;
            }

            workingDay.setStartTime(startTime);
            workingDay.setEndTime(endTime);
            workingDay.setAppointmentDurationMinutes(duration);

            client.updateAvailability(
                    workingDay.getId(),
                    workingDay
            );

            view.lblFeedback.setText(
                    "Franja horaria actualizada correctamente."
            );

            loadSelectedProfessionalAvailability();

        } catch (Exception e) {
            e.printStackTrace();
            view.showConfiguredAvailabilityError(
                    "No se pudo actualizar la franja horaria."
            );
        }
    }

    private DayOfWeek parseDayOfWeek(String value) {

        return switch (value) {
            case "Lunes" -> DayOfWeek.MONDAY;
            case "Martes" -> DayOfWeek.TUESDAY;
            case "Miercoles" -> DayOfWeek.WEDNESDAY;
            case "Jueves" -> DayOfWeek.THURSDAY;
            case "Viernes" -> DayOfWeek.FRIDAY;
            case "Sabado" -> DayOfWeek.SATURDAY;
            case "Domingo" -> DayOfWeek.SUNDAY;
            default -> DayOfWeek.valueOf(value);
        };
    }

    public void clearForm() {

        view.cbProfessional.setValue(null);
        view.cbProfessional.getSelectionModel().clearSelection();
        view.cbProfessional.getEditor().clear();
        view.clearConfiguredAvailability();
        clearRows();
    }

    private void clearRows() {

        view.containerRows.getChildren().clear();
        view.rows.clear();
        view.addRow();
    }
}
