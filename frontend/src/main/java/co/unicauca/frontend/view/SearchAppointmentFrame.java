
package co.unicauca.frontend.view;


import co.unicauca.frontend.controller.SearchAppointmentController;
import co.unicauca.frontend.viewmodel.AppointmentViewModel;
import co.unicauca.frontend.view.ScheduleAppointmentFrame;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.time.LocalDate;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


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
        root.setStyle("-fx-background-color: #f3f4f6;");

        VBox topContainer = new VBox();
        topContainer.getChildren().addAll(createHeader(), createToolbar());

        root.setTop(topContainer);
        root.setCenter(createContent());

        Scene scene = new Scene(root, 1100, 650);
        stage.setScene(scene);
        stage.setTitle("Buscar citas");
        stage.show();

        controller.onInit();
    }

    private Node createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(18, 28, 18, 28));
        header.setSpacing(15);
        header.setStyle("-fx-background-color: white; -fx-border-color: #d9d9d9; -fx-border-width: 0 0 1 0;");

        Image logo = new Image(
                getClass().getResourceAsStream("/logo_piedrazul.png")
        );

        ImageView logoView = new ImageView(logo);
        logoView.setFitWidth(85);
        logoView.setPreserveRatio(true);

        VBox textLogoBox = new VBox(0);

        Label logoTop = new Label("Servicios médicos");
        logoTop.setStyle("-fx-text-fill: #5b8bd9; -fx-font-size: 14px;");

        Label logoBottom = new Label("Piedrazul");
        logoBottom.setStyle("-fx-text-fill: #2d6dcc; -fx-font-size: 22px; -fx-font-weight: bold;");

        textLogoBox.getChildren().addAll(logoTop, logoBottom);

        HBox brandBox = new HBox(10);
        brandBox.setAlignment(Pos.CENTER_LEFT);
        brandBox.getChildren().addAll(logoView, textLogoBox);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label user = new Label("Miguel - Agendador");
        user.setStyle("-fx-text-fill: #2d6dcc; -fx-font-size: 14px;");

        header.getChildren().addAll(brandBox, spacer, user);
        return header;
    }

    private Node createToolbar() {
        VBox container = new VBox(10);
        container.setPadding(new Insets(20, 40, 10, 40));

        HBox navBar = new HBox(20);
        navBar.setAlignment(Pos.CENTER_LEFT);

        Button btnAgendar = new Button("Agendar cita");
        btnAgendar.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #555;" +
                        "-fx-font-size: 16px;"
        );
        btnAgendar.setOnAction(e -> {
            try {
                new ScheduleAppointmentFrame().start(new Stage());
                stage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Button btnListar = new Button("Listar citas");
        btnListar.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #2d6dcc;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-border-color: transparent transparent #2d6dcc transparent;" +
                        "-fx-border-width: 0 0 3 0;"
        );

        navBar.getChildren().addAll(btnAgendar, btnListar);

        HBox toolbar = new HBox();
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setSpacing(20);

        Label title = new Label("Lista de Citas");
        title.setFont(Font.font("System", 30));
        title.setStyle("-fx-text-fill: #222; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        txtSearch = new TextField();
        txtSearch.setPromptText("Buscar por profesional");
        txtSearch.setPrefWidth(220);

        Button btnSearch = new Button("Buscar");

        btnSearch.setOnAction(e -> controller.onSearch());

        toolbar.getChildren().addAll(title, spacer, txtSearch, btnSearch);

        container.getChildren().addAll(navBar, toolbar);

        return container;
    }

    private Node createContent() {
        VBox wrapper = new VBox(15);
        wrapper.setPadding(new Insets(10, 40, 30, 40));

        HBox filters = new HBox(12);
        filters.setAlignment(Pos.CENTER_LEFT);

        cbProfessional = new ComboBox<>();
        cbProfessional.setPromptText("Seleccione profesional");
        cbProfessional.setPrefWidth(260);

        datePicker = new DatePicker();
        datePicker.setPrefWidth(180);
        datePicker.setValue(LocalDate.now());

        Button btnConsultar = new Button("Consultar");
        btnConsultar.setStyle("-fx-background-color: #3b86df; -fx-text-fill: white;");
        btnConsultar.setOnAction(e -> controller.onSearch());

        filters.getChildren().addAll(cbProfessional, datePicker, btnConsultar);

        VBox card = new VBox(15);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-border-color: #ddd;");

        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<AppointmentViewModel, String> colId = new TableColumn<>("Identificación");
        colId.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().patientId)
        );

        TableColumn<AppointmentViewModel, String> colDoctor = new TableColumn<>("Médico");
        colDoctor.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().doctorName)
        );

        TableColumn<AppointmentViewModel, String> colPatient = new TableColumn<>("Paciente");
        colPatient.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().patientName)
        );

        TableColumn<AppointmentViewModel, String> colDate = new TableColumn<>("Fecha");
        colDate.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().date)
        );

        TableColumn<AppointmentViewModel, String> colTime = new TableColumn<>("Hora");
        colTime.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().time)
        );

        table.getColumns().addAll(colId, colDoctor, colPatient, colDate, colTime);

        lblTotal = new Label("Total citas: 0");

        card.getChildren().addAll(table, lblTotal);
        wrapper.getChildren().addAll(filters, card);

        return wrapper;
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
            lblTotal.setText("Total citas: " + total);
        }

        @Override
        public void showAlert(String message) {
            SearchAppointmentFrame.this.showAlert(message);
        }
    }
}