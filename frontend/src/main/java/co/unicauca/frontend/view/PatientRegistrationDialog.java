package co.unicauca.frontend.view;

import co.unicauca.frontend.client.PatientHttpClient;
import co.unicauca.frontend.dto.CreatePatientRequestDTO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;

public class PatientRegistrationDialog {

    private final Stage stage = new Stage();

    private final PatientHttpClient patientHttpClient =
            new PatientHttpClient();

    private final Runnable onPatientCreated;

    private TextField txtDocumentNumber;
    private TextField txtFirstName;
    private TextField txtFirstLastName;
    private TextField txtPhone;
    private TextField txtEmail;
    private ComboBox<String> cbGender;
    private DatePicker dpBirthDate;
    private Label lblFeedback;

    public PatientRegistrationDialog(
            Stage owner,
            Runnable onPatientCreated
    ) {
        this.onPatientCreated = onPatientCreated;

        stage.initOwner(owner);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Registrar paciente");
        stage.setScene(createScene());
    }

    public void show() {
        stage.showAndWait();
    }

    private Scene createScene() {

        VBox root = new VBox(24);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(34));
        root.setStyle("-fx-background-color: white;");

        Label title = new Label("Registrar paciente");
        title.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        30
                )
        );
        title.setStyle("-fx-text-fill: #102a43;");

        lblFeedback = new Label();
        lblFeedback.setWrapText(true);
        lblFeedback.setVisible(false);
        lblFeedback.setManaged(false);
        lblFeedback.setStyle(
                "-fx-text-fill: #8f1d1d;" +
                        "-fx-background-color: #fff2f2;" +
                        "-fx-background-radius: 14;" +
                        "-fx-padding: 12;"
        );

        root.getChildren().addAll(
                title,
                createRequiredInfoLabel(),
                createForm(),
                lblFeedback,
                createActions()
        );

        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        scrollPane.setStyle("-fx-background-color: white; -fx-background: white;");

        return new Scene(scrollPane, 780, 560);
    }

    private GridPane createForm() {

        GridPane form = new GridPane();
        form.setHgap(22);
        form.setVgap(18);
        form.setAlignment(Pos.CENTER);

        txtDocumentNumber = createTextField("Documento *");
        txtFirstName = createTextField("Nombres *");
        txtFirstLastName = createTextField("Apellidos *");
        txtPhone = createTextField("Celular *");
        txtEmail = createTextField("Correo electrónico");

        cbGender = new ComboBox<>();
        cbGender.getItems().addAll(
                "Masculino",
                "Femenino",
                "Otro"
        );
        cbGender.setPromptText("Género *");
        cbGender.setPrefWidth(280);
        cbGender.setPrefHeight(46);

        dpBirthDate = new DatePicker();
        dpBirthDate.setPromptText("Fecha de nacimiento");
        dpBirthDate.setPrefWidth(280);
        dpBirthDate.setPrefHeight(46);

        form.add(txtDocumentNumber, 0, 0);
        form.add(txtFirstLastName, 1, 0);

        form.add(txtFirstName, 0, 1);
        form.add(cbGender, 1, 1);

        form.add(txtPhone, 0, 2);
        form.add(txtEmail, 1, 2);

        form.add(dpBirthDate, 0, 3);

        return form;
    }

    private Label createRequiredInfoLabel() {
        Label label = new Label("Los campos con * son obligatorios.");
        label.setWrapText(true);
        label.setStyle("-fx-text-fill: #6b7e90;");
        return label;
    }

    private TextField createTextField(String prompt) {

        TextField textField = new TextField();
        textField.setPromptText(prompt);
        textField.setPrefWidth(280);
        textField.setPrefHeight(46);
        textField.setFont(Font.font("System", 16));

        return textField;
    }

    private HBox createActions() {

        HBox actions = new HBox(24);
        actions.setAlignment(Pos.CENTER);

        Button btnCancel = new Button("Cancelar");
        btnCancel.setPrefWidth(180);
        btnCancel.setPrefHeight(50);
        btnCancel.setStyle(
                "-fx-background-color: #ff3b3f;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-font-weight: bold;"
        );

        btnCancel.setOnAction(
                e -> stage.close()
        );

        Button btnRegister = new Button("Registrar");
        btnRegister.setPrefWidth(180);
        btnRegister.setPrefHeight(50);
        btnRegister.setStyle(
                "-fx-background-color: #2fac66;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-font-weight: bold;"
        );

        btnRegister.setOnAction(
                e -> registerPatient()
        );

        actions.getChildren().addAll(
                btnCancel,
                btnRegister
        );

        return actions;
    }

    private void registerPatient() {

        if (!validateForm()) {
            return;
        }

        try {

            CreatePatientRequestDTO request =
                    new CreatePatientRequestDTO(
                            txtFirstName.getText().trim(),
                            null,
                            txtFirstLastName.getText().trim(),
                            null,
                            "CC",
                            txtDocumentNumber.getText().trim(),
                            cleanOptional(txtEmail.getText()),
                            txtPhone.getText().trim(),
                            cbGender.getValue(),
                            dpBirthDate.getValue()
                    );

            patientHttpClient.createPatient(request);

            Alert alert =
                    new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("Paciente registrado");
            alert.setHeaderText("Paciente creado correctamente");
            alert.setContentText(
                    "El paciente fue guardado en el microservicio de pacientes."
            );
            alert.getDialogPane()
                    .setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();

            if (onPatientCreated != null) {
                onPatientCreated.run();
            }

            stage.close();

        } catch (Exception e) {

            e.printStackTrace();

            showFeedback(
                    "No se pudo registrar el paciente. Revise que el documento no exista y que patient-service esté corriendo."
            );
        }
    }

    private boolean validateForm() {

        String document =
                txtDocumentNumber.getText().trim();

        String firstName =
                txtFirstName.getText().trim();

        String firstLastName =
                txtFirstLastName.getText().trim();

        String phone =
                txtPhone.getText().trim();

        if (document.isBlank() ||
                !document.matches("\\d+")) {

            showFeedback(
                    "El documento es obligatorio y debe contener solo números."
            );
            return false;
        }

        if (firstName.isBlank()) {

            showFeedback(
                    "El nombre es obligatorio."
            );
            return false;
        }

        if (firstLastName.isBlank()) {

            showFeedback(
                    "El apellido es obligatorio."
            );
            return false;
        }

        if (phone.isBlank() ||
                !phone.matches("\\d+")) {

            showFeedback(
                    "El celular es obligatorio y debe contener solo números."
            );
            return false;
        }

        if (cbGender.getValue() == null) {

            showFeedback(
                    "Debe seleccionar un género."
            );
            return false;
        }

        if (dpBirthDate.getValue() != null &&
                !dpBirthDate.getValue().isBefore(LocalDate.now())) {

            showFeedback(
                    "La fecha de nacimiento debe ser anterior a hoy."
            );
            return false;
        }

        return true;
    }

    private String cleanOptional(String value) {

        if (value == null ||
                value.isBlank()) {
            return null;
        }

        return value.trim();
    }

    private void showFeedback(String message) {

        lblFeedback.setText(message);
        lblFeedback.setVisible(true);
        lblFeedback.setManaged(true);
    }
}