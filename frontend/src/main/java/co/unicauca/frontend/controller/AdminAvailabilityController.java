package co.unicauca.frontend.controller;

import co.unicauca.frontend.client.AdminHttpClient;
import co.unicauca.frontend.dto.WorkingDayDTO;
import co.unicauca.frontend.view.AdminAvailabilityFrame;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AdminAvailabilityController {

    private final AdminAvailabilityFrame view;

    private final AdminHttpClient client;

    public AdminAvailabilityController(
            AdminAvailabilityFrame view
    ) {

        this.view = view;

        this.client = new AdminHttpClient();
    }

    public void initData() {

        try {

            view.cbProfessional.getItems().addAll(
                    client.getAllProfessionals()
            );

        } catch (Exception e) {

            view.lblFeedback.setText(
                    "Error cargando profesionales."
            );
        }
    }

    public void saveAvailability() {

        try {

            String professionalName =
                    view.cbProfessional.getValue();

            if (professionalName == null ||
                    professionalName.isBlank()) {

                view.lblFeedback.setText(
                        "Seleccione un profesional."
                );

                return;
            }

            Long professionalId =
                    client.getProfessionalIdByName(
                            professionalName
                    );

            List<WorkingDayDTO> workingDays =
                    buildWorkingDays();

            if (workingDays.isEmpty()) {

                view.lblFeedback.setText(
                        "Debe agregar al menos una franja."
                );

                return;
            }

            client.configureAvailability(
                    professionalId,
                    workingDays
            );

            view.lblFeedback.setText(
                    "Disponibilidad configurada correctamente."
            );

            clearForm();
            loadConfiguredAvailability();

        } catch (Exception e) {

            e.printStackTrace();

            view.lblFeedback.setText(
                    "Error configurando disponibilidad."
            );
        }
    }

    private List<WorkingDayDTO> buildWorkingDays() {

        List<WorkingDayDTO> list =
                new ArrayList<>();

        view.rows.forEach(row -> {

            try {

                String day =
                        row.cbDay.getValue();

                String start =
                        row.cbStartHour.getValue();

                String end =
                        row.cbEndHour.getValue();

                String duration =
                        row.cbDuration.getValue();

                if (day == null ||
                        start == null ||
                        end == null ||
                        duration == null) {

                    return;
                }

                WorkingDayDTO dto =
                        new WorkingDayDTO();

                dto.setDayOfWeek(
                        parseDayOfWeek(day)
                );

                dto.setStartTime(
                        LocalTime.parse(start)
                );

                dto.setEndTime(
                        LocalTime.parse(end)
                );

                dto.setAppointmentDurationMinutes(
                        Integer.parseInt(duration)
                );

                list.add(dto);

            } catch (Exception ignored) {

            }
        });

        return list;
    }

    public void loadConfiguredAvailability() {

        try {

            List<WorkingDayDTO> workingDays =
                    client.getConfiguredAvailability()
                            .stream()
                            .sorted(
                                    Comparator
                                            .comparing(
                                                    WorkingDayDTO::getProfessionalName,
                                                    Comparator.nullsLast(
                                                            String::compareToIgnoreCase
                                                    )
                                            )
                                            .thenComparing(
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

            view.setConfiguredAvailability(workingDays);

        } catch (Exception e) {

            e.printStackTrace();

            view.showConfiguredAvailabilityError(
                    "No se pudieron cargar las franjas horarias."
            );
        }
    }

    public void updateDuration(
            WorkingDayDTO workingDay,
            Integer duration
    ) {

        try {

            workingDay.setAppointmentDurationMinutes(duration);

            client.updateAvailability(
                    workingDay.getId(),
                    workingDay
            );

            view.lblFeedback.setText(
                    "Duración actualizada correctamente."
            );

            loadConfiguredAvailability();

        } catch (Exception e) {

            e.printStackTrace();

            view.showConfiguredAvailabilityError(
                    "No se pudo actualizar la duración."
            );
        }
    }

    public void deleteAvailability(WorkingDayDTO workingDay) {

        try {

            client.deleteAvailability(
                    workingDay.getId()
            );

            view.lblFeedback.setText(
                    "Franja horaria eliminada correctamente."
            );

            loadConfiguredAvailability();

        } catch (Exception e) {

            e.printStackTrace();

            view.showConfiguredAvailabilityError(
                    "No se pudo eliminar la franja horaria."
            );
        }
    }

    private DayOfWeek parseDayOfWeek(String value) {

        return switch (value) {
            case "Lunes" -> DayOfWeek.MONDAY;
            case "Martes" -> DayOfWeek.TUESDAY;
            case "Miércoles" -> DayOfWeek.WEDNESDAY;
            case "Jueves" -> DayOfWeek.THURSDAY;
            case "Viernes" -> DayOfWeek.FRIDAY;
            case "Sábado" -> DayOfWeek.SATURDAY;
            case "Domingo" -> DayOfWeek.SUNDAY;
            default -> DayOfWeek.valueOf(value);
        };
    }

    public void clearForm() {

        view.cbProfessional.setValue(null);

        view.containerRows.getChildren().clear();

        view.rows.clear();

        view.addRow();
    }
}
