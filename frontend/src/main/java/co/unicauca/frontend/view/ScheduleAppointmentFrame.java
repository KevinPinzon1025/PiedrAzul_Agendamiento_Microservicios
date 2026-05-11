
package co.unicauca.frontend.view;

import co.unicauca.frontend.controller.ScheduleAppointmentController;
import co.unicauca.frontend.dto.AuthResponse;
import co.unicauca.frontend.dto.AuthSession;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.time.LocalDate;

public class ScheduleAppointmentFrame extends Application {

    private ScheduleAppointmentController controller;

    public ComboBox<String> cbPatient;
    public ComboBox<String> cbProfessional;
    public DatePicker datePicker;
    public ComboBox<String> cbTime;
    public TextArea txtMotivo;
    public Label lblFeedback;

    private Stage stage;

    @Override
    public void start(Stage stage) {

        this.stage = stage;

        this.controller =
                new ScheduleAppointmentController(this);

        BorderPane root = new BorderPane();

        root.setStyle("-fx-background-color: #eef5fb;");

        VBox topContainer = new VBox(12);

        topContainer.setPadding(
                new Insets(18, 26, 8, 26)
        );

        topContainer.getChildren().addAll(
                createHeader(),
                createToolbar()
        );

        root.setTop(topContainer);

        root.setCenter(
                createScrollableContent(
                        createForm()
                )
        );

        Scene scene = new Scene(root, 1220, 780);

        stage.setScene(scene);

        stage.setTitle("Agendar cita");

        stage.show();

        controller.initData();
    }

    private Node createScrollableContent(Node content) {

        ScrollPane scrollPane = new ScrollPane(content);

        scrollPane.setFitToWidth(true);

        scrollPane.setPannable(true);

        scrollPane.setStyle(
                "-fx-background-color: #eef5fb;" +
                        "-fx-background: #eef5fb;"
        );

        return scrollPane;
    }

    private Node createHeader() {

        HBox header = new HBox(16);

        header.setAlignment(Pos.CENTER_LEFT);

        header.setPadding(
                new Insets(20, 24, 20, 24)
        );

        header.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 22;" +
                        "-fx-border-radius: 22;" +
                        "-fx-border-color: #d6e3f2;"
        );

        Label brand = new Label(
                "Servicios médicos Piedrazul"
        );

        brand.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        28
                )
        );

        brand.setStyle(
                "-fx-text-fill: #1f6fb2;"
        );

        Region spacer = new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        VBox userBox = new VBox(4);

        userBox.setAlignment(Pos.CENTER_RIGHT);

        AuthResponse currentUser =
                AuthSession.getCurrentUser();

        String displayName = currentUser != null ? currentUser.getDisplayName() : "Usuario";

        String role =
                currentUser != null &&
                        currentUser.getRole() != null
                        ? currentUser.getRole()
                        : "";

        Label lblUser = new Label(displayName);

        lblUser.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        19
                )
        );

        lblUser.setStyle(
                "-fx-text-fill: #204968;"
        );

        Label lblRole = new Label(
                role.isBlank()
                        ? "Sesión iniciada"
                        : "Rol: " + role
        );

        lblRole.setFont(
                Font.font("System", 16)
        );

        lblRole.setStyle(
                "-fx-text-fill: #5f7387;"
        );

        userBox.getChildren().addAll(
                lblUser,
                lblRole
        );

        Button btnLogout = new Button(
                "Cerrar sesión"
        );

        btnLogout.setPrefHeight(44);

        btnLogout.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        17
                )
        );

        btnLogout.setStyle(
                "-fx-background-color: #e8f1fb;" +
                        "-fx-text-fill: #1f6fb2;" +
                        "-fx-background-radius: 14;" +
                        "-fx-cursor: hand;"
        );

        btnLogout.setOnAction(
                e -> logout()
        );

        header.getChildren().addAll(
                brand,
                spacer,
                userBox,
                btnLogout
        );

        return header;
    }

    private Node createToolbar() {

        HBox toolbar = new HBox(20);

        toolbar.setPadding(
                new Insets(10, 10, 0, 10)
        );

        toolbar.setAlignment(Pos.CENTER_LEFT);

        Button btnAgendar =
                createNavButton(
                        "Agendar cita",
                        true
                );

        Button btnListar =
                createNavButton(
                        "Consultar citas",
                        false
                );

        btnListar.setOnAction(e -> {

            try {

                new SearchAppointmentFrame()
                        .start(new Stage());

                stage.close();

            } catch (Exception ex) {

                ex.printStackTrace();
            }
        });

        toolbar.getChildren().addAll(
                btnAgendar,
                btnListar
        );

        return toolbar;
    }

    private Node createForm() {

        VBox wrapper = new VBox(18);

        wrapper.setAlignment(Pos.TOP_CENTER);

        wrapper.setPadding(
                new Insets(12, 36, 30, 36)
        );

        VBox card = new VBox(20);

        card.setMaxWidth(900);

        card.setPadding(new Insets(28));

        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 22;" +
                        "-fx-border-radius: 22;" +
                        "-fx-border-color: #d6e3f2;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 18, 0.20, 0, 5);"
        );

        Label title = new Label(
                "Agendar una cita"
        );

        title.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        32
                )
        );

        title.setStyle(
                "-fx-text-fill: #163b5b;"
        );

        Label subtitle = new Label(
                "Seleccione paciente, profesional y fecha. Luego escriba el motivo de la consulta."
        );

        subtitle.setWrapText(true);

        subtitle.setFont(
                Font.font("System", 18)
        );

        subtitle.setStyle(
                "-fx-text-fill: #5f7387;"
        );

        lblFeedback = new Label(" ");

        lblFeedback.setWrapText(true);

        lblFeedback.setMinHeight(54);

        lblFeedback.setFont(
                Font.font("System", 16)
        );

        lblFeedback.setStyle(
                "-fx-text-fill: #0c5b3f;" +
                        "-fx-background-color: #ecfbf1;" +
                        "-fx-background-radius: 14;" +
                        "-fx-padding: 12;"
        );

        card.getChildren().addAll(
                title,
                subtitle,
                createTopActions(),
                createFormGrid(),
                createBottomActions(),
                lblFeedback
        );

        wrapper.getChildren().add(card);

        return wrapper;
    }

    private HBox createTopActions() {

        HBox topActions = new HBox(14);

        topActions.setAlignment(Pos.CENTER_LEFT);

        Button btnNewPatient =
                createSecondaryButton(
                        "Nuevo paciente"
                );

        btnNewPatient.setOnAction(e -> {

            PatientRegistrationDialog dialog =
                    new PatientRegistrationDialog(
                            stage,
                            () -> {
                                try {
                                    Thread.sleep(700);
                                } catch (InterruptedException ex) {
                                    Thread.currentThread().interrupt();
                                }

                                controller.initData();

                                lblFeedback.setStyle(
                                        "-fx-text-fill: #0c5b3f;" +
                                                "-fx-background-color: #ecfbf1;" +
                                                "-fx-background-radius: 14;" +
                                                "-fx-padding: 12;"
                                );

                                lblFeedback.setText(
                                        "Paciente creado correctamente. La lista de pacientes fue actualizada."
                                );
                            }
                    );

            dialog.show();
        });

        Button btnConsultarHorarios =
                createSecondaryButton(
                        "Consultar horarios"
                );

        btnConsultarHorarios.setOnAction(e -> {

            try {

                String professional =
                        cbProfessional.getValue();

                LocalDate date =
                        datePicker.getValue();

                if (professional == null ||
                        date == null) {

                    lblFeedback.setText(
                            "Seleccione profesional y fecha primero."
                    );

                    return;
                }

                ConsultScheduleFrame consultFrame =
                        new ConsultScheduleFrame(
                                stage,
                                professional,
                                date
                        );

                consultFrame.show();

            } catch (Exception ex) {

                ex.printStackTrace();

                lblFeedback.setText(
                        "No fue posible abrir la consulta de horarios."
                );
            }
        });

        topActions.getChildren().addAll(
                btnNewPatient,
                btnConsultarHorarios
        );

        return topActions;
    }

    private Node createFormGrid() {

        javafx.scene.layout.GridPane form =
                new javafx.scene.layout.GridPane();

        form.setHgap(20);

        form.setVgap(18);

        form.setAlignment(Pos.TOP_CENTER);

        ColumnConstraints col1 =
                new ColumnConstraints();

        col1.setPercentWidth(50);

        ColumnConstraints col2 =
                new ColumnConstraints();

        col2.setPercentWidth(50);

        form.getColumnConstraints().addAll(
                col1,
                col2
        );

        cbPatient = new ComboBox<>();
        cbPatient.setPromptText("Seleccione un paciente");
        styleField(cbPatient);

        cbProfessional = new ComboBox<>();
        cbProfessional.setPromptText("Seleccione un profesional");
        styleField(cbProfessional);

        datePicker = new DatePicker(LocalDate.now());
        styleField(datePicker);

        cbTime = new ComboBox<>();
        cbTime.setPromptText("Hora de la cita");
        styleField(cbTime);

        txtMotivo = new TextArea();

        txtMotivo.setPromptText(
                "Describa brevemente el motivo de la consulta"
        );

        txtMotivo.setWrapText(true);

        txtMotivo.setPrefHeight(150);

        txtMotivo.setFont(
                Font.font("System", 17)
        );

        txtMotivo.setStyle(
                "-fx-background-color: #f8fbff;" +
                        "-fx-border-color: #c8d9eb;" +
                        "-fx-border-radius: 14;" +
                        "-fx-background-radius: 14;" +
                        "-fx-padding: 12;"
        );

        form.add(createFieldBox("Paciente", cbPatient), 0, 0);

        form.add(createFieldBox("Profesional", cbProfessional), 1, 0);

        form.add(createFieldBox("Fecha", datePicker), 0, 1);

        form.add(createFieldBox("Hora", cbTime), 1, 1);

        form.add(
                createFieldBox(
                        "Motivo de la consulta",
                        txtMotivo
                ),
                0,
                2,
                2,
                1
        );

        cbProfessional.valueProperty().addListener(
                (obs, oldVal, newVal) ->
                        controller.loadAvailableHours()
        );

        datePicker.valueProperty().addListener(
                (obs, oldVal, newVal) ->
                        controller.loadAvailableHours()
        );

        datePicker.setDayCellFactory(
                picker -> new DateCell() {

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

        return form;
    }

    private VBox createFieldBox(
            String labelText,
            Control field
    ) {

        Label label = new Label(labelText);

        label.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        18
                )
        );

        label.setStyle(
                "-fx-text-fill: #244b68;"
        );

        VBox box = new VBox(8);

        box.getChildren().addAll(
                label,
                field
        );

        return box;
    }

    private HBox createBottomActions() {

        HBox actions = new HBox(14);

        actions.setAlignment(Pos.CENTER);

        Button btnRegister =
                new Button("Registrar cita");

        btnRegister.setPrefWidth(220);

        btnRegister.setPrefHeight(52);

        btnRegister.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        19
                )
        );

        btnRegister.setStyle(
                "-fx-background-color: #25a25a;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 14;"
        );

        btnRegister.setOnAction(
                e -> controller.registerAppointment()
        );

        Button btnClear =
                new Button("Limpiar formulario");

        btnClear.setPrefWidth(220);

        btnClear.setPrefHeight(52);

        btnClear.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        19
                )
        );

        btnClear.setStyle(
                "-fx-background-color: #eef2f7;" +
                        "-fx-text-fill: #333;" +
                        "-fx-background-radius: 14;" +
                        "-fx-border-color: #cfd6df;" +
                        "-fx-border-radius: 14;"
        );

        btnClear.setOnAction(
                e -> controller.clearForm()
        );

        actions.getChildren().addAll(
                btnRegister,
                btnClear
        );

        return actions;
    }

    private Button createSecondaryButton(String text) {

        Button button = new Button(text);

        button.setPrefHeight(46);

        button.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        17
                )
        );

        button.setStyle(
                "-fx-background-color: #f8fbff;" +
                        "-fx-text-fill: #1f6fb2;" +
                        "-fx-border-color: #b7cdfa;" +
                        "-fx-border-radius: 12;" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 0 18 0 18;"
        );

        return button;
    }

    private Button createNavButton(
            String text,
            boolean active
    ) {

        Button button = new Button(text);

        button.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        18
                )
        );

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

    private void styleField(Control control) {

        control.setPrefHeight(50);

        control.setMaxWidth(Double.MAX_VALUE);

        control.setStyle(
                "-fx-background-color: #f8fbff;" +
                        "-fx-border-color: #c8d9eb;" +
                        "-fx-border-radius: 14;" +
                        "-fx-background-radius: 14;" +
                        "-fx-font-size: 17px;"
        );
    }

    private void logout() {

        try {

            AuthSession.clear();

            new LoginFrame()
                    .start(new Stage());

            stage.close();

        } catch (Exception e) {

            lblFeedback.setText(
                    "No fue posible cerrar sesión."
            );
        }
    }

    public static void main(String[] args) {

        launch(args);
    }
}