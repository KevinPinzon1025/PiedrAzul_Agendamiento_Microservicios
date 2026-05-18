package co.unicauca.frontend.controller;

import co.unicauca.frontend.client.AppointmentHttpClient;
import co.unicauca.frontend.dto.AppointmentDTO;
import co.unicauca.frontend.dto.CreateAppointmentRequestDTO;
import co.unicauca.frontend.dto.SchedulingType;
import co.unicauca.frontend.util.AppointmentSubject;
import co.unicauca.frontend.view.ConsultScheduleFrame;
import co.unicauca.frontend.view.ReScheduleAppointmentFrame;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class ReScheduleAppointmentController {

    private final ReScheduleAppointmentFrame view;

    private final AppointmentDTO appointment;

    private final AppointmentHttpClient httpClient;

    public ReScheduleAppointmentController(
            ReScheduleAppointmentFrame view,
            AppointmentDTO appointment
    ) {

        this.view = view;

        this.appointment = appointment;

        this.httpClient =
                new AppointmentHttpClient();
    }

    public void initData() {

        try {

            view.txtPatient.setText(
                    appointment.getPatient().getPatName()
            );

            view.txtProfessional.setText(
                    appointment.getProfessional().getProfName()
            );

            if (appointment.getObservation() != null) {

                view.txtMotivo.setText(
                        appointment.getObservation()
                );
            }

            if (appointment.getAppointmentDate() != null) {

                view.datePicker.setValue(
                        appointment
                                .getAppointmentDate()
                                .toLocalDate()
                );
            }

            loadAvailableHours();

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "No fue posible cargar la información de la cita."
            );
        }
    }

    public void bindTitle(Label title) {

        title.setText(
                "Re-agendar Cita: " +
                        appointment.getIdAppointment()
        );
    }

    public void loadAvailableHours() {

        try {

            view.cbTime.getItems().clear();

            if (view.datePicker.getValue() == null) {

                return;
            }

            Long professionalId =
                    appointment.getProfessional().getId();

            List<String> slots =
                    httpClient.getAvailableSlots(
                            professionalId,
                            view.datePicker.getValue()
                    );

            view.cbTime.getItems().setAll(
                    slots
            );

            if (!slots.isEmpty()) {

                view.cbTime.setValue(
                        slots.get(0)
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Error cargando horarios disponibles."
            );
        }
    }

    public void openScheduleDialog() {

        try {

            String professional =
                    appointment.getProfessional().getProfName();

            LocalDate date =
                    view.datePicker.getValue();

            if (date == null) {

                showError(
                        "Seleccione una fecha primero."
                );

                return;
            }

            ConsultScheduleFrame consultFrame =
                    new ConsultScheduleFrame(
                            new Stage(),
                            professional,
                            date
                    );

            consultFrame.show();

        } catch (Exception ex) {

            ex.printStackTrace();

            showError(
                    "No fue posible abrir la consulta de horarios."
            );
        }
    }

    public void rescheduleAppointment() {

        try {

            LocalDate selectedDate =
                    view.datePicker.getValue();

            String selectedHour =
                    view.cbTime.getValue();

            String observation =
                    view.txtMotivo.getText();

            if (selectedDate == null ||
                    selectedHour == null ||
                    observation == null ||
                    observation.isBlank()) {

                showError(
                        "Complete todos los campos antes de reagendar."
                );

                return;
            }

            Long patientId = this.appointment.getPatient().getId();
            Long professionalId = this.appointment.getProfessional().getId();
            LocalTime selectedTime =
                    LocalTime.parse(selectedHour);

            LocalDateTime appointmentDate =
                    LocalDateTime.of(
                            selectedDate,
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

            request.setSchedulerId(this.appointment.getScheduler().getId());

            httpClient.createAppointment(request);

            AppointmentSubject.notifyObservers();

            showSuccess(
                    "La cita fue reagendada correctamente."
            );

            clearForm();

            // recargar slots para ocultar el recién ocupado
            loadAvailableHours();

        } catch (Exception ex) {

            ex.printStackTrace();

            showError(
                    "Ocurrió un error al reagendar la cita."
            );
        }
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

        Alert alert =
                new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Éxito");

        alert.setHeaderText(null);

        alert.setContentText(message);

        ButtonType acceptButton =
                new ButtonType(
                        "Aceptar",
                        ButtonBar.ButtonData.OK_DONE
                );

        alert.getButtonTypes().setAll(
                acceptButton
        );

        DialogPane dialogPane =
                alert.getDialogPane();

        dialogPane.setStyle(
                "-fx-background-color: #ecfbf1;" +
                        "-fx-background-radius: 14;" +
                        "-fx-border-radius: 14;" +
                        "-fx-padding: 12;"
        );

        dialogPane.lookup(".content.label")
                .setStyle(
                        "-fx-text-fill: #0c5b3f;" +
                                "-fx-font-size: 14px;"
                );

        Button button =
                (Button) dialogPane.lookupButton(
                        acceptButton
                );

        button.setStyle(
                "-fx-background-color: #0c5b3f;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 8 18 8 18;"
        );

        alert.showAndWait();
    }

    public void clearForm() {

        view.datePicker.setValue(
                LocalDate.now()
        );

        view.cbTime.getItems().clear();

        view.txtMotivo.clear();

        view.lblFeedback.setText(" ");
    }
}