package co.unicauca.frontend.view;

import co.unicauca.frontend.client.AppointmentHttpClient;
import co.unicauca.frontend.dto.CreateAppointmentRequestDTO;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class ScheduleAppointmentFrame extends Application {

    private AppointmentHttpClient httpClient;

    private ComboBox<String> cbPatient;
    private ComboBox<String> cbProfessional;
    private DatePicker datePicker;
    private ComboBox<String> cbTime;
    private TextArea txtMotivo;
    private Label lblFeedback;
    private Stage stage;

    @Override
    public void start(Stage stage) {

        this.stage = stage;

        // Cliente HTTP REST
        this.httpClient = new AppointmentHttpClient();

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f4f6fb;");

        VBox topContainer = new VBox(0);
        topContainer.getChildren().addAll(createHeader(), createToolbar());

        root.setTop(topContainer);
        root.setCenter(createForm());

        Scene scene = new Scene(root, 980, 680);

        stage.setScene(scene);
        stage.setTitle("Agendar Cita");
        stage.show();
    }

    private Node createHeader() {

        HBox header = new HBox();

        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15, 30, 15, 30));
        header.setSpacing(12);

        header.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #d9d9d9;" +
                        "-fx-border-width: 0 0 1 0;"
        );

        Label brand = new Label("Servicios médicos Piedrazul");

        brand.setFont(Font.font("System", 22));

        brand.setStyle(
                "-fx-text-fill: #2a5fa9;" +
                        "-fx-font-weight: bold;"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label userLabel = new Label("Miguel - Agendador");

        userLabel.setStyle(
                "-fx-text-fill: #2d6dcc;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;"
        );

        header.getChildren().addAll(brand, spacer, userLabel);

        return header;
    }

    private Node createToolbar() {

        HBox toolbar = new HBox();

        toolbar.setPadding(new Insets(20, 50, 10, 50));
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setSpacing(20);

        // Botón AGENDAR (activo)
        Button btnAgendar = new Button("Agendar cita");

        btnAgendar.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #2d6dcc;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-border-color: transparent transparent #2d6dcc transparent;" +
                        "-fx-border-width: 0 0 3 0;"
        );

        // Botón LISTAR
        Button btnListar = new Button("Listar citas");

        btnListar.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #555;" +
                        "-fx-font-size: 16px;"
        );

        // Navegar a listar citas
        btnListar.setOnAction(e -> {
            try {
                new SearchAppointmentFrame().start(new Stage());
                stage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        toolbar.getChildren().addAll(btnAgendar, btnListar);

        return toolbar;
    }

    private Node createForm() {

        VBox wrapper = new VBox();

        wrapper.setAlignment(Pos.TOP_CENTER);
        wrapper.setPadding(new Insets(10, 40, 30, 40));

        VBox card = new VBox(18);

        card.setMaxWidth(760);

        card.setPadding(new Insets(28));

        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 18;" +
                        "-fx-border-radius: 18;" +
                        "-fx-border-color: #dfe3eb;" +
                        "-fx-border-width: 1;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 14, 0.2, 0, 4);"
        );

        HBox topActions = createTopActions();
        GridPane formGrid = createFormGrid();
        HBox bottomActions = createBottomActions();

        lblFeedback = new Label();

        lblFeedback.setStyle(
                "-fx-text-fill: #0b5345;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;"
        );

        card.getChildren().addAll(
                topActions,
                formGrid,
                bottomActions,
                lblFeedback
        );

        wrapper.getChildren().add(card);

        initData();

        return wrapper;
    }

    private HBox createTopActions() {

        HBox topActions = new HBox(12);

        topActions.setAlignment(Pos.CENTER);

        Button btnNewPatient = new Button("Nuevo Paciente");

        btnNewPatient.setPrefWidth(150);
        btnNewPatient.setPrefHeight(34);

        btnNewPatient.setStyle(
                "-fx-background-color: white;" +
                        "-fx-text-fill: #2d6dcc;" +
                        "-fx-font-weight: bold;" +
                        "-fx-border-color: #2d6dcc;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;"
        );

        // TODO: conectar luego
        btnNewPatient.setOnAction(e -> {
            lblFeedback.setText("Funcionalidad pendiente");
        });

        Button btnConsultarHorarios = new Button("Consultar Horarios");

        btnConsultarHorarios.setPrefWidth(160);
        btnConsultarHorarios.setPrefHeight(34);

        btnConsultarHorarios.setStyle(
                "-fx-background-color: white;" +
                        "-fx-text-fill: #5b8def;" +
                        "-fx-font-weight: bold;" +
                        "-fx-border-color: #b7cdfa;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;"
        );

        // TODO: conectar luego
        btnConsultarHorarios.setOnAction(e -> {
            lblFeedback.setText("Consulta de horarios pendiente");
        });

        topActions.getChildren().addAll(
                btnNewPatient,
                btnConsultarHorarios
        );

        return topActions;
    }

    private GridPane createFormGrid() {

        GridPane form = new GridPane();

        form.setHgap(18);
        form.setVgap(16);
        form.setAlignment(Pos.TOP_CENTER);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);

        form.getColumnConstraints().addAll(col1, col2);

        cbPatient = new ComboBox<>();
        cbPatient.setPromptText("Seleccionar paciente");
        cbPatient.setMaxWidth(Double.MAX_VALUE);
        cbPatient.setPrefHeight(38);

        cbProfessional = new ComboBox<>();
        cbProfessional.setPromptText("Seleccionar profesional");
        cbProfessional.setMaxWidth(Double.MAX_VALUE);
        cbProfessional.setPrefHeight(38);

        datePicker = new DatePicker(LocalDate.now());
        datePicker.setPromptText("Fecha");
        datePicker.setMaxWidth(Double.MAX_VALUE);
        datePicker.setPrefHeight(38);

        cbTime = new ComboBox<>();
        cbTime.setPromptText("Hora");
        cbTime.setMaxWidth(Double.MAX_VALUE);
        cbTime.setPrefHeight(38);

        txtMotivo = new TextArea();
        txtMotivo.setPromptText("Motivo de la consulta");
        txtMotivo.setWrapText(true);
        txtMotivo.setPrefHeight(120);

        VBox patientBox = createFieldBox("Paciente", cbPatient);
        VBox professionalBox = createFieldBox("Profesional", cbProfessional);
        VBox dateBox = createFieldBox("Fecha", datePicker);
        VBox timeBox = createFieldBox("Hora", cbTime);
        VBox reasonBox = createFieldBox("Motivo", txtMotivo);

        form.add(patientBox, 0, 0);
        form.add(professionalBox, 1, 0);
        form.add(dateBox, 0, 1);
        form.add(timeBox, 1, 1);
        form.add(reasonBox, 0, 2, 2, 1);

        // Bloquear fechas pasadas
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                if (empty) return;

                if (date.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle(
                            "-fx-background-color: #eeeeee;" +
                                    "-fx-text-fill: #999;"
                    );
                }
            }
        });

        return form;
    }

    private VBox createFieldBox(String labelText, Control field) {

        Label label = new Label(labelText);

        label.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #444;"
        );

        VBox box = new VBox(6);

        box.getChildren().addAll(label, field);

        VBox.setVgrow(field, Priority.NEVER);

        return box;
    }

    private HBox createBottomActions() {

        HBox actions = new HBox(12);

        actions.setAlignment(Pos.CENTER);

        Button btnRegister = new Button("Registrar");

        btnRegister.setPrefWidth(150);
        btnRegister.setPrefHeight(40);

        btnRegister.setStyle(
                "-fx-background-color: #25b05b;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;"
        );

        btnRegister.setOnAction(e -> {

            try {

                String patientName = cbPatient.getValue();
                String professionalName = cbProfessional.getValue();

                LocalDate date = datePicker.getValue();

                String observation = txtMotivo.getText();

                if (patientName == null ||
                        professionalName == null ||
                        date == null ||
                        observation == null ||
                        observation.isBlank()) {

                    lblFeedback.setText(
                            "Complete todos los campos"
                    );

                    return;
                }

                // Obtener IDs reales desde nombres
                Long patientId = httpClient.getPatientIdByName(patientName);

                Long professionalId = httpClient.getProfessionalIdByName(professionalName);

                CreateAppointmentRequestDTO request =
                        new CreateAppointmentRequestDTO();

                request.setPatientId(patientId);

                request.setProfessionalId(professionalId);

                request.setObservation(observation);

                // temporal mientras conectamos horarios
                request.setAppointmentDate(
                        date.atTime(10, 0)
                );

                httpClient.createAppointment(request);

                lblFeedback.setText(
                        "Cita registrada correctamente"
                );

                clearForm();

            } catch (Exception ex) {

                ex.printStackTrace();

                lblFeedback.setText(
                        "Error registrando cita"
                );
            }
        });

        Button btnClear = new Button("Limpiar");

        btnClear.setPrefWidth(120);
        btnClear.setPrefHeight(40);

        btnClear.setStyle(
                "-fx-background-color: #eef2f7;" +
                        "-fx-text-fill: #333;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-color: #cfd6df;" +
                        "-fx-border-radius: 8;"
        );

        btnClear.setOnAction(e -> clearForm());

        actions.getChildren().addAll(btnRegister, btnClear);

        return actions;
    }

    private void initData() {

        try {

            // Cargar pacientes
            List<String> patients = httpClient.getAllPatients();
            cbPatient.getItems().setAll(patients);

            // Cargar profesionales
            List<String> professionals = httpClient.getAllProfessionals();
            cbProfessional.getItems().setAll(professionals);

        } catch (Exception e) {

            e.printStackTrace();

            lblFeedback.setText(
                    "Error cargando datos"
            );
        }
    }

    private void clearForm() {

        cbPatient.getSelectionModel().clearSelection();
        cbProfessional.getSelectionModel().clearSelection();

        datePicker.setValue(LocalDate.now());

        cbTime.getSelectionModel().clearSelection();

        txtMotivo.clear();

        lblFeedback.setText("");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
