
package co.unicauca.frontend;

import co.unicauca.frontend.view.SearchAppointmentFrame;
import co.unicauca.frontend.view.SelfServiceAppointmentFrame;
import javafx.application.Application;

public class Frontend {

    public static void main(String[] args) {
       // Application.launch(SearchAppointmentFrame.class, args); //-> front de citas manuales
        Application.launch(SelfServiceAppointmentFrame.class, args); //-> front de citas autonomas (en proceso aun)
    }
}

/*
TODO
    -la cita se registra pero, donde esta registrando la hora? habra que modificar eso cuando se carguen los slots de los medicos
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
     -En el backend configurar el DTO que recibe para gestionar agendador y recibir si es una cita manual o una cita autonoma
 */