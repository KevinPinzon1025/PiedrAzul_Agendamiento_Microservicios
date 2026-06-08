package co.unicauca.frontend.view;

import co.unicauca.frontend.controller.AdminAvailabilityController;
import co.unicauca.frontend.dto.AuthResponse;
import co.unicauca.frontend.dto.AuthSession;
import co.unicauca.frontend.dto.WorkingDayDTO;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.text.Normalizer;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class AdminAvailabilityFrame extends Application {

    private AdminAvailabilityController controller;
    private Stage stage;

    public ComboBox<String> cbProfessional;
    public VBox containerRows;
    public Label lblFeedback;
    public Label lblConfiguredAvailabilityFeedback;
    public TableView<WorkingDayDTO> tblConfiguredAvailability;
    public List<RowData> rows = new ArrayList<>();

    private List<String> allProfessionals = new ArrayList<>();
    private boolean updatingProfessionalFilter;

    @Override
    public void start(Stage stage) {

        this.stage = stage;
        this.controller = new AdminAvailabilityController(this);

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #eef5fb;");
        root.setTop(createHeader());
        root.setCenter(createContent());

        Scene scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.setTitle("Configuracion de disponibilidad");
        stage.show();

        addRow();
        controller.initData();
    }

    private ScrollPane createContent() {

        VBox wrapper = new VBox(20);
        wrapper.setPadding(new Insets(30));
        wrapper.setAlignment(Pos.TOP_CENTER);

        VBox card = new VBox(20);
        card.setMaxWidth(1080);
        card.setPadding(new Insets(28));
        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 22;" +
                        "-fx-border-radius: 22;" +
                        "-fx-border-color: #d6e3f2;"
        );

        Label title = new Label("Configurar disponibilidad");
        title.setFont(Font.font("System", FontWeight.BOLD, 30));
        title.setStyle("-fx-text-fill: #163b5b;");

        Label subtitle = new Label(
                "Busque un profesional, edite sus franjas existentes o registre una nueva disponibilidad."
        );
        subtitle.setWrapText(true);
        subtitle.setFont(Font.font("System", 17));
        subtitle.setStyle("-fx-text-fill: #5f7387;");

        cbProfessional = new ComboBox<>();
        cbProfessional.setEditable(true);
        cbProfessional.setPromptText("Buscar nombre de profesional");
        styleField(cbProfessional);
        cbProfessional.getEditor().textProperty().addListener(
                (obs, oldValue, newValue) ->
                        updateProfessionalSuggestions(newValue)
        );
        cbProfessional.setOnAction(
                e -> controller.loadSelectedProfessionalAvailability()
        );

        lblConfiguredAvailabilityFeedback = new Label(
                "Seleccione un profesional para ver sus franjas horarias."
        );
        lblConfiguredAvailabilityFeedback.setMinHeight(46);
        lblConfiguredAvailabilityFeedback.setWrapText(true);
        lblConfiguredAvailabilityFeedback.setStyle(
                "-fx-background-color: #eef5fb;" +
                        "-fx-text-fill: #5f7387;" +
                        "-fx-padding: 12;" +
                        "-fx-background-radius: 12;"
        );

        tblConfiguredAvailability = createAvailabilityTable();

        Label registerTitle = new Label("Registrar disponibilidad");
        registerTitle.setFont(Font.font("System", FontWeight.BOLD, 22));
        registerTitle.setStyle("-fx-text-fill: #163b5b;");

        containerRows = new VBox(16);

        Button btnAddRow = new Button("Agregar franja");
        btnAddRow.setPrefHeight(46);
        btnAddRow.setStyle(
                "-fx-background-color: #1f6fb2;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;"
        );
        btnAddRow.setOnAction(e -> addRow());

        HBox actions = createSaveActions();

        lblFeedback = new Label(" ");
        lblFeedback.setMinHeight(50);
        lblFeedback.setWrapText(true);
        lblFeedback.setStyle(
                "-fx-background-color: #ecfbf1;" +
                        "-fx-text-fill: #0c5b3f;" +
                        "-fx-padding: 12;" +
                        "-fx-background-radius: 12;"
        );

        card.getChildren().addAll(
                title,
                subtitle,
                createRequiredInfoLabel(),
                createFieldBox("Profesional", true, cbProfessional),
                lblConfiguredAvailabilityFeedback,
                tblConfiguredAvailability,
                registerTitle,
                btnAddRow,
                containerRows,
                actions,
                lblFeedback
        );

        wrapper.getChildren().add(card);

        ScrollPane scroll = new ScrollPane(wrapper);
        scroll.setFitToWidth(true);
        scroll.setStyle(
                "-fx-background: #eef5fb;" +
                        "-fx-background-color: #eef5fb;"
        );

        return scroll;
    }

    private HBox createSaveActions() {

        HBox actions = new HBox(14);
        actions.setAlignment(Pos.CENTER);

        Button btnSave = new Button("Guardar disponibilidad");
        btnSave.setPrefWidth(260);
        btnSave.setPrefHeight(50);
        btnSave.setStyle(
                "-fx-background-color: #25a25a;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 14;" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;"
        );
        btnSave.setOnAction(e -> controller.saveAvailability());

        Button btnClear = new Button("Limpiar");
        btnClear.setPrefWidth(180);
        btnClear.setPrefHeight(50);
        btnClear.setStyle(
                "-fx-background-color: #eef2f7;" +
                        "-fx-text-fill: #333;" +
                        "-fx-background-radius: 14;" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;"
        );
        btnClear.setOnAction(e -> controller.clearForm());

        actions.getChildren().addAll(btnSave, btnClear);

        return actions;
    }

    private TableView<WorkingDayDTO> createAvailabilityTable() {

        TableView<WorkingDayDTO> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(310);

        TableColumn<WorkingDayDTO, String> colDay =
                new TableColumn<>("Dia");
        colDay.setCellValueFactory(cell ->
                new SimpleStringProperty(
                        toSpanishDay(cell.getValue().getDayOfWeek())
                )
        );

        TableColumn<WorkingDayDTO, String> colStart =
                new TableColumn<>("Hora inicio");
        colStart.setCellValueFactory(cell ->
                new SimpleStringProperty(
                        cell.getValue().getStartTime() == null
                                ? ""
                                : cell.getValue().getStartTime().toString()
                )
        );

        TableColumn<WorkingDayDTO, String> colEnd =
                new TableColumn<>("Hora fin");
        colEnd.setCellValueFactory(cell ->
                new SimpleStringProperty(
                        cell.getValue().getEndTime() == null
                                ? ""
                                : cell.getValue().getEndTime().toString()
                )
        );

        TableColumn<WorkingDayDTO, String> colDuration =
                new TableColumn<>("Duracion");
        colDuration.setCellValueFactory(cell ->
                new SimpleStringProperty(
                        cell.getValue().getAppointmentDurationMinutes()
                                + " min"
                )
        );

        TableColumn<WorkingDayDTO, Void> colActions =
                new TableColumn<>("Accion");
        colActions.setCellFactory(column ->
                new TableCell<>() {

                    private final Button btnEdit =
                            createTableButton("Editar", "#1f6fb2");

                    {
                        btnEdit.setOnAction(e ->
                                editWorkingDay(
                                        getTableView()
                                                .getItems()
                                                .get(getIndex())
                                )
                        );
                    }

                    @Override
                    protected void updateItem(
                            Void item,
                            boolean empty
                    ) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : btnEdit);
                    }
                }
        );

        table.getColumns().addAll(
                colDay,
                colStart,
                colEnd,
                colDuration,
                colActions
        );

        return table;
    }

    private HBox createHeader() {

        HBox header = new HBox(16);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(18, 30, 18, 30));
        header.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #d6e3f2;" +
                        "-fx-border-width: 0 0 1 0;"
        );

        Label brand = new Label("Administrador - Piedrazul");
        brand.setFont(Font.font("System", FontWeight.BOLD, 24));
        brand.setStyle("-fx-text-fill: #1f6fb2;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        VBox userBox = new VBox(3);
        userBox.setAlignment(Pos.CENTER_RIGHT);

        AuthResponse currentUser = AuthSession.getCurrentUser();
        String displayName =
                currentUser != null
                        ? currentUser.getDisplayName()
                        : "Administrador";
        String role =
                currentUser != null && currentUser.getRole() != null
                        ? currentUser.getRole()
                        : "";

        Label lblUser = new Label(displayName);
        lblUser.setStyle(
                "-fx-text-fill: #204968;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;"
        );

        Label lblRole = new Label(
                role.isBlank() ? "Sesion iniciada" : "Rol: " + role
        );
        lblRole.setStyle(
                "-fx-text-fill: #5f7387;" +
                        "-fx-font-size: 14px;"
        );

        userBox.getChildren().addAll(lblUser, lblRole);

        Button btnLogout = new Button("Cerrar sesion");
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

        header.getChildren().addAll(
                brand,
                spacer,
                userBox,
                btnLogout
        );

        return header;
    }

    private void logout() {
        try {
            AuthSession.clear();
            new LoginFrame().start(new Stage());
            stage.close();
        } catch (Exception e) {
            showAlert("No fue posible cerrar sesion.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sesion");
        alert.setHeaderText("Resultado");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void addRow() {

        RowData row = new RowData();
        rows.add(row);

        HBox line = new HBox(12);
        line.setAlignment(Pos.CENTER_LEFT);

        row.cbDay = new ComboBox<>();
        row.cbDay.getItems().addAll(
                "Lunes",
                "Martes",
                "Miercoles",
                "Jueves",
                "Viernes",
                "Sabado",
                "Domingo"
        );
        row.cbDay.setPromptText("Dia");
        styleField(row.cbDay);

        row.cbStartHour = createHourCombo();
        row.cbEndHour = createHourCombo();

        row.cbDuration = createDurationCombo();
        row.cbDuration.setPromptText("Duracion");

        Button btnDelete = new Button("X");
        btnDelete.setStyle(
                "-fx-background-color: #d9534f;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 10;"
        );
        btnDelete.setOnAction(e -> {
            containerRows.getChildren().remove(line);
            rows.remove(row);
        });

        line.getChildren().addAll(
                createFieldBox("Dia", true, row.cbDay),
                createFieldBox("Hora inicio", true, row.cbStartHour),
                createFieldBox("Hora fin", true, row.cbEndHour),
                createFieldBox("Duracion cita", true, row.cbDuration),
                btnDelete
        );

        containerRows.getChildren().add(line);
    }

    private ComboBox<String> createHourCombo() {

        ComboBox<String> combo = new ComboBox<>();

        for (int hour = 6; hour <= 20; hour++) {
            combo.getItems().add(String.format("%02d:00", hour));
            combo.getItems().add(String.format("%02d:30", hour));
        }

        styleField(combo);

        return combo;
    }

    private ComboBox<String> createDurationCombo() {

        ComboBox<String> combo = new ComboBox<>();
        combo.getItems().addAll("10", "15", "20", "30", "45", "60");
        styleField(combo);

        return combo;
    }

    private VBox createFieldBox(String labelText, Control field) {
        return createFieldBox(labelText, false, field);
    }

    private VBox createFieldBox(
            String labelText,
            boolean required,
            Control field
    ) {

        Label label = new Label(required ? labelText + " *" : labelText);
        label.setFont(Font.font("System", FontWeight.BOLD, 16));
        label.setStyle("-fx-text-fill: #244b68;");

        VBox box = new VBox(8);
        box.getChildren().addAll(label, field);

        return box;
    }

    private Label createRequiredInfoLabel() {

        Label label = new Label("Los campos con * son obligatorios.");
        label.setWrapText(true);
        label.setFont(Font.font("System", 15));
        label.setStyle("-fx-text-fill: #6b7e90;");

        return label;
    }

    private void styleField(Control control) {

        control.setPrefHeight(46);
        control.setStyle(
                "-fx-background-color: #f8fbff;" +
                        "-fx-border-color: #c8d9eb;" +
                        "-fx-border-radius: 12;" +
                        "-fx-background-radius: 12;" +
                        "-fx-font-size: 16px;"
        );
    }

    private Button createTableButton(String text, String color) {

        Button button = new Button(text);
        button.setPrefHeight(34);
        button.setStyle(
                "-fx-background-color: " + color + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-font-weight: bold;"
        );

        return button;
    }

    private void editWorkingDay(WorkingDayDTO workingDay) {

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Editar franja horaria");
        dialog.setHeaderText(null);

        ButtonType saveButton =
                new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(
                saveButton,
                ButtonType.CANCEL
        );

        ComboBox<String> cbStart = createHourCombo();
        cbStart.setValue(
                workingDay.getStartTime() == null
                        ? null
                        : workingDay.getStartTime().toString()
        );

        ComboBox<String> cbEnd = createHourCombo();
        cbEnd.setValue(
                workingDay.getEndTime() == null
                        ? null
                        : workingDay.getEndTime().toString()
        );

        ComboBox<String> cbDuration = createDurationCombo();
        cbDuration.setValue(
                String.valueOf(
                        workingDay.getAppointmentDurationMinutes()
                )
        );

        GridPane form = new GridPane();
        form.setHgap(14);
        form.setVgap(14);
        form.setPadding(new Insets(12));
        form.add(createFieldBox("Hora inicio", true, cbStart), 0, 0);
        form.add(createFieldBox("Hora fin", true, cbEnd), 1, 0);
        form.add(createFieldBox("Duracion", true, cbDuration), 0, 1);

        dialog.getDialogPane().setContent(form);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == saveButton) {
            controller.updateAvailability(
                    workingDay,
                    LocalTime.parse(cbStart.getValue()),
                    LocalTime.parse(cbEnd.getValue()),
                    Integer.parseInt(cbDuration.getValue())
            );
        }
    }

    public void setProfessionals(List<String> professionals) {

        allProfessionals = new ArrayList<>(professionals);
        updateProfessionalItems(professionals, "");
    }

    private void updateProfessionalSuggestions(String text) {

        if (updatingProfessionalFilter ||
                cbProfessional == null ||
                allProfessionals == null) {

            return;
        }

        String normalizedSearch = normalize(text);

        if (countNonBlankCharacters(normalizedSearch) < 2) {
            updateProfessionalItems(allProfessionals, text);
            cbProfessional.hide();
            return;
        }

        List<String> matches =
                allProfessionals.stream()
                        .filter(professional ->
                                normalize(professional)
                                        .contains(normalizedSearch)
                        )
                        .toList();

        updateProfessionalItems(matches, text);

        if (matches.isEmpty()) {
            cbProfessional.hide();
        } else {
            cbProfessional.show();
        }
    }

    private void updateProfessionalItems(
            List<String> professionals,
            String editorText
    ) {

        updatingProfessionalFilter = true;

        cbProfessional.getItems().setAll(professionals);
        cbProfessional.getEditor().setText(editorText);
        cbProfessional.getEditor().positionCaret(
                editorText == null ? 0 : editorText.length()
        );

        updatingProfessionalFilter = false;
    }

    private int countNonBlankCharacters(String text) {

        if (text == null) {
            return 0;
        }

        return (int) text.chars()
                .filter(ch -> !Character.isWhitespace(ch))
                .count();
    }

    private String normalize(String value) {

        if (value == null) {
            return "";
        }

        return Normalizer.normalize(
                        value.trim(),
                        Normalizer.Form.NFD
                )
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT);
    }

    public String getProfessionalSearchText() {

        return cbProfessional.isEditable()
                ? cbProfessional.getEditor().getText()
                : cbProfessional.getValue();
    }

    public void setConfiguredAvailability(
            List<WorkingDayDTO> workingDays
    ) {

        tblConfiguredAvailability.getItems().setAll(workingDays);
        lblConfiguredAvailabilityFeedback.setStyle(
                "-fx-background-color: #eef5fb;" +
                        "-fx-text-fill: #5f7387;" +
                        "-fx-padding: 12;" +
                        "-fx-background-radius: 12;"
        );
        lblConfiguredAvailabilityFeedback.setText(
                workingDays.isEmpty()
                        ? "El profesional seleccionado no tiene franjas registradas."
                        : "Franjas registradas: " + workingDays.size()
        );
    }

    public void clearConfiguredAvailability() {

        tblConfiguredAvailability.getItems().clear();
        lblConfiguredAvailabilityFeedback.setStyle(
                "-fx-background-color: #eef5fb;" +
                        "-fx-text-fill: #5f7387;" +
                        "-fx-padding: 12;" +
                        "-fx-background-radius: 12;"
        );
        lblConfiguredAvailabilityFeedback.setText(
                "Seleccione un profesional para ver sus franjas horarias."
        );
    }

    public void showConfiguredAvailabilityError(String message) {

        lblConfiguredAvailabilityFeedback.setStyle(
                "-fx-background-color: #fdecec;" +
                        "-fx-text-fill: #9b1c1c;" +
                        "-fx-padding: 12;" +
                        "-fx-background-radius: 12;"
        );
        lblConfiguredAvailabilityFeedback.setText(message);
    }

    private String toSpanishDay(DayOfWeek day) {

        if (day == null) {
            return "";
        }

        return switch (day) {
            case MONDAY -> "Lunes";
            case TUESDAY -> "Martes";
            case WEDNESDAY -> "Miercoles";
            case THURSDAY -> "Jueves";
            case FRIDAY -> "Viernes";
            case SATURDAY -> "Sabado";
            case SUNDAY -> "Domingo";
        };
    }

    public static class RowData {

        public ComboBox<String> cbDay;
        public ComboBox<String> cbStartHour;
        public ComboBox<String> cbEndHour;
        public ComboBox<String> cbDuration;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
