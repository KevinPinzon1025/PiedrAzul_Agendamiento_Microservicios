package co.unicauca.frontend.view;

import co.unicauca.frontend.controller.SearchAppointmentController;
import co.unicauca.frontend.dto.AuthResponse;
import co.unicauca.frontend.dto.AuthSession;
import co.unicauca.frontend.viewmodel.AppointmentViewModel;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class SearchAppointmentFrame extends Application {

    private SearchAppointmentController controller;
    private Stage stage;

    private TableView<AppointmentViewModel> table;
    private Label lblTotal;
    private ComboBox<String> cbProfessional;
    private DatePicker datePicker;
    private TextField txtSearch;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        this.controller = new SearchAppointmentController(new FxViewAdapter(stage));

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #eef5fb;");

        VBox topContainer = new VBox(12);
        topContainer.setPadding(new Insets(18, 26, 8, 26));
        topContainer.getChildren().addAll(createHeader(), createToolbar());

        root.setTop(topContainer);
        root.setCenter(createContent());

        Scene scene = new Scene(root, 1240, 760);
        stage.setScene(scene);
        stage.setTitle("Consultar citas");
        stage.show();

        controller.onInit();
    }

    private Node createHeader() {
        HBox header = new HBox(16);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(20, 24, 20, 24));
        header.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 22;" +
                        "-fx-border-radius: 22;" +
                        "-fx-border-color: #d6e3f2;"
        );

        try {
            Image logo = new Image(getClass().getResourceAsStream("/logo_piedrazul.png"));
            ImageView logoView = new ImageView(logo);
            logoView.setFitWidth(90);
            logoView.setPreserveRatio(true);
            header.getChildren().add(logoView);
        } catch (Exception ignored) {
        }

        VBox brandText = new VBox(4);
        Label brandTop = new Label("Servicios médicos");
        brandTop.setFont(Font.font("System", 18));
        brandTop.setStyle("-fx-text-fill: #5b8bd9;");

        Label brandBottom = new Label("Piedrazul");
        brandBottom.setFont(Font.font("System", FontWeight.BOLD, 30));
        brandBottom.setStyle("-fx-text-fill: #1f6fb2;");
        brandText.getChildren().addAll(brandTop, brandBottom);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        VBox userBox = new VBox(4);
        userBox.setAlignment(Pos.CENTER_RIGHT);
        AuthResponse currentUser = AuthSession.getCurrentUser();
        String displayName = currentUser != null && currentUser.getFullName() != null && !currentUser.getFullName().isBlank()
                ? currentUser.getFullName()
                : currentUser != null && currentUser.getLogin() != null ? currentUser.getLogin() : "Usuario";
        String role = currentUser != null && currentUser.getRole() != null ? currentUser.getRole() : "";

        Label lblUser = new Label(displayName);
        lblUser.setFont(Font.font("System", FontWeight.BOLD, 19));
        lblUser.setStyle("-fx-text-fill: #204968;");

        Label lblRole = new Label(role.isBlank() ? "Sesión iniciada" : "Rol: " + role);
        lblRole.setFont(Font.font("System", 16));
        lblRole.setStyle("-fx-text-fill: #5f7387;");
        userBox.getChildren().addAll(lblUser, lblRole);

        Button btnLogout = new Button("Cerrar sesión");
        btnLogout.setPrefHeight(44);
        btnLogout.setFont(Font.font("System", FontWeight.BOLD, 17));
        btnLogout.setStyle(
                "-fx-background-color: #e8f1fb;" +
                        "-fx-text-fill: #1f6fb2;" +
                        "-fx-background-radius: 14;" +
                        "-fx-cursor: hand;"
        );
        btnLogout.setOnAction(e -> logout());

        header.getChildren().addAll(brandText, spacer, userBox, btnLogout);
        return header;
    }

    private Node createToolbar() {
        VBox container = new VBox(16);
        container.setPadding(new Insets(4, 10, 0, 10));

        HBox navBar = new HBox(20);
        navBar.setAlignment(Pos.CENTER_LEFT);

        Button btnAgendar = createNavButton("Agendar cita", false);
        btnAgendar.setOnAction(e -> {
            try {
                new ScheduleAppointmentFrame().start(new Stage());
                stage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Button btnListar = createNavButton("Consultar citas", true);
        navBar.getChildren().addAll(btnAgendar, btnListar);

        HBox toolbar = new HBox(20);
        toolbar.setAlignment(Pos.CENTER_LEFT);

        VBox titleBox = new VBox(4);
        Label title = new Label("Consultar citas");
        title.setFont(Font.font("System", FontWeight.BOLD, 32));
        title.setStyle("-fx-text-fill: #163b5b;");

        Label subtitle = new Label("Use los filtros para encontrar citas con mayor facilidad.");
        subtitle.setFont(Font.font("System", 18));
        subtitle.setStyle("-fx-text-fill: #5f7387;");
        titleBox.getChildren().addAll(title, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        txtSearch = new TextField();
        txtSearch.setPromptText("Buscar por nombre del profesional");
        txtSearch.setPrefWidth(320);
        styleInput(txtSearch);

        Button btnSearch = new Button("Buscar");
        btnSearch.setPrefHeight(50);
        btnSearch.setFont(Font.font("System", FontWeight.BOLD, 18));
        btnSearch.setStyle(
                "-fx-background-color: #1f6fb2;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 14;" +
                        "-fx-padding: 0 24 0 24;"
        );
        btnSearch.setOnAction(e -> controller.onSearch());

        toolbar.getChildren().addAll(titleBox, spacer, txtSearch, btnSearch);
        container.getChildren().addAll(navBar, toolbar);

        return container;
    }

    private Node createContent() {
        VBox wrapper = new VBox(18);
        wrapper.setPadding(new Insets(12, 36, 30, 36));

        HBox filters = new HBox(14);
        filters.setAlignment(Pos.CENTER_LEFT);
        filters.setPadding(new Insets(18));
        filters.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 18;" +
                        "-fx-border-radius: 18;" +
                        "-fx-border-color: #d6e3f2;"
        );

        cbProfessional = new ComboBox<>();
        cbProfessional.setPromptText("Seleccione un profesional");
        cbProfessional.setPrefWidth(320);
        cbProfessional.setPrefHeight(50);
        cbProfessional.setStyle("-fx-font-size: 17px;");

        datePicker = new DatePicker();
        datePicker.setPrefWidth(210);
        datePicker.setPrefHeight(50);
        datePicker.setValue(LocalDate.now());
        datePicker.setStyle("-fx-font-size: 17px;");

        Button btnConsultar = new Button("Aplicar filtros");
        btnConsultar.setPrefHeight(50);
        btnConsultar.setFont(Font.font("System", FontWeight.BOLD, 18));
        btnConsultar.setStyle(
                "-fx-background-color: #25a25a;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 14;" +
                        "-fx-padding: 0 24 0 24;"
        );
        btnConsultar.setOnAction(e -> controller.onSearch());

        filters.getChildren().addAll(cbProfessional, datePicker, btnConsultar);

        VBox card = new VBox(16);
        card.setPadding(new Insets(22));
        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 18;" +
                        "-fx-border-radius: 18;" +
                        "-fx-border-color: #d6e3f2;"
        );

        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(500);
        table.setStyle("-fx-font-size: 16px;");

        TableColumn<AppointmentViewModel, String> colId = new TableColumn<>("Identificación");
        colId.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().patientId));

        TableColumn<AppointmentViewModel, String> colDoctor = new TableColumn<>("Médico");
        colDoctor.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().doctorName));

        TableColumn<AppointmentViewModel, String> colPatient = new TableColumn<>("Paciente");
        colPatient.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().patientName));

        TableColumn<AppointmentViewModel, String> colDate = new TableColumn<>("Fecha");
        colDate.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().date));

        TableColumn<AppointmentViewModel, String> colTime = new TableColumn<>("Hora");
        colTime.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().time));

        table.getColumns().addAll(colId, colDoctor, colPatient, colDate, colTime);

        lblTotal = new Label("Total de citas: 0");
        lblTotal.setFont(Font.font("System", FontWeight.BOLD, 18));
        lblTotal.setStyle("-fx-text-fill: #244b68;");

        card.getChildren().addAll(table, lblTotal);
        wrapper.getChildren().addAll(filters, card);

        return wrapper;
    }

    private Button createNavButton(String text, boolean active) {
        Button button = new Button(text);
        button.setFont(Font.font("System", FontWeight.BOLD, 18));
        button.setStyle(
                active
                        ? "-fx-background-color: transparent;" +
                        "-fx-text-fill: #1f6fb2;" +
                        "-fx-border-color: transparent transparent #1f6fb2 transparent;" +
                        "-fx-border-width: 0 0 4 0;" +
                        "-fx-padding: 0 6 10 6;"
                        : "-fx-background-color: transparent;" +
                        "-fx-text-fill: #5f7387;" +
                        "-fx-padding: 0 6 10 6;"
        );
        return button;
    }

    private void styleInput(TextField field) {
        field.setFont(Font.font("System", 17));
        field.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #c8d9eb;" +
                        "-fx-border-radius: 14;" +
                        "-fx-background-radius: 14;" +
                        "-fx-padding: 10 14 10 14;"
        );
    }

    private void logout() {
        try {
            AuthSession.clear();
            new LoginFrame().start(new Stage());
            stage.close();
        } catch (Exception e) {
            showAlert("No fue posible cerrar sesión");
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Consulta");
        alert.setHeaderText("Resultado");
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private class FxViewAdapter implements SearchAppointmentController.View {
        private final Stage stage;

        private FxViewAdapter(Stage stage) {
            this.stage = stage;
        }

        @Override
        public void setProfessionals(List<String> professionals) {
            cbProfessional.getItems().setAll(professionals);
        }

        @Override
        public String getSelectedProfessional() {
            return cbProfessional.getValue();
        }

        @Override
        public LocalDate getSelectedDate() {
            return datePicker.getValue();
        }

        @Override
        public String getSearchText() {
            return txtSearch.getText();
        }

        @Override
        public void clearAppointments() {
            table.getItems().clear();
        }

        @Override
        public void setAppointments(List<AppointmentViewModel> appointments) {
            table.getItems().setAll(appointments);
        }

        @Override
        public void setTotal(int total) {
            lblTotal.setText("Total de citas: " + total);
        }

        @Override
        public void showAlert(String message) {
            SearchAppointmentFrame.this.showAlert(message);
        }
    }
}
