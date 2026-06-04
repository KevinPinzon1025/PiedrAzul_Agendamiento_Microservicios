package co.unicauca.frontend.util;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.Locale;
import java.util.stream.IntStream;

/**
 * Selector de fecha de nacimiento con calendario y selección rápida de mes/año.
 *
 * Mantiene una apariencia parecida a un DatePicker, pero el popup muestra:
 * - ComboBox de mes
 * - ComboBox de año
 * - Calendario mensual para seleccionar el día
 */
public class BirthDateSelector extends HBox {

    private static final String[] MONTHS = {
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    };

    private static final String[] WEEK_DAYS = {
            "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom"
    };

    private static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final TextField editor = new TextField();
    private final Button calendarButton = new Button("▾");
    private final Popup popup = new Popup();
    private final VBox popupContent = new VBox(10);
    private final ComboBox<String> monthCombo = new ComboBox<>();
    private final ComboBox<Integer> yearCombo = new ComboBox<>();
    private final GridPane calendarGrid = new GridPane();

    private LocalDate value;
    private YearMonth visibleMonth;

    public BirthDateSelector() {
        LocalDate today = LocalDate.now();
        visibleMonth = YearMonth.from(today.minusYears(18));

        configureMainControl();
        configurePopup();
        refreshHeaderFromVisibleMonth();
        refreshCalendar();
    }

    private void configureMainControl() {
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(0);
        setStyle(
                "-fx-background-color: #f8fbff;" +
                "-fx-border-color: #c8d9eb;" +
                "-fx-border-radius: 14;" +
                "-fx-background-radius: 14;" +
                "-fx-padding: 0;"
        );

        editor.setPromptText("Seleccione la fecha");
        editor.setPrefHeight(46);
        editor.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-border-color: transparent;" +
                "-fx-font-size: 16px;" +
                "-fx-padding: 0 12 0 14;"
        );

        calendarButton.setPrefWidth(48);
        calendarButton.setPrefHeight(46);
        calendarButton.setFocusTraversable(false);
        calendarButton.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-border-color: transparent;" +
                "-fx-font-size: 18px;" +
                "-fx-text-fill: #4d5b68;"
        );

        HBox.setHgrow(editor, Priority.ALWAYS);
        getChildren().addAll(editor, calendarButton);

        editor.setOnMouseClicked(event -> showPopup());
        editor.setOnAction(event -> commitEditorText());
        editor.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (!isFocused) {
                commitEditorText();
            }
        });
        calendarButton.setOnAction(event -> showPopup());
    }

    private void configurePopup() {
        popup.setAutoHide(true);
        popupContent.setPadding(new Insets(12));
        popupContent.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #b9c9da;" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.18), 12, 0, 0, 3);"
        );

        Button previousButton = createNavButton("‹");
        Button nextButton = createNavButton("›");

        monthCombo.getItems().addAll(MONTHS);
        monthCombo.setPrefWidth(150);
        monthCombo.setStyle("-fx-font-size: 14px;");

        int currentYear = LocalDate.now().getYear();
        IntStream.rangeClosed(currentYear - 120, currentYear - 1)
                .boxed()
                .sorted((a, b) -> Integer.compare(b, a))
                .forEach(yearCombo.getItems()::add);
        yearCombo.setPrefWidth(110);
        yearCombo.setStyle("-fx-font-size: 14px;");

        previousButton.setOnAction(event -> {
            visibleMonth = visibleMonth.minusMonths(1);
            refreshHeaderFromVisibleMonth();
            refreshCalendar();
        });

        nextButton.setOnAction(event -> {
            visibleMonth = visibleMonth.plusMonths(1);
            refreshHeaderFromVisibleMonth();
            refreshCalendar();
        });

        monthCombo.valueProperty().addListener((obs, oldValue, newValue) -> updateVisibleMonthFromHeader());
        yearCombo.valueProperty().addListener((obs, oldValue, newValue) -> updateVisibleMonthFromHeader());

        HBox header = new HBox(8, previousButton, monthCombo, yearCombo, nextButton);
        header.setAlignment(Pos.CENTER);

        calendarGrid.setHgap(4);
        calendarGrid.setVgap(4);
        calendarGrid.setAlignment(Pos.CENTER);

        for (int i = 0; i < 7; i++) {
            ColumnConstraints column = new ColumnConstraints(42);
            calendarGrid.getColumnConstraints().add(column);
        }

        popupContent.getChildren().addAll(header, calendarGrid);
        popup.getContent().add(popupContent);
    }

    private Button createNavButton(String text) {
        Button button = new Button(text);
        button.setPrefSize(34, 34);
        button.setFocusTraversable(false);
        button.setStyle(
                "-fx-background-color: #eef5fc;" +
                "-fx-background-radius: 8;" +
                "-fx-border-color: #c8d9eb;" +
                "-fx-border-radius: 8;" +
                "-fx-font-size: 18px;" +
                "-fx-text-fill: #0b4778;"
        );
        return button;
    }

    private void showPopup() {
        if (getScene() == null || getScene().getWindow() == null) {
            return;
        }

        if (value != null) {
            visibleMonth = YearMonth.from(value);
            refreshHeaderFromVisibleMonth();
            refreshCalendar();
        }

        if (popup.isShowing()) {
            popup.hide();
        } else {
            popup.show(this, localToScreen(0, getHeight()).getX(), localToScreen(0, getHeight()).getY() + 4);
        }
    }

    private void refreshHeaderFromVisibleMonth() {
        monthCombo.setValue(MONTHS[visibleMonth.getMonthValue() - 1]);
        yearCombo.setValue(visibleMonth.getYear());
    }

    private void updateVisibleMonthFromHeader() {
        String month = monthCombo.getValue();
        Integer year = yearCombo.getValue();

        if (month == null || year == null) {
            return;
        }

        int monthNumber = monthCombo.getItems().indexOf(month) + 1;
        if (monthNumber < 1) {
            return;
        }

        YearMonth newVisibleMonth = YearMonth.of(year, monthNumber);
        if (!newVisibleMonth.equals(visibleMonth)) {
            visibleMonth = newVisibleMonth;
            refreshCalendar();
        }
    }

    private void refreshCalendar() {
        calendarGrid.getChildren().clear();

        for (int col = 0; col < WEEK_DAYS.length; col++) {
            Label label = new Label(WEEK_DAYS[col]);
            label.setAlignment(Pos.CENTER);
            label.setPrefSize(42, 28);
            label.setStyle("-fx-font-weight: bold; -fx-text-fill: #102a43; -fx-font-size: 13px;");
            calendarGrid.add(label, col, 0);
        }

        LocalDate firstDayOfMonth = visibleMonth.atDay(1);
        int startColumn = firstDayOfMonth.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue();
        int daysInMonth = visibleMonth.lengthOfMonth();
        LocalDate today = LocalDate.now();

        int row = 1;
        int col = startColumn;

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = visibleMonth.atDay(day);
            Button dayButton = new Button(String.valueOf(day));
            dayButton.setPrefSize(42, 34);
            dayButton.setFocusTraversable(false);

            boolean isSelected = value != null && value.equals(date);
            boolean isFutureOrToday = !date.isBefore(today);

            dayButton.setDisable(isFutureOrToday);
            dayButton.setStyle(getDayButtonStyle(isSelected, isFutureOrToday));

            dayButton.setOnAction(event -> {
                setValue(date);
                popup.hide();
            });

            calendarGrid.add(dayButton, col, row);

            col++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }
    }

    private String getDayButtonStyle(boolean isSelected, boolean isDisabled) {
        if (isSelected) {
            return "-fx-background-color: #2277bb; -fx-text-fill: white; -fx-background-radius: 8; -fx-font-weight: bold;";
        }
        if (isDisabled) {
            return "-fx-background-color: transparent; -fx-text-fill: #c5cbd3;";
        }
        return "-fx-background-color: transparent; -fx-text-fill: #102a43; -fx-background-radius: 8;";
    }

    private void commitEditorText() {
        String text = editor.getText();
        if (text == null || text.isBlank()) {
            value = null;
            return;
        }

        LocalDate parsedDate = parseDate(text.trim());
        if (parsedDate != null && parsedDate.isBefore(LocalDate.now())) {
            setValue(parsedDate);
        }
    }

    private LocalDate parseDate(String text) {
        DateTimeFormatter[] formatters = new DateTimeFormatter[] {
                DateTimeFormatter.ISO_LOCAL_DATE,
                new DateTimeFormatterBuilder()
                        .appendPattern("d/M/")
                        .appendValue(ChronoField.YEAR, 4)
                        .toFormatter(Locale.forLanguageTag("es-CO")),
                new DateTimeFormatterBuilder()
                        .appendPattern("d-M-")
                        .appendValue(ChronoField.YEAR, 4)
                        .toFormatter(Locale.forLanguageTag("es-CO"))
        };

        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDate.parse(text, formatter);
            } catch (DateTimeParseException ignored) {
                // Try next format.
            }
        }

        return null;
    }

    public LocalDate getValue() {
        commitEditorText();
        return value;
    }

    public void setValue(LocalDate date) {
        value = date;
        if (date == null) {
            editor.clear();
            return;
        }

        visibleMonth = YearMonth.from(date);
        editor.setText(DISPLAY_FORMATTER.format(date));
        refreshHeaderFromVisibleMonth();
        refreshCalendar();
    }

    public boolean isComplete() {
        return getValue() != null;
    }

    public boolean isPartiallyFilled() {
        return editor.getText() != null && !editor.getText().isBlank();
    }
}
