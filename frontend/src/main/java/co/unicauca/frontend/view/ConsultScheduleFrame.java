package co.unicauca.frontend.view;

import co.unicauca.frontend.dto.AppointmentDTO;
import co.unicauca.frontend.dto.SlotResponseDTO;
import co.unicauca.frontend.controller.ConsultScheduleController;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ConsultScheduleFrame {

    private final Stage stage;

    private final ConsultScheduleController controller;

    private TableView<SlotResponseDTO> tableAvailable;

    private TableView<AppointmentDTO> tableOccupied;

    private VBox contentContainer;

    private boolean showingAvailableSchedulesTab = true;

    public ConsultScheduleFrame(
            Stage owner,
            String professional,
            LocalDate date
    ) {

        stage = new Stage();

        stage.initOwner(owner);

        stage.initModality(Modality.APPLICATION_MODAL);

        stage.setTitle(
                "Consultar horarios"
        );

        controller =
                new ConsultScheduleController(
                        new FxViewAdapter(),
                        professional,
                        date
                );

        StackPane root = new StackPane();

        root.setStyle(
                "-fx-background-color: rgba(0,0,0,0.25);"
        );

        VBox modal = createModal();

        ScrollPane scrollPane = new ScrollPane(modal);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        root.getChildren().add(scrollPane);

        Scene scene =
                new Scene(root, 800, 550);

        stage.setScene(scene);

        stage.setOnCloseRequest(e -> {

            controller.onClose();
        });

        controller.onInit();
    }

    public void show() {

        stage.showAndWait();
    }

    private VBox createModal() {

        VBox container = new VBox(20);

        container.setPadding(new Insets(25));

        container.setMaxWidth(850);

        container.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;"
        );

        Label title =
                new Label("Consultar horarios");

        title.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;"
        );

        HBox tabButtons = createTabButtons();

        contentContainer = new VBox();

        contentContainer.setSpacing(15);

        showAvailableSchedules();

        container.getChildren().addAll(
                title,
                tabButtons,
                contentContainer
        );

        return container;
    }


    private HBox createTabButtons() {

        HBox box = new HBox(15);

        box.setAlignment(Pos.CENTER);

        Button btnSchedules =
                new Button("Horarios disponibles");

        Button btnAppointments =
                new Button("Mis citas");

        btnSchedules.setPrefWidth(220);

        btnAppointments.setPrefWidth(160);

        styleTabButton(btnSchedules, true);

        styleTabButton(btnAppointments, false);

        btnSchedules.setOnAction(e -> {

            styleTabButton(btnSchedules, true);

            styleTabButton(btnAppointments, false);

            showingAvailableSchedulesTab = true;

            showAvailableSchedules();

            controller.onShowAvailableTab();
        });

        btnAppointments.setOnAction(e -> {

            styleTabButton(btnSchedules, false);

            styleTabButton(btnAppointments, true);

            showingAvailableSchedulesTab = false;

            showMyAppointments();

            controller.onShowAppointmentsTab();
        });

        box.getChildren().addAll(
                btnSchedules,
                btnAppointments
        );

        return box;
    }

    private void styleTabButton(
            Button btn,
            boolean active
    ) {

        if (active) {

            btn.setStyle(
                    "-fx-background-color: #3b86df;" +
                            "-fx-text-fill: white;" +
                            "-fx-background-radius: 8;"
            );

        } else {

            btn.setStyle(
                    "-fx-background-color: #e5e7eb;" +
                            "-fx-text-fill: #333;" +
                            "-fx-background-radius: 8;"
            );
        }
    }

    private void showAvailableSchedules() {

        contentContainer.getChildren().clear();

        VBox availableBox = new VBox(6);

        Label lblAvailable =
                new Label("Disponibles");

        lblAvailable.setStyle(
                "-fx-font-weight: bold;"
        );

        tableAvailable = new TableView<>();

        tableAvailable.setPlaceholder(
                new Label("Sin horarios")
        );

        TableColumn<SlotResponseDTO, String> colHour =
                new TableColumn<>("Hora");

        colHour.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getStartTime()
                )
        );

        colHour.setPrefWidth(500);

        tableAvailable.getColumns().add(
                colHour
        );

        availableBox.getChildren().addAll(
                lblAvailable,
                tableAvailable
        );

        contentContainer.getChildren().add(
                availableBox
        );
    }

    private void showMyAppointments() {

        contentContainer.getChildren().clear();

        tableOccupied = new TableView<>();

        tableOccupied.setPlaceholder(
                new Label("Sin citas")
        );

        TableColumn<AppointmentDTO, String> colDate =
                new TableColumn<>("Fecha");

        colDate.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue()
                                .getAppointmentDate()
                                .toLocalDate()
                                .toString()
                )
        );

        TableColumn<AppointmentDTO, String> colTime =
                new TableColumn<>("Hora");

        colTime.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue()
                                .getAppointmentDate()
                                .toLocalTime()
                                .format(
                                        DateTimeFormatter.ofPattern("HH:mm")
                                )
                )
        );

        TableColumn<AppointmentDTO, String> colPatient =
                new TableColumn<>("Paciente");

        colPatient.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getPatient().getPatName()
                )
        );

        TableColumn<AppointmentDTO, String> colReason =
                new TableColumn<>("Motivo");

        colReason.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue()
                                .getObservation()
                )
        );

        tableOccupied.getColumns().addAll(
                colDate,
                colTime,
                colPatient,
                colReason
        );

        contentContainer.getChildren().add(
                tableOccupied
        );
    }

    private class FxViewAdapter
            implements ConsultScheduleController.View {

        @Override
        public void setAvailableSlots(
                List<SlotResponseDTO> slots
        ) {

            if (tableAvailable == null) {

                return;
            }

            tableAvailable.getItems().setAll(
                    slots
            );
        }

        @Override
        public void setAppointments(
                List<AppointmentDTO> appointments
        ) {

            if (tableOccupied == null) {

                return;
            }

            tableOccupied.getItems().setAll(
                   appointments
            );
        }
    }
}