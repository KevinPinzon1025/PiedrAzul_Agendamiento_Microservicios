
package co.unicauca.frontend.view;

import co.unicauca.frontend.controller.SelfServiceAppointmentController;
import co.unicauca.frontend.dto.AppointmentDTO;
import co.unicauca.frontend.dto.AuthResponse;
import co.unicauca.frontend.dto.AuthSession;
import javafx.beans.property.SimpleStringProperty;
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
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SelfServiceAppointmentFrame extends Application {

    private SelfServiceAppointmentController controller;
    private Stage stage;

    public ComboBox<String> cbProfessional;
    public DatePicker datePicker;
    public ComboBox<String> cbTime;
    public TextArea txtMotivo;
    public Label lblFeedback;
    public Label lblHelpBox;
    public Label lblMyAppointmentsFeedback;
    public TableView<AppointmentDTO> tblMyAppointments;

    private BorderPane root;
    private Button btnScheduleTab;
    private Button btnMyAppointmentsTab;

    @Override
    public void start(Stage stage) {

        this.stage = stage;
        this.controller = new SelfServiceAppointmentController(this);

        root = new BorderPane();
        root.setStyle("-fx-background-color: #f4f6fb;");

        VBox topContainer = new VBox(12);
        topContainer.setPadding(new Insets(15, 30, 0, 30));
        topContainer.getChildren().addAll(
                createHeader(),
                createToolbar()
        );

        root.setTop(topContainer);
        root.setCenter(createScheduleContent());

        Scene scene = new Scene(root, 1100, 650);

        stage.setScene(scene);

        stage.setTitle(
                "Autogestión de Citas - Servicios Médicos Piedrazul"
        );

        stage.show();

        controller.initData();
    }

    private Node createToolbar() {

        HBox toolbar = new HBox(20);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setPadding(new Insets(0, 0, 8, 0));

        btnScheduleTab = createNavButton("Agendar cita", true);
        btnMyAppointmentsTab = createNavButton("Mis citas", false);

        btnScheduleTab.setOnAction(e -> showScheduleTab());
        btnMyAppointmentsTab.setOnAction(e -> showMyAppointmentsTab());

        toolbar.getChildren().addAll(
                btnScheduleTab,
                btnMyAppointmentsTab
        );

        return toolbar;
    }

    private void showScheduleTab() {

        btnScheduleTab.setStyle(navButtonStyle(true));
        btnMyAppointmentsTab.setStyle(navButtonStyle(false));
        root.setCenter(createScheduleContent());
        controller.initData();
    }

    public void showMyAppointmentsTab() {

        btnScheduleTab.setStyle(navButtonStyle(false));
        btnMyAppointmentsTab.setStyle(navButtonStyle(true));
        root.setCenter(createMyAppointmentsContent());
        controller.loadMyAppointments();
    }

    private Node createScheduleContent() {

        HBox mainContent = new HBox(20);
        mainContent.setAlignment(Pos.CENTER);
        mainContent.setPadding(new Insets(30));

        mainContent.getChildren().addAll(
                createForm(),
                createHelpPanel()
        );

        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        scrollPane.setStyle("-fx-background-color: #f4f6fb; -fx-background: #f4f6fb;");

        return scrollPane;
    }

    private Node createMyAppointmentsContent() {

        VBox wrapper = new VBox(18);
        wrapper.setAlignment(Pos.TOP_CENTER);
        wrapper.setPadding(new Insets(30));

        VBox card = new VBox(18);
        card.setMaxWidth(980);
        card.setPadding(new Insets(28));
        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 18;" +
                        "-fx-border-radius: 18;" +
                        "-fx-border-color: #d6e3f2;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 18, 0.20, 0, 5);"
        );

        Label title = new Label("Mis citas");
        title.setFont(Font.font("System", 28));
        title.setStyle("-fx-text-fill: #163b5b; -fx-font-weight: bold;");

        lblMyAppointmentsFeedback = new Label(" ");
        lblMyAppointmentsFeedback.setMinHeight(42);
        lblMyAppointmentsFeedback.setWrapText(true);
        lblMyAppointmentsFeedback.setStyle(
                "-fx-text-fill: #5f7387;" +
                        "-fx-background-color: #eef5fb;" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 10;"
        );

        tblMyAppointments = createMyAppointmentsTable();

        Button btnRefresh = new Button("Actualizar");
        btnRefresh.setPrefHeight(44);
        btnRefresh.setStyle(
                "-fx-background-color: #1f6fb2;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;"
        );
        btnRefresh.setOnAction(e -> controller.loadMyAppointments());

        HBox actions = new HBox(btnRefresh);
        actions.setAlignment(Pos.CENTER_RIGHT);

        card.getChildren().addAll(
                title,
                lblMyAppointmentsFeedback,
                tblMyAppointments,
                actions
        );

        wrapper.getChildren().add(card);

        ScrollPane scrollPane = new ScrollPane(wrapper);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #f4f6fb; -fx-background-color: #f4f6fb;");

        return scrollPane;
    }

    private Node createHeader() {

        HBox header = new HBox();

        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(16);

        header.setPadding(new Insets(15, 30, 15, 30));

        header.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #d9d9d9;" +
                        "-fx-border-width: 0 0 1 0;"
        );

        Label brand = new Label(
                "Portal del Paciente - Piedrazul"
        );

        brand.setFont(Font.font("System", 22));

        brand.setStyle(
                "-fx-text-fill: #2a5fa9;" +
                        "-fx-font-weight: bold;"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        VBox userBox = new VBox(3);
        userBox.setAlignment(Pos.CENTER_RIGHT);
        AuthResponse currentUser = AuthSession.getCurrentUser();
        String displayName = currentUser != null ? currentUser.getDisplayName() : "Paciente";
        String role = currentUser != null && currentUser.getRole() != null ? currentUser.getRole() : "";

        Label lblUser = new Label(displayName);
        lblUser.setStyle("-fx-text-fill: #204968; -fx-font-size: 16px; -fx-font-weight: bold;");

        Label lblRole = new Label(role.isBlank() ? "Sesión iniciada" : "Rol: " + role);
        lblRole.setStyle("-fx-text-fill: #5f7387; -fx-font-size: 14px;");
        userBox.getChildren().addAll(lblUser, lblRole);

        Button btnLogout = new Button("Cerrar sesión");
        btnLogout.setPrefHeight(40);
        btnLogout.setStyle(
                "-fx-background-color: #e8f1fb;" +
                        "-fx-text-fill: #1f6fb2;" +
                        "-fx-background-radius: 14;" +
                        "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;"
        );
        btnLogout.setOnAction(e -> logout());

        header.getChildren().addAll(brand, spacer, userBox, btnLogout);

        return header;
    }

    private void logout() {
        try {
            AuthSession.clear();
            new LoginFrame().start(new Stage());
            stage.close();
        } catch (Exception e) {
            showAlert("No fue posible cerrar sesión.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sesión");
        alert.setHeaderText("Resultado");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Node createForm() {

        VBox card = new VBox(20);

        card.setMinWidth(600);

        card.setMaxWidth(600);

        card.setPadding(new Insets(30));

        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 18;" +
                        "-fx-border-radius: 18;" +
                        "-fx-border-color: #dfe3eb;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 4);"
        );

        Label title = new Label("Programar mi cita");

        title.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #333;"
        );

        GridPane grid = new GridPane();

        grid.setHgap(15);

        grid.setVgap(20);

        cbProfessional = new ComboBox<>();

        cbProfessional.setPromptText(
                "Seleccione su médico"
        );

        cbProfessional.setMaxWidth(Double.MAX_VALUE);

        datePicker = new DatePicker(LocalDate.now());

        datePicker.setMaxWidth(Double.MAX_VALUE);

        cbTime = new ComboBox<>();

        cbTime.setPromptText("Seleccione hora");

        cbTime.setMaxWidth(Double.MAX_VALUE);

        txtMotivo = new TextArea();

        txtMotivo.setPromptText(
                "Ej: Control anual, dolor de espalda..."
        );

        txtMotivo.setPrefHeight(100);

        txtMotivo.setWrapText(true);

        Label requiredInfo = createRequiredInfoLabel();

        grid.add(createRequiredLabel("Profesional"), 0, 0);

        grid.add(cbProfessional, 0, 1);

        grid.add(createRequiredLabel("Fecha deseada"), 1, 0);

        grid.add(datePicker, 1, 1);

        grid.add(createRequiredLabel("Hora"), 0, 2);

        grid.add(cbTime, 0, 3);

        grid.add(
                createRequiredLabel("Motivo de consulta"),
                0,
                4,
                2,
                1
        );

        grid.add(txtMotivo, 0, 5, 2, 1);

        cbProfessional.valueProperty().addListener(
                (obs, oldVal, newVal) ->
                        controller.loadAvailableHours()
        );

        datePicker.valueProperty().addListener(
                (obs, oldVal, newVal) ->
                        controller.loadAvailableHours()
        );

        datePicker.setDayCellFactory(picker ->
                new DateCell() {

                    @Override
                    public void updateItem(
                            LocalDate date,
                            boolean empty
                    ) {

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
                }
        );

        HBox actions = new HBox(15);

        actions.setAlignment(Pos.CENTER_RIGHT);

        Button btnRegister = new Button(
                "Confirmar Cita"
        );

        btnRegister.setStyle(
                "-fx-background-color: #25b05b;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10 25;"
        );

        btnRegister.setOnAction(
                e -> controller.handleRegister()
        );

        Button btnClear = new Button("Limpiar");

        btnClear.setOnAction(
                e -> controller.clearForm()
        );

        actions.getChildren().addAll(
                btnClear,
                btnRegister
        );

        lblFeedback = new Label();

        card.getChildren().addAll(
                title,
                requiredInfo,
                grid,
                actions,
                lblFeedback
        );

        controller.setupHelpListeners();

        return card;
    }

    private Label createRequiredLabel(String text) {
        Label label = new Label(text + " *");
        label.setStyle("-fx-font-weight: bold; -fx-text-fill: #244b68;");
        return label;
    }

    private Label createRequiredInfoLabel() {
        Label label = new Label("Los campos con * son obligatorios.");
        label.setWrapText(true);
        label.setStyle("-fx-text-fill: #6b7e90;");
        return label;
    }

    private Node createHelpPanel() {

        VBox helpBox = new VBox(10);

        helpBox.setPrefWidth(250);

        helpBox.setPadding(new Insets(20));

        helpBox.setAlignment(Pos.TOP_LEFT);

        helpBox.setStyle(
                "-fx-background-color: #eef2f7;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: #cfd6df;"
        );

        Label helpTitle = new Label("Guía de Usuario");

        helpTitle.setStyle(
                "-fx-font-weight: bold;" +
                        "-fx-text-fill: #2d6dcc;"
        );

        lblHelpBox = new Label(
                "Pase el cursor sobre un campo para ver la ayuda."
        );

        lblHelpBox.setWrapText(true);

        lblHelpBox.setStyle(
                "-fx-text-fill: #555;" +
                        "-fx-font-style: italic;"
        );

        helpBox.getChildren().addAll(
                helpTitle,
                lblHelpBox
        );

        return helpBox;
    }

    private TableView<AppointmentDTO> createMyAppointmentsTable() {

        TableView<AppointmentDTO> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(430);

        TableColumn<AppointmentDTO, String> colProfessional =
                new TableColumn<>("Profesional");
        colProfessional.setCellValueFactory(cell ->
                new SimpleStringProperty(
                        cell.getValue().getProfessional() == null
                                ? ""
                                : cell.getValue().getProfessional().getProfName()
                )
        );

        TableColumn<AppointmentDTO, String> colDate =
                new TableColumn<>("Fecha");
        colDate.setCellValueFactory(cell ->
                new SimpleStringProperty(
                        cell.getValue().getAppointmentDate() == null
                                ? ""
                                : cell.getValue().getAppointmentDate()
                                .toLocalDate()
                                .toString()
                )
        );

        TableColumn<AppointmentDTO, String> colTime =
                new TableColumn<>("Hora");
        colTime.setCellValueFactory(cell ->
                new SimpleStringProperty(
                        cell.getValue().getAppointmentDate() == null
                                ? ""
                                : cell.getValue().getAppointmentDate()
                                .toLocalTime()
                                .format(DateTimeFormatter.ofPattern("HH:mm"))
                )
        );

        TableColumn<AppointmentDTO, String> colObservation =
                new TableColumn<>("Motivo");
        colObservation.setCellValueFactory(cell ->
                new SimpleStringProperty(
                        cell.getValue().getObservation() == null
                                ? ""
                                : cell.getValue().getObservation()
                )
        );

        table.getColumns().addAll(
                colProfessional,
                colDate,
                colTime,
                colObservation
        );

        return table;
    }

    public void setMyAppointments(
            List<AppointmentDTO> appointments
    ) {

        tblMyAppointments.getItems().setAll(appointments);

        if (appointments.isEmpty()) {

            lblMyAppointmentsFeedback.setText(
                    "No tiene citas agendadas."
            );

        } else {

            lblMyAppointmentsFeedback.setText(
                    "Citas agendadas: " + appointments.size()
            );
        }
    }

    public void showMyAppointmentsError(String message) {

        lblMyAppointmentsFeedback.setStyle(
                "-fx-text-fill: #9b1c1c;" +
                        "-fx-background-color: #fdecec;" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 10;"
        );

        lblMyAppointmentsFeedback.setText(message);
    }

    private Button createNavButton(
            String text,
            boolean active
    ) {

        Button button = new Button(text);
        button.setFont(Font.font("System", 18));
        button.setStyle(navButtonStyle(active));

        return button;
    }

    private String navButtonStyle(boolean active) {

        return active
                ? "-fx-background-color: transparent;" +
                "-fx-text-fill: #1f6fb2;" +
                "-fx-border-color: transparent transparent #1f6fb2 transparent;" +
                "-fx-border-width: 0 0 4 0;" +
                "-fx-padding: 0 6 10 6;" +
                "-fx-font-weight: bold;"
                : "-fx-background-color: transparent;" +
                "-fx-text-fill: #5f7387;" +
                "-fx-padding: 0 6 10 6;" +
                "-fx-font-weight: bold;";
    }

    public static void main(String[] args) {

        launch(args);
    }
}
