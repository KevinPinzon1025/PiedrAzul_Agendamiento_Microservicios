
package co.unicauca.frontend;

import co.unicauca.frontend.view.SearchAppointmentFrame;
import javafx.application.Application;

public class Frontend {

    public static void main(String[] args) {
        Application.launch(SearchAppointmentFrame.class, args);
    }
}

/*
TODO
    - Endpoint para obtener horarios disponibles de los medicos
    en el CONTROLLER DEL BACKEND:
        @GetMapping("/available-hours")
        public ResponseEntity<List<String>> getAvailableHours(
                @RequestParam String professional,
                @RequestParam LocalDate date
        )
     en el APPOINTMENTSERVICE del BACKEND:
        List<String> getAvailableHours(
        String professional,
        LocalDate date
        )
    - modificar el post en el backend para recibir el CreateAppointmentRequestDTO y luego mappear a appointment
        -DTO equivalente en el backend para CreateAppointmentRequest
 */