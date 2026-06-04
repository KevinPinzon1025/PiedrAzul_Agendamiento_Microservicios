package co.unicauca.frontend.util;

import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public final class BirthDatePickerUtil {

    private static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final List<DateTimeFormatter> ACCEPTED_FORMATTERS = List.of(
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("d/M/yyyy"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("d-M-yyyy"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy")
    );

    private BirthDatePickerUtil() {
    }

    public static void configure(DatePicker datePicker) {
        datePicker.setEditable(true);
        datePicker.setPromptText("Fecha de nacimiento * (aaaa-mm-dd)");
        datePicker.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return date == null ? "" : DISPLAY_FORMATTER.format(date);
            }

            @Override
            public LocalDate fromString(String value) {
                if (value == null || value.trim().isEmpty()) {
                    return null;
                }

                String normalized = value.trim();
                for (DateTimeFormatter formatter : ACCEPTED_FORMATTERS) {
                    try {
                        return LocalDate.parse(normalized, formatter);
                    } catch (DateTimeParseException ignored) {
                        // Try next accepted format.
                    }
                }

                throw new DateTimeParseException(
                        "Formato de fecha inválido. Use aaaa-mm-dd, por ejemplo 2006-07-03.",
                        normalized,
                        0
                );
            }
        });

        datePicker.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (!isFocused) {
                commitEditorText(datePicker);
            }
        });

        datePicker.setOnAction(event -> commitEditorText(datePicker));

        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (!empty && !date.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #eeeeee; -fx-text-fill: #999;");
                }
            }
        });
    }

    public static boolean hasInvalidEditorText(DatePicker datePicker) {
        String text = datePicker.getEditor().getText();
        if (text == null || text.trim().isEmpty()) {
            return false;
        }

        try {
            datePicker.getConverter().fromString(text);
            return false;
        } catch (RuntimeException exception) {
            return true;
        }
    }

    private static void commitEditorText(DatePicker datePicker) {
        String text = datePicker.getEditor().getText();
        if (text == null || text.trim().isEmpty()) {
            datePicker.setValue(null);
            return;
        }

        try {
            LocalDate parsedDate = datePicker.getConverter().fromString(text);
            datePicker.setValue(parsedDate);
            datePicker.getEditor().setText(datePicker.getConverter().toString(parsedDate));
        } catch (RuntimeException ignored) {
            // Keep the typed text so validation can show a friendly message.
        }
    }
}
