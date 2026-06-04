package co.unicauca.frontend.view;

import co.unicauca.frontend.client.AuthHttpClient;
import co.unicauca.frontend.dto.RegisterRequest;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class RegisterFrame extends Application {

    private static final String PRIMARY = "#1f6fb2";
    private static final String BACKGROUND = "#eef5fb";
    private static final String INPUT = "#f8fbff";

    private static final Pattern DIGITS_PATTERN = Pattern.compile("^[0-9]+$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[\\p{L} ]+$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private final AuthHttpClient authHttpClient = new AuthHttpClient();
    private TextField txtDocumentNumber;
    private TextField txtFirstName;
    private TextField txtSecondName;
    private TextField txtFirstLastName;
    private TextField txtSecondLastName;
    private TextField txtPhone;
    private ComboBox<String> cbGender;
    private DatePicker dpBirthDate;
    private TextField txtEmail;
    private PasswordField txtPassword;
    private PasswordField txtConfirmPassword;
    private Label lblFeedback;
    private Stage stage;

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(28));
        root.setStyle("-fx-background-color: " + BACKGROUND + ";");

        VBox card = new VBox(18);
        card.setAlignment(Pos.TOP_CENTER);
        card.setMaxWidth(840);
        card.setPadding(new Insets(32));
        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 26;" +
                        "-fx-border-radius: 26;" +
                        "-fx-border-color: #d6e3f2;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 18, 0.20, 0, 5);"
        );

        Label title = new Label("Crear cuenta de paciente");
        title.setFont(Font.font("System", FontWeight.BOLD, 30));
        title.setStyle("-fx-text-fill: #163b5b;");

        Label subtitle = new Label("Ingrese sus datos personales. El número de documento será usado para iniciar sesión.");
        subtitle.setWrapText(true);
        subtitle.setFont(Font.font("System", 18));
        subtitle.setStyle("-fx-text-fill: #48637d;");



        Label requiredInfo = createRequiredInfoLabel();
        card.getChildren().addAll(title, subtitle, requiredInfo, createForm(), createActions());

        ScrollPane scrollPane = new ScrollPane(card);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scrollPane.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(scrollPane, javafx.scene.layout.Priority.ALWAYS);
        root.getChildren().add(scrollPane);

        Scene scene = new Scene(root, 1040, 760);
        stage.setScene(scene);
        stage.setTitle("Registro de paciente - Piedrazul");
        stage.show();
    }

    private GridPane createForm() {
        GridPane form = new GridPane();
        form.setHgap(22);
        form.setVgap(16);
        form.setAlignment(Pos.CENTER);
        form.setPadding(new Insets(6, 0, 6, 0));

        txtDocumentNumber = new TextField();
        txtDocumentNumber.setPromptText("Solo números, entre 6 y 15 dígitos");
        styleInput(txtDocumentNumber);
        allowOnlyDigits(txtDocumentNumber, 15);

        txtFirstName = new TextField();
        txtFirstName.setPromptText("Solo letras");
        styleInput(txtFirstName);
        allowOnlyLetters(txtFirstName, 80);

        txtSecondName = new TextField();
        txtSecondName.setPromptText("Opcional, solo letras");
        styleInput(txtSecondName);
        allowOnlyLetters(txtSecondName, 80);

        txtFirstLastName = new TextField();
        txtFirstLastName.setPromptText("Solo letras");
        styleInput(txtFirstLastName);
        allowOnlyLetters(txtFirstLastName, 80);

        txtSecondLastName = new TextField();
        txtSecondLastName.setPromptText("Opcional, solo letras");
        styleInput(txtSecondLastName);
        allowOnlyLetters(txtSecondLastName, 80);

        txtPhone = new TextField();
        txtPhone.setPromptText("Solo números, entre 7 y 15 dígitos");
        styleInput(txtPhone);
        allowOnlyDigits(txtPhone, 15);

        cbGender = new ComboBox<>();
        cbGender.getItems().addAll("Hombre", "Mujer", "Otro");
        cbGender.setPromptText("Seleccione género");
        cbGender.setPrefWidth(360);
        cbGender.setPrefHeight(50);
        cbGender.setStyle(
                "-fx-background-color: " + INPUT + ";" +
                        "-fx-border-color: #c8d9eb;" +
                        "-fx-border-radius: 14;" +
                        "-fx-background-radius: 14;" +
                        "-fx-font-size: 18px;"
        );

        dpBirthDate = new DatePicker();
        dpBirthDate.setPromptText("Opcional, no puede ser futura");
        dpBirthDate.setPrefWidth(360);
        dpBirthDate.setPrefHeight(50);
        dpBirthDate.setStyle("-fx-font-size: 18px;");
        dpBirthDate.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (!empty && !date.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #eeeeee; -fx-text-fill: #999;");
                }
            }
        });

        txtEmail = new TextField();
        txtEmail.setPromptText("Opcional, ejemplo: correo@dominio.com");
        styleInput(txtEmail);

        txtPassword = new PasswordField();
        txtPassword.setPromptText("Mínimo 6 caracteres");
        styleInput(txtPassword);
        limitLength(txtPassword, 72);

        txtConfirmPassword = new PasswordField();
        txtConfirmPassword.setPromptText("Repita la contraseña");
        styleInput(txtConfirmPassword);
        limitLength(txtConfirmPassword, 72);

        lblFeedback = new Label();
        lblFeedback.setWrapText(true);
        lblFeedback.setMinHeight(Region.USE_PREF_SIZE);
        lblFeedback.setFont(Font.font("System", 16));
        lblFeedback.setStyle(
                "-fx-text-fill: #8f1d1d;" +
                        "-fx-background-color: #fff2f2;" +
                        "-fx-background-radius: 14;" +
                        "-fx-padding: 12;"
        );
        hideFeedback();

        form.add(createFieldLabel("Número de documento", true), 0, 0);
        form.add(txtDocumentNumber, 1, 0);
        form.add(createFieldLabel("Primer nombre", true), 0, 2);
        form.add(txtFirstName, 1, 2);
        form.add(createFieldLabel("Segundo nombre"), 0, 3);
        form.add(txtSecondName, 1, 3);
        form.add(createFieldLabel("Primer apellido", true), 0, 4);
        form.add(txtFirstLastName, 1, 4);
        form.add(createFieldLabel("Segundo apellido"), 0, 5);
        form.add(txtSecondLastName, 1, 5);
        form.add(createFieldLabel("Celular", true), 0, 6);
        form.add(txtPhone, 1, 6);
        form.add(createFieldLabel("Género", true), 0, 7);
        form.add(cbGender, 1, 7);
        form.add(createFieldLabel("Fecha de nacimiento"), 0, 8);
        form.add(dpBirthDate, 1, 8);
        form.add(createFieldLabel("Correo electrónico"), 0, 9);
        form.add(txtEmail, 1, 9);
        form.add(createFieldLabel("Contraseña", true), 0, 10);
        form.add(txtPassword, 1, 10);
        form.add(createFieldLabel("Confirmar contraseña", true), 0, 11);
        form.add(txtConfirmPassword, 1, 11);
        form.add(lblFeedback, 0, 12, 2, 1);

        return form;
    }

    private VBox createActions() {
        VBox actions = new VBox(14);
        actions.setAlignment(Pos.CENTER);

        Button btnRegister = new Button("Registrarme");
        btnRegister.setPrefWidth(320);
        btnRegister.setPrefHeight(54);
        btnRegister.setFont(Font.font("System", FontWeight.BOLD, 20));
        btnRegister.setStyle(
                "-fx-background-color: " + PRIMARY + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 16;" +
                        "-fx-cursor: hand;"
        );
        btnRegister.setOnAction(e -> register());

        Button btnBackToLogin = new Button("Volver al login");
        btnBackToLogin.setPrefWidth(320);
        btnBackToLogin.setPrefHeight(44);
        btnBackToLogin.setFont(Font.font("System", FontWeight.BOLD, 16));
        btnBackToLogin.setStyle(
                "-fx-background-color: #e8f1fb;" +
                        "-fx-text-fill: #215f98;" +
                        "-fx-background-radius: 14;" +
                        "-fx-cursor: hand;"
        );
        btnBackToLogin.setOnAction(e -> openLogin());

        actions.getChildren().addAll(btnRegister, btnBackToLogin);
        return actions;
    }

    private Label createFieldLabel(String text) {
        return createFieldLabel(text, false);
    }

    private Label createFieldLabel(String text, boolean required) {
        Label label = new Label(required ? text + " *" : text);
        label.setFont(Font.font("System", FontWeight.BOLD, 18));
        label.setStyle("-fx-text-fill: #244b68;");
        return label;
    }

    private Label createRequiredInfoLabel() {
        Label label = new Label("Los campos con * son obligatorios.");
        label.setWrapText(true);
        label.setFont(Font.font("System", 15));
        label.setStyle("-fx-text-fill: #6b7e90;");
        return label;
    }

    private void styleInput(TextField field) {
        field.setPrefHeight(50);
        field.setPrefWidth(360);
        field.setFont(Font.font("System", 18));
        field.setStyle(
                "-fx-background-color: " + INPUT + ";" +
                        "-fx-border-color: #c8d9eb;" +
                        "-fx-border-radius: 14;" +
                        "-fx-background-radius: 14;" +
                        "-fx-padding: 10 14 10 14;"
        );
    }

    private void allowOnlyDigits(TextField field, int maxLength) {
        field.textProperty().addListener((obs, oldValue, newValue) -> {
            String filtered = newValue == null ? "" : newValue.replaceAll("[^0-9]", "");
            if (filtered.length() > maxLength) {
                filtered = filtered.substring(0, maxLength);
            }
            if (!filtered.equals(newValue)) {
                field.setText(filtered);
            }
        });
    }

    private void allowOnlyLetters(TextField field, int maxLength) {
        field.textProperty().addListener((obs, oldValue, newValue) -> {
            String filtered = newValue == null ? "" : newValue.replaceAll("[^\\p{L} ]", "");
            filtered = filtered.replaceAll("\\s{2,}", " ");
            if (filtered.length() > maxLength) {
                filtered = filtered.substring(0, maxLength);
            }
            if (!filtered.equals(newValue)) {
                field.setText(filtered);
            }
        });
    }

    private void limitLength(TextField field, int maxLength) {
        field.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && newValue.length() > maxLength) {
                field.setText(newValue.substring(0, maxLength));
            }
        });
    }

    private void register() {
        List<String> errors = validateForm();
        if (!errors.isEmpty()) {
            lblFeedback.setText(String.join("\n", errors));
            return;
        }

        try {
            RegisterRequest request = new RegisterRequest(
                    txtDocumentNumber.getText().trim(),
                    txtFirstName.getText().trim(),
                    cleanOptional(txtSecondName.getText()),
                    txtFirstLastName.getText().trim(),
                    cleanOptional(txtSecondLastName.getText()),
                    txtPhone.getText().trim(),
                    cbGender.getValue(),
                    dpBirthDate.getValue(),
                    cleanOptional(txtEmail.getText()),
                    txtPassword.getText()
            );

            authHttpClient.register(request);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registro exitoso");
            alert.setHeaderText("Usuario creado correctamente");
            alert.setContentText("Ahora puede iniciar sesión con su número de documento y la contraseña registrada.");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
            openLogin();
        } catch (Exception e) {
            showFeedback(cleanErrorMessage(e));
        }
    }

    private String cleanErrorMessage(Exception exception) {
        Throwable current = exception;

        while (current.getCause() != null) {
            current = current.getCause();
        }

        String message = current.getMessage();

        if (message == null || message.isBlank()) {
            return "No se pudo registrar. Revise los datos e intente nuevamente.";
        }

        return message
                .replace("java.lang.RuntimeException: ", "")
                .trim();
    }

    private void showFeedback(String message) {
        lblFeedback.setText(message);
        lblFeedback.setVisible(true);
        lblFeedback.setManaged(true);
    }

    private void hideFeedback() {
        lblFeedback.setText("");
        lblFeedback.setVisible(false);
        lblFeedback.setManaged(false);
    }

    private List<String> validateForm() {
        List<String> errors = new ArrayList<>();

        String document = txtDocumentNumber.getText().trim();
        String firstName = txtFirstName.getText().trim();
        String secondName = txtSecondName.getText().trim();
        String firstLastName = txtFirstLastName.getText().trim();
        String secondLastName = txtSecondLastName.getText().trim();
        String phone = txtPhone.getText().trim();
        String gender = cbGender.getValue();
        LocalDate birthDate = dpBirthDate.getValue();
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();

        if (!isDigitsInRange(document, 6, 15)) {
            errors.add("El número de documento debe tener entre 6 y 15 dígitos.");
        }

        if (!isValidRequiredName(firstName)) {
            errors.add("El primer nombre debe tener solo letras y mínimo 2 caracteres.");
        }

        if (!isBlankOrValidName(secondName)) {
            errors.add("El segundo nombre solo puede tener letras y espacios.");
        }

        if (!isValidRequiredName(firstLastName)) {
            errors.add("El primer apellido debe tener solo letras y mínimo 2 caracteres.");
        }

        if (!isBlankOrValidName(secondLastName)) {
            errors.add("El segundo apellido solo puede tener letras y espacios.");
        }

        if (!isDigitsInRange(phone, 7, 15)) {
            errors.add("El celular debe tener entre 7 y 15 dígitos.");
        }

        if (gender == null || !List.of("Hombre", "Mujer", "Otro").contains(gender)) {
            errors.add("Debe seleccionar un género válido.");
        }

        if (birthDate != null && !birthDate.isBefore(LocalDate.now())) {
            errors.add("La fecha de nacimiento debe ser anterior a la fecha actual.");
        }

        if (!email.isBlank() && !EMAIL_PATTERN.matcher(email).matches()) {
            errors.add("El correo electrónico no tiene un formato válido.");
        }

        if (password == null || password.length() < 6 || password.length() > 72) {
            errors.add("La contraseña debe tener mínimo 6 caracteres y máximo 72.");
        }

        if (confirmPassword == null || !confirmPassword.equals(password)) {
            errors.add("La confirmación de contraseña no coincide.");
        }

        return errors;
    }

    private boolean isDigitsInRange(String value, int min, int max) {
        return value != null
                && DIGITS_PATTERN.matcher(value).matches()
                && value.length() >= min
                && value.length() <= max;
    }

    private boolean isValidRequiredName(String value) {
        return value != null
                && value.length() >= 2
                && value.length() <= 80
                && NAME_PATTERN.matcher(value).matches();
    }

    private boolean isBlankOrValidName(String value) {
        return value == null
                || value.isBlank()
                || (value.length() <= 80 && NAME_PATTERN.matcher(value).matches());
    }

    private String cleanOptional(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    private void openLogin() {
        try {
            new LoginFrame().start(new Stage());
            stage.close();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("No se pudo completar la operación");
        alert.setContentText(message);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }
}
