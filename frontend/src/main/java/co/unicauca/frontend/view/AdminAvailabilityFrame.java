package co.unicauca.frontend.view;

import co.unicauca.frontend.controller.AdminAvailabilityController;
import co.unicauca.frontend.dto.AuthResponse;
import co.unicauca.frontend.dto.AuthSession;
import co.unicauca.frontend.dto.WorkingDayDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
        import javafx.scene.layout.*;
        import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.DayOfWeek;

public class AdminAvailabilityFrame extends Application {

    private AdminAvailabilityController controller;
    private Stage stage;

    public ComboBox<String> cbProfessional;

    public VBox containerRows;

    public Label lblFeedback;

    public Label lblConfiguredAvailabilityFeedback;

    public TableView<WorkingDayDTO> tblConfiguredAvailability;

    public List<RowData> rows =
            new ArrayList<>();

    @Override
    public void start(Stage stage) {

        this.stage = stage;
        controller =
                new AdminAvailabilityController(this);

        BorderPane root =
                new BorderPane();

        root.setStyle(
                "-fx-background-color: #eef5fb;"
        );

        root.setTop(createHeader());

        VBox wrapper =
                new VBox(20);

        wrapper.setPadding(
                new Insets(30)
        );

        wrapper.setAlignment(Pos.TOP_CENTER);

        VBox card =
                new VBox(20);

        card.setMaxWidth(1000);

        card.setPadding(
                new Insets(28)
        );

        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 22;" +
                        "-fx-border-radius: 22;" +
                        "-fx-border-color: #d6e3f2;"
        );

        Label title =
                new Label(
                        "Configurar disponibilidad"
                );

        title.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        30
                )
        );

        title.setStyle(
                "-fx-text-fill: #163b5b;"
        );

        Label subtitle =
                new Label(
                        "Configure las franjas horarias de atención para el profesional."
                );

        subtitle.setFont(
                Font.font("System", 17)
        );

        subtitle.setStyle(
                "-fx-text-fill: #5f7387;"
        );

        cbProfessional =
                new ComboBox<>();

        cbProfessional.setPromptText(
                "Seleccione un profesional"
        );

        styleField(cbProfessional);

        containerRows =
                new VBox(16);

        Button btnAddRow =
                new Button(
                        "Agregar franja"
                );

        btnAddRow.setPrefHeight(46);

        btnAddRow.setStyle(
                "-fx-background-color: #1f6fb2;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;"
        );

        btnAddRow.setOnAction(
                e -> addRow()
        );

        HBox actions =
                new HBox(14);

        actions.setAlignment(Pos.CENTER);

        Button btnSave =
                new Button(
                        "Guardar disponibilidad"
                );

        btnSave.setPrefWidth(260);

        btnSave.setPrefHeight(50);

        btnSave.setStyle(
                "-fx-background-color: #25a25a;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 14;" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;"
        );

        btnSave.setOnAction(
                e -> controller.saveAvailability()
        );

        Button btnClear =
                new Button(
                        "Limpiar"
                );

        btnClear.setPrefWidth(180);

        btnClear.setPrefHeight(50);

        btnClear.setStyle(
                "-fx-background-color: #eef2f7;" +
                        "-fx-text-fill: #333;" +
                        "-fx-background-radius: 14;" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;"
        );

        btnClear.setOnAction(
                e -> controller.clearForm()
        );

        actions.getChildren().addAll(
                btnSave,
                btnClear
        );

        lblFeedback =
                new Label(" ");

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
                createFieldBox(
                        "Profesional",
                        true,
                        cbProfessional
                ),
                btnAddRow,
                containerRows,
                actions,
                lblFeedback
        );

        wrapper.getChildren().add(card);

        ScrollPane scroll =
                new ScrollPane(wrapper);

        scroll.setFitToWidth(true);

        scroll.setStyle(
                "-fx-background: #eef5fb;" +
                        "-fx-background-color: #eef5fb;"
        );

        Tab tabConfigure =
                new Tab(
                        "Configurar disponibilidad",
                        scroll
                );

        tabConfigure.setClosable(false);

        Tab tabList =
                new Tab(
                        "Ver franjas horarias",
                        createAvailabilityListContent()
                );

        tabList.setClosable(false);

        TabPane tabs =
                new TabPane(
                        tabConfigure,
                        tabList
                );

        tabs.setStyle(
                "-fx-background-color: #eef5fb;" +
                        "-fx-font-size: 16px;"
        );

        tabList.setOnSelectionChanged(e -> {

            if (tabList.isSelected()) {

                controller.loadConfiguredAvailability();
            }
        });

        root.setCenter(tabs);

        Scene scene =
                new Scene(root, 1200, 800);

        stage.setScene(scene);

        stage.setTitle(
                "Configuración de disponibilidad"
        );

        stage.show();

        addRow();

        controller.initData();
        controller.loadConfiguredAvailability();
    }

    private ScrollPane createAvailabilityListContent() {

        VBox wrapper =
                new VBox(20);

        wrapper.setPadding(
                new Insets(30)
        );

        wrapper.setAlignment(Pos.TOP_CENTER);

        VBox card =
                new VBox(18);

        card.setMaxWidth(1080);

        card.setPadding(
                new Insets(28)
        );

        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 22;" +
                        "-fx-border-radius: 22;" +
                        "-fx-border-color: #d6e3f2;"
        );

        Label title =
                new Label("Franjas horarias configuradas");

        title.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        30
                )
        );

        title.setStyle(
                "-fx-text-fill: #163b5b;"
        );

        lblConfiguredAvailabilityFeedback =
                new Label(" ");

        lblConfiguredAvailabilityFeedback.setMinHeight(46);

        lblConfiguredAvailabilityFeedback.setWrapText(true);

        lblConfiguredAvailabilityFeedback.setStyle(
                "-fx-background-color: #eef5fb;" +
                        "-fx-text-fill: #5f7387;" +
                        "-fx-padding: 12;" +
                        "-fx-background-radius: 12;"
        );

        tblConfiguredAvailability =
                createAvailabilityTable();

        Button btnRefresh =
                new Button("Actualizar");

        btnRefresh.setPrefHeight(46);

        btnRefresh.setStyle(
                "-fx-background-color: #1f6fb2;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;"
        );

        btnRefresh.setOnAction(
                e -> controller.loadConfiguredAvailability()
        );

        HBox actions =
                new HBox(btnRefresh);

        actions.setAlignment(Pos.CENTER_RIGHT);

        card.getChildren().addAll(
                title,
                lblConfiguredAvailabilityFeedback,
                tblConfiguredAvailability,
                actions
        );

        wrapper.getChildren().add(card);

        ScrollPane scrollPane =
                new ScrollPane(wrapper);

        scrollPane.setFitToWidth(true);

        scrollPane.setStyle(
                "-fx-background: #eef5fb;" +
                        "-fx-background-color: #eef5fb;"
        );

        return scrollPane;
    }

    private TableView<WorkingDayDTO> createAvailabilityTable() {

        TableView<WorkingDayDTO> table =
                new TableView<>();

        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        table.setPrefHeight(470);

        TableColumn<WorkingDayDTO, String> colProfessional =
                new TableColumn<>("Profesional");

        colProfessional.setCellValueFactory(cell ->
                new SimpleStringProperty(
                        cell.getValue().getProfessionalName() == null
                                ? ""
                                : cell.getValue().getProfessionalName()
                )
        );

        TableColumn<WorkingDayDTO, String> colDay =
                new TableColumn<>("Día");

        colDay.setCellValueFactory(cell ->
                new SimpleStringProperty(
                        toSpanishDay(
                                cell.getValue().getDayOfWeek()
                        )
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
                new TableColumn<>("Duración");

        colDuration.setCellValueFactory(cell ->
                new SimpleStringProperty(
                        cell.getValue()
                                .getAppointmentDurationMinutes()
                                + " min"
                )
        );

        TableColumn<WorkingDayDTO, Void> colActions =
                new TableColumn<>("Acciones");

        colActions.setCellFactory(column ->
                new TableCell<>() {

                    private final Button btnEdit =
                            createTableButton(
                                    "Editar",
                                    "#1f6fb2"
                            );

                    private final Button btnDelete =
                            createTableButton(
                                    "Eliminar",
                                    "#d9534f"
                            );

                    private final HBox box =
                            new HBox(
                                    8,
                                    btnEdit,
                                    btnDelete
                            );

                    {
                        box.setAlignment(Pos.CENTER);

                        btnEdit.setOnAction(e ->
                                editDuration(
                                        getTableView()
                                                .getItems()
                                                .get(getIndex())
                                )
                        );

                        btnDelete.setOnAction(e ->
                                confirmDelete(
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

                        setGraphic(empty ? null : box);
                    }
                }
        );

        table.getColumns().addAll(
                colProfessional,
                colDay,
                colStart,
                colEnd,
                colDuration,
                colActions
        );

        return table;
    }

    private HBox createHeader() {

        HBox header =
                new HBox(16);

        header.setAlignment(Pos.CENTER_LEFT);

        header.setPadding(
                new Insets(18, 30, 18, 30)
        );

        header.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #d6e3f2;" +
                        "-fx-border-width: 0 0 1 0;"
        );

        Label brand =
                new Label("Administrador - Piedrazul");

        brand.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        24
                )
        );

        brand.setStyle(
                "-fx-text-fill: #1f6fb2;"
        );

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        VBox userBox =
                new VBox(3);

        userBox.setAlignment(Pos.CENTER_RIGHT);

        AuthResponse currentUser =
                AuthSession.getCurrentUser();

        String displayName =
                currentUser != null
                        ? currentUser.getDisplayName()
                        : "Administrador";

        String role =
                currentUser != null && currentUser.getRole() != null
                        ? currentUser.getRole()
                        : "";

        Label lblUser =
                new Label(displayName);

        lblUser.setStyle(
                "-fx-text-fill: #204968;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;"
        );

        Label lblRole =
                new Label(role.isBlank() ? "Sesión iniciada" : "Rol: " + role);

        lblRole.setStyle(
                "-fx-text-fill: #5f7387;" +
                        "-fx-font-size: 14px;"
        );

        userBox.getChildren().addAll(
                lblUser,
                lblRole
        );

        Button btnLogout =
                new Button("Cerrar sesión");

        btnLogout.setPrefHeight(40);

        btnLogout.setStyle(
                "-fx-background-color: #e8f1fb;" +
                        "-fx-text-fill: #1f6fb2;" +
                        "-fx-background-radius: 14;" +
                        "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
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

    public void addRow() {

        RowData row =
                new RowData();

        rows.add(row);

        HBox line =
                new HBox(12);

        line.setAlignment(Pos.CENTER_LEFT);

        row.cbDay =
                new ComboBox<>();

        row.cbDay.getItems().addAll(
                "Lunes",
                "Martes",
                "Miércoles",
                "Jueves",
                "Viernes",
                "Sábado",
                "Domingo"
        );

        row.cbDay.setPromptText("Día");

        styleField(row.cbDay);

        row.cbStartHour =
                createHourCombo();

        row.cbEndHour =
                createHourCombo();

        row.cbDuration =
                new ComboBox<>();

        row.cbDuration.getItems().addAll(
                "10",
                "15",
                "20",
                "30",
                "45",
                "60"
        );

        row.cbDuration.setPromptText(
                "Duración"
        );

        styleField(row.cbDuration);

        Button btnDelete =
                new Button("X");

        btnDelete.setStyle(
                "-fx-background-color: #d9534f;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 10;"
        );

        btnDelete.setOnAction(e -> {

            containerRows.getChildren()
                    .remove(line);

            rows.remove(row);
        });

        line.getChildren().addAll(
                createFieldBox(
                        "Día",
                        true,
                        row.cbDay
                ),
                createFieldBox(
                        "Hora inicio",
                        true,
                        row.cbStartHour
                ),
                createFieldBox(
                        "Hora fin",
                        true,
                        row.cbEndHour
                ),
                createFieldBox(
                        "Duración cita",
                        true,
                        row.cbDuration
                ),
                btnDelete
        );

        containerRows.getChildren().add(line);
    }

    private ComboBox<String> createHourCombo() {

        ComboBox<String> combo =
                new ComboBox<>();

        for (int hour = 6;
             hour <= 20;
             hour++) {

            combo.getItems().add(
                    String.format(
                            "%02d:00",
                            hour
                    )
            );

            combo.getItems().add(
                    String.format(
                            "%02d:30",
                            hour
                    )
            );
        }

        styleField(combo);

        return combo;
    }

    private VBox createFieldBox(
            String labelText,
            Control field
    ) {
        return createFieldBox(labelText, false, field);
    }

    private VBox createFieldBox(
            String labelText,
            boolean required,
            Control field
    ) {

        Label label =
                new Label(required ? labelText + " *" : labelText);

        label.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        16
                )
        );

        label.setStyle(
                "-fx-text-fill: #244b68;"
        );

        VBox box =
                new VBox(8);

        box.getChildren().addAll(
                label,
                field
        );

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

    private Button createTableButton(
            String text,
            String color
    ) {

        Button button =
                new Button(text);

        button.setPrefHeight(34);

        button.setStyle(
                "-fx-background-color: " + color + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-font-weight: bold;"
        );

        return button;
    }

    private void editDuration(
            WorkingDayDTO workingDay
    ) {

        ChoiceDialog<String> dialog =
                new ChoiceDialog<>(
                        String.valueOf(
                                workingDay
                                        .getAppointmentDurationMinutes()
                        ),
                        "10",
                        "15",
                        "20",
                        "30",
                        "45",
                        "60"
                );

        dialog.setTitle("Editar duración");
        dialog.setHeaderText(null);
        dialog.setContentText("Duración de la cita en minutos:");

        Optional<String> result =
                dialog.showAndWait();

        result.ifPresent(value ->
                controller.updateDuration(
                        workingDay,
                        Integer.parseInt(value)
                )
        );
    }

    private void confirmDelete(
            WorkingDayDTO workingDay
    ) {

        Alert alert =
                new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Eliminar franja");
        alert.setHeaderText(null);
        alert.setContentText(
                "¿Desea eliminar esta franja horaria?"
        );

        Optional<ButtonType> result =
                alert.showAndWait();

        if (result.isPresent() &&
                result.get() == ButtonType.OK) {

            controller.deleteAvailability(workingDay);
        }
    }

    public void setConfiguredAvailability(
            List<WorkingDayDTO> workingDays
    ) {

        tblConfiguredAvailability.getItems()
                .setAll(workingDays);

        lblConfiguredAvailabilityFeedback.setStyle(
                "-fx-background-color: #eef5fb;" +
                        "-fx-text-fill: #5f7387;" +
                        "-fx-padding: 12;" +
                        "-fx-background-radius: 12;"
        );

        lblConfiguredAvailabilityFeedback.setText(
                workingDays.isEmpty()
                        ? "No hay franjas horarias configuradas."
                        : "Franjas configuradas: " + workingDays.size()
        );
    }

    public void showConfiguredAvailabilityError(
            String message
    ) {

        lblConfiguredAvailabilityFeedback.setStyle(
                "-fx-background-color: #fdecec;" +
                        "-fx-text-fill: #9b1c1c;" +
                        "-fx-padding: 12;" +
                        "-fx-background-radius: 12;"
        );

        lblConfiguredAvailabilityFeedback.setText(message);
    }

    private String toSpanishDay(
            DayOfWeek day
    ) {

        if (day == null) {

            return "";
        }

        return switch (day) {
            case MONDAY -> "Lunes";
            case TUESDAY -> "Martes";
            case WEDNESDAY -> "Miércoles";
            case THURSDAY -> "Jueves";
            case FRIDAY -> "Viernes";
            case SATURDAY -> "Sábado";
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
