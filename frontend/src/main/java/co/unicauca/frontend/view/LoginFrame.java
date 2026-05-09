package co.unicauca.frontend.view;

import co.unicauca.frontend.client.AuthHttpClient;
//import co.unicauca.frontend.dto.AuthResponse;
//import co.unicauca.frontend.dto.AuthSession;
//import co.unicauca.frontend.dto.LoginRequest;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class LoginFrame extends Application {

    private static final String PRIMARY = "#1f6fb2";
    private static final String BACKGROUND = "#eef5fb";
    private static final String CARD = "#ffffff";
    private static final String INPUT = "#f8fbff";

    private final AuthHttpClient authHttpClient = new AuthHttpClient();
    private TextField txtLogin;
    private Label lblFeedback;
    private Stage stage;

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        HBox root = new HBox(30);
        root.setPadding(new Insets(35));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: " + BACKGROUND + ";");

        VBox welcomePanel = createWelcomePanel();
        VBox loginCard = createLoginCard();

        root.getChildren().addAll(welcomePanel, loginCard);

        Scene scene = new Scene(root, 1040, 700);
        stage.setScene(scene);
        stage.setTitle("Inicio de sesión - Piedrazul");
        stage.show();
    }

    private VBox createWelcomePanel() {
        VBox panel = new VBox(18);
        panel.setAlignment(Pos.CENTER_LEFT);
        panel.setPadding(new Insets(40));
        panel.setPrefWidth(430);
        panel.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, #2d86c9, #1f5f98);" +
                        "-fx-background-radius: 26;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 18, 0.25, 0, 6);"
        );

        try {
            Image logo = new Image(getClass().getResourceAsStream("/logo_piedrazul.png"));
            ImageView logoView = new ImageView(logo);
            logoView.setFitWidth(150);
            logoView.setPreserveRatio(true);
            panel.getChildren().add(logoView);
        } catch (Exception ignored) {
        }

        Label title = new Label("Bienvenido a Piedrazul");
        title.setWrapText(true);
        title.setFont(Font.font("System", FontWeight.BOLD, 30));
        title.setStyle("-fx-text-fill: white;");

        Label subtitle = new Label("Una aplicación clara y cómoda para consultar y gestionar citas médicas.");
        subtitle.setWrapText(true);
        subtitle.setFont(Font.font("System", 20));
        subtitle.setStyle("-fx-text-fill: #edf6ff;");

        VBox tips = new VBox(12,
                createTip("1. Ingrese su número de identificación."),
                createTip("2. Si aún no tiene cuenta, use el botón 'Crear cuenta'."),
                createTip("3. Después del ingreso podrá consultar o agendar citas.")
        );
        tips.setPadding(new Insets(10, 0, 0, 0));

        panel.getChildren().addAll(title, subtitle, tips);
        return panel;
    }

    private Label createTip(String text) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.setFont(Font.font("System", 18));
        label.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-background-color: rgba(255,255,255,0.16);" +
                        "-fx-background-radius: 14;" +
                        "-fx-padding: 12 14 12 14;"
        );
        return label;
    }

    private VBox createLoginCard() {
        VBox card = new VBox(20);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPrefWidth(480);
        card.setMaxWidth(480);
        card.setPadding(new Insets(34));
        card.setStyle(
                "-fx-background-color: " + CARD + ";" +
                        "-fx-background-radius: 26;" +
                        "-fx-border-radius: 26;" +
                        "-fx-border-color: #d6e3f2;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 18, 0.20, 0, 5);"
        );

        Label title = new Label("Iniciar sesión");
        title.setFont(Font.font("System", FontWeight.BOLD, 30));
        title.setStyle("-fx-text-fill: #163b5b;");

        
        VBox form = createForm();
        VBox actions = createActions();

        card.getChildren().addAll(title, form, actions);
        return card;
    }

    private VBox createForm() {
        VBox form = new VBox(12);
        form.setAlignment(Pos.CENTER_LEFT);
        form.setPadding(new Insets(10, 0, 0, 0));

        Label lblLogin = createFieldLabel("Número de identificación");
        txtLogin = new TextField();
        txtLogin.setPromptText("Escriba su número de documento");
        styleInput(txtLogin);
        allowOnlyDigits(txtLogin, 15);

        lblFeedback = new Label(" ");
        lblFeedback.setWrapText(true);
        lblFeedback.setMinHeight(54);
        lblFeedback.setFont(Font.font("System", 16));
        lblFeedback.setStyle(
                "-fx-text-fill: #8f1d1d;" +
                        "-fx-background-color: #fff2f2;" +
                        "-fx-background-radius: 14;" +
                        "-fx-padding: 12;"
        );

        form.getChildren().addAll(lblLogin, txtLogin, lblFeedback);
        return form;
    }

    private VBox createActions() {
        VBox actions = new VBox(14);
        actions.setAlignment(Pos.CENTER);
        actions.setPadding(new Insets(8, 0, 0, 0));

        Button btnLogin = new Button("Ingresar");
        btnLogin.setDefaultButton(true);
        btnLogin.setPrefWidth(320);
        btnLogin.setPrefHeight(54);
        btnLogin.setFont(Font.font("System", FontWeight.BOLD, 20));
        btnLogin.setStyle(
                "-fx-background-color: " + PRIMARY + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 16;" +
                        "-fx-cursor: hand;"
        );
        btnLogin.setOnAction(e -> login());

        Hyperlink linkRegister = new Hyperlink("Crear cuenta nueva");
        linkRegister.setFont(Font.font("System", 18));
        linkRegister.setStyle("-fx-text-fill: #215f98;");
        linkRegister.setOnAction(e -> openRegister());

        Label helpText = new Label("Si tiene dificultades para ingresar, solicite ayuda al personal de apoyo.");
        helpText.setWrapText(true);
        helpText.setFont(Font.font("System", 15));
        helpText.setStyle("-fx-text-fill: #6b7e90;");
        helpText.setMaxWidth(360);
        helpText.setAlignment(Pos.CENTER);

        actions.getChildren().addAll(btnLogin, linkRegister, helpText);
        return actions;
    }

    private Label createFieldLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("System", FontWeight.BOLD, 18));
        label.setStyle("-fx-text-fill: #244b68;");
        return label;
    }

    private void styleInput(TextField field) {
        field.setPrefHeight(50);
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

    private void login() {

        String documentNumber = txtLogin.getText().trim();
        if (!documentNumber.matches("^[0-9]{6,15}$")) {
            lblFeedback.setText("El número de identificación debe tener entre 6 y 15 dígitos.");
            return;
        }

        try {


            new SearchAppointmentFrame().start(new Stage());
            stage.close();
        } catch (Exception e) {
            lblFeedback.setText("No fue posible iniciar sesión. Revise su número de identificación o verifique que AuthService esté disponible.");
            showError(e.getMessage());
        }
    }

    private void openRegister() {
        try {
            new RegisterFrame().start(new Stage());
            stage.close();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("No se pudo iniciar sesión");
        alert.setContentText(message);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }
}
