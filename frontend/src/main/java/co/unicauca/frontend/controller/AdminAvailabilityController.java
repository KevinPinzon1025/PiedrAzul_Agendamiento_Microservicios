package co.unicauca.frontend.controller;

import co.unicauca.frontend.client.AdminHttpClient;
import co.unicauca.frontend.dto.WorkingDayDTO;
import co.unicauca.frontend.view.AdminAvailabilityFrame;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
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
                        DayOfWeek.valueOf(day)
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

    public void clearForm() {

        view.cbProfessional.setValue(null);

        view.containerRows.getChildren().clear();

        view.rows.clear();

        view.addRow();
    }
}