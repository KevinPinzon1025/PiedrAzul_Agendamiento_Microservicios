
package co.unicauca.frontend.controller;

import co.unicauca.frontend.client.AppointmentHttpClient;
import co.unicauca.frontend.dto.CreateAppointmentRequestDTO;
import co.unicauca.frontend.dto.SchedulingType;
import javafx.scene.Node;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import co.unicauca.frontend.view.SelfServiceAppointmentFrame;
import javafx.scene.control.*;

public class SelfServiceAppointmentController {

    private static final Long TEMP_PATIENT_ID = 1L;

    private final SelfServiceAppointmentFrame view;

    private final AppointmentHttpClient httpClient;

    public SelfServiceAppointmentController(
            SelfServiceAppointmentFrame view
    ) {

        this.view = view;

        this.httpClient = new AppointmentHttpClient();
    }

    public void initData() {

        try {

            List<String> professionals =
                    httpClient.getAllProfessionals();

            view.cbProfessional.getItems().setAll(
                    professionals
            );

        } catch (Exception e) {

            view.lblFeedback.setStyle(
                    "-fx-text-fill: red;"
            );

            view.lblFeedback.setText(
                    "Error cargando profesionales."
            );
        }
    }

    public void loadAvailableHours() {

        try {

            if (view.cbProfessional.getValue() == null ||
                    view.datePicker.getValue() == null) {

                return;
            }

            Long professionalId =
                    httpClient.getProfessionalIdByName(
                            view.cbProfessional.getValue()
                    );

            List<String> availableSlots =
                    httpClient.getAvailableSlots(
                            professionalId,
                            view.datePicker.getValue()
                    );

            view.cbTime.getItems().clear();

            view.cbTime.getItems().addAll(
                    availableSlots
            );

            if (availableSlots.isEmpty()) {

                view.lblFeedback.setStyle(
                        "-fx-text-fill: orange;"
                );

                view.lblFeedback.setText(
                        "No hay horarios disponibles."
                );

            } else {

                view.lblFeedback.setText("");
            }

        } catch (Exception e) {

            e.printStackTrace();

            view.lblFeedback.setStyle(
                    "-fx-text-fill: red;"
            );

            view.lblFeedback.setText(
                    "Error cargando horarios."
            );
        }
    }

    public void setupHelpListeners() {

        addHelp(
                view.cbProfessional,
                "Seleccione al médico especialista con el que desea agendar su cita."
        );

        addHelp(
                view.datePicker,
                "Elija el día de su preferencia. Solo fechas futuras."
        );

        addHelp(
                view.cbTime,
                "Seleccione un horario disponible."
        );

        addHelp(
                view.txtMotivo,
                "Describa brevemente el motivo de la consulta."
        );
    }

    private void addHelp(Node node, String message) {

        node.setOnMouseEntered(
                e -> view.lblHelpBox.setText(message)
        );

        node.setOnMouseExited(
                e -> view.lblHelpBox.setText(
                        "Pase el cursor sobre un campo para ver la ayuda."
                )
        );
    }

    public void handleRegister() {

        try {

            if (!validateFields()) {

                return;
            }

            Long professionalId =
                    httpClient.getProfessionalIdByName(
                            view.cbProfessional.getValue()
                    );

            LocalDate date =
                    view.datePicker.getValue();

            LocalTime time =
                    LocalTime.parse(
                            view.cbTime.getValue()
                    );

            LocalDateTime appointmentDate =
                    LocalDateTime.of(date, time);

            CreateAppointmentRequestDTO request =
                    new CreateAppointmentRequestDTO();

            request.setPatientId(TEMP_PATIENT_ID);

            request.setProfessionalId(professionalId);

            request.setObservation(
                    view.txtMotivo.getText()
            );

            request.setAppointmentDate(
                    appointmentDate
            );

            request.setSchedulingType(
                    SchedulingType.SELFSERVICE
            );

            httpClient.createAppointment(request);

            view.lblFeedback.setStyle(
                    "-fx-text-fill: green;"
            );

            view.lblFeedback.setText(
                    "¡Cita agendada con éxito!"
            );

            showSuccess("Cita agendada con exito!");

            clearForm();

        } catch (Exception ex) {

            ex.printStackTrace();

            view.lblFeedback.setStyle(
                    "-fx-text-fill: red;"
            );

            view.lblFeedback.setText(
                    "Error al registrar cita."
            );
        }
    }

    private boolean validateFields() {

        if (view.cbProfessional.getValue() == null ||
                view.datePicker.getValue() == null ||
                view.cbTime.getValue() == null ||
                view.txtMotivo.getText().isBlank()) {

            view.lblFeedback.setStyle(
                    "-fx-text-fill: red;"
            );

            view.lblFeedback.setText(
                    "Complete todos los campos."
            );

            return false;
        }

        return true;
    }

    public void clearForm() {

        view.cbProfessional.getSelectionModel()
                .clearSelection();

        view.cbTime.getItems().clear();

        view.cbTime.getSelectionModel()
                .clearSelection();

        view.txtMotivo.clear();

        view.lblFeedback.setText("");
    }

    private void showSuccess(String message) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Cambiar texto del botón
        ButtonType acceptButton = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(acceptButton);

        // Obtener el DialogPane para aplicar estilos
        DialogPane dialogPane = alert.getDialogPane();

        dialogPane.setStyle(
                "-fx-background-color: #ecfbf1;" +
                        "-fx-background-radius: 14;" +
                        "-fx-border-radius: 14;" +
                        "-fx-padding: 12;"
        );

        // Estilo del texto
        dialogPane.lookup(".content.label").setStyle(
                "-fx-text-fill: #0c5b3f;" +
                        "-fx-font-size: 14px;"
        );

        // Estilo del botón
        Button button = (Button) dialogPane.lookupButton(acceptButton);

        button.setStyle(
                "-fx-background-color: #0c5b3f;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 8 18 8 18;"
        );

        alert.showAndWait();
    }

}