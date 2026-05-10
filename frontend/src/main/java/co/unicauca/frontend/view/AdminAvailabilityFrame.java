package co.unicauca.frontend.view;

import co.unicauca.frontend.controller.AdminAvailabilityController;
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

public class AdminAvailabilityFrame extends Application {

    private AdminAvailabilityController controller;

    public ComboBox<String> cbProfessional;

    public VBox containerRows;

    public Label lblFeedback;

    public List<RowData> rows =
            new ArrayList<>();

    @Override
    public void start(Stage stage) {

        controller =
                new AdminAvailabilityController(this);

        BorderPane root =
                new BorderPane();

        root.setStyle(
                "-fx-background-color: #eef5fb;"
        );

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
                createFieldBox(
                        "Profesional",
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

        root.setCenter(scroll);

        Scene scene =
                new Scene(root, 1200, 800);

        stage.setScene(scene);

        stage.setTitle(
                "Configuración de disponibilidad"
        );

        stage.show();

        addRow();

        controller.initData();
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
                "MONDAY",
                "TUESDAY",
                "WEDNESDAY",
                "THURSDAY",
                "FRIDAY",
                "SATURDAY",
                "SUNDAY"
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
                        row.cbDay
                ),
                createFieldBox(
                        "Hora inicio",
                        row.cbStartHour
                ),
                createFieldBox(
                        "Hora fin",
                        row.cbEndHour
                ),
                createFieldBox(
                        "Duración cita",
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

        Label label =
                new Label(labelText);

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