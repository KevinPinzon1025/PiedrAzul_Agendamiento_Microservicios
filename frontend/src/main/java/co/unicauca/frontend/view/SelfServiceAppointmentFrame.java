
package co.unicauca.frontend.view;

import co.unicauca.frontend.controller.SelfServiceAppointmentController;
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

public class SelfServiceAppointmentFrame extends Application {

    private SelfServiceAppointmentController controller;

    public ComboBox<String> cbProfessional;
    public DatePicker datePicker;
    public ComboBox<String> cbTime;
    public TextArea txtMotivo;
    public Label lblFeedback;
    public Label lblHelpBox;

    @Override
    public void start(Stage stage) {

        this.controller = new SelfServiceAppointmentController(this);

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f4f6fb;");

        root.setTop(createHeader());

        HBox mainContent = new HBox(20);
        mainContent.setAlignment(Pos.CENTER);
        mainContent.setPadding(new Insets(30));

        mainContent.getChildren().addAll(
                createForm(),
                createHelpPanel()
        );

        root.setCenter(mainContent);

        Scene scene = new Scene(root, 1100, 650);

        stage.setScene(scene);

        stage.setTitle(
                "Autogestión de Citas - Servicios Médicos Piedrazul"
        );

        stage.show();

        controller.initData();
    }

    private Node createHeader() {

        HBox header = new HBox();

        header.setAlignment(Pos.CENTER_LEFT);

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

        header.getChildren().add(brand);

        return header;
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

        grid.add(new Label("Profesional:"), 0, 0);

        grid.add(cbProfessional, 0, 1);

        grid.add(new Label("Fecha deseada:"), 1, 0);

        grid.add(datePicker, 1, 1);

        grid.add(new Label("Hora:"), 0, 2);

        grid.add(cbTime, 0, 3);

        grid.add(
                new Label("Motivo de consulta:"),
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
                grid,
                actions,
                lblFeedback
        );

        controller.setupHelpListeners();

        return card;
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

    public static void main(String[] args) {

        launch(args);
    }
}