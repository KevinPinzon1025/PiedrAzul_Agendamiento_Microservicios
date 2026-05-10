

package co.unicauca.frontend.controller;

import co.unicauca.frontend.client.AppointmentHttpClient;
import co.unicauca.frontend.dto.CreateAppointmentRequestDTO;
import co.unicauca.frontend.dto.SchedulingType;
import co.unicauca.frontend.view.ScheduleAppointmentFrame;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class ScheduleAppointmentController {

    private final ScheduleAppointmentFrame view;

    private final AppointmentHttpClient httpClient;

    public ScheduleAppointmentController(
            ScheduleAppointmentFrame view
    ) {

        this.view = view;

        this.httpClient = new AppointmentHttpClient();
    }

    public void initData() {

        try {

            List<String> patients =
                    httpClient.getAllPatients();

            view.cbPatient.getItems().setAll(
                    patients
            );

            List<String> professionals =
                    httpClient.getAllProfessionals();

            view.cbProfessional.getItems().setAll(
                    professionals
            );

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "No se pudieron cargar pacientes y profesionales."
            );
        }
    }

    public void loadAvailableHours() {

        try {

            view.cbTime.getItems().clear();

            if (view.cbProfessional.getValue() == null ||
                    view.datePicker.getValue() == null) {

                return;
            }

            Long professionalId =
                    httpClient.getProfessionalIdByName(
                            view.cbProfessional.getValue()
                    );

            List<String> slots =
                    httpClient.getAvailableSlots(
                            professionalId,
                            view.datePicker.getValue()
                    );

            view.cbTime.getItems().setAll(slots);

            if (!slots.isEmpty()) {

                view.cbTime.setValue(slots.get(0));
            }

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Error cargando horarios disponibles."
            );
        }
    }

    public void registerAppointment() {

        try {

            String patientName =
                    view.cbPatient.getValue();

            String professionalName =
                    view.cbProfessional.getValue();

            LocalDate date =
                    view.datePicker.getValue();

            String time =
                    view.cbTime.getValue();

            String observation =
                    view.txtMotivo.getText();

            if (patientName == null ||
                    professionalName == null ||
                    date == null ||
                    time == null ||
                    observation == null ||
                    observation.isBlank()) {

                showError(
                        "Por favor complete todos los campos antes de registrar la cita."
                );

                return;
            }

            Long patientId =
                    httpClient.getPatientIdByName(
                            patientName
                    );

            Long professionalId =
                    httpClient.getProfessionalIdByName(
                            professionalName
                    );

            LocalTime selectedTime =
                    LocalTime.parse(time);

            LocalDateTime appointmentDate =
                    LocalDateTime.of(
                            date,
                            selectedTime
                    );

            CreateAppointmentRequestDTO request =
                    new CreateAppointmentRequestDTO();

            request.setPatientId(patientId);

            request.setProfessionalId(
                    professionalId
            );

            request.setObservation(
                    observation
            );

            request.setAppointmentDate(
                    appointmentDate
            );

            request.setSchedulingType(
                    SchedulingType.MANUAL
            );

            // TODO temporal
            request.setSchedulerId(1L);

            httpClient.createAppointment(request);

            showSuccess(
                    "La cita se registró correctamente."
            );

            clearForm();

            // recargar slots para ocultar el recién ocupado
            loadAvailableHours();

        } catch (Exception ex) {

            ex.printStackTrace();

            showError(
                    "Ocurrió un error al registrar la cita."
            );
        }
    }

    public void clearForm() {

        view.cbPatient.getSelectionModel()
                .clearSelection();

        view.cbProfessional.getSelectionModel()
                .clearSelection();

        view.datePicker.setValue(
                LocalDate.now()
        );

        view.cbTime.getItems().clear();

        view.txtMotivo.clear();

        view.lblFeedback.setText(" ");
    }

    private void showError(String message) {

        view.lblFeedback.setStyle(
                "-fx-text-fill: #8f1d1d;" +
                        "-fx-background-color: #fff2f2;" +
                        "-fx-background-radius: 14;" +
                        "-fx-padding: 12;"
        );

        view.lblFeedback.setText(message);
    }

    private void showSuccess(String message) {

        view.lblFeedback.setStyle(
                "-fx-text-fill: #0c5b3f;" +
                        "-fx-background-color: #ecfbf1;" +
                        "-fx-background-radius: 14;" +
                        "-fx-padding: 12;"
        );

        view.lblFeedback.setText(message);
    }
}