/*
package co.unicauca.frontend.controller;



import co.unicauca.frontend.client.AppointmentHttpClient;
import co.unicauca.frontend.dto.AppointmentDTO;
import co.unicauca.frontend.mapper.AppointmentMapper;
import co.unicauca.frontend.viewmodel.AppointmentViewModel;

import java.time.LocalDate;
import java.util.List;

public class SearchAppointmentController {

    public interface View {
        void setProfessionals(List<String> professionals);

        String getSelectedProfessional();
        LocalDate getSelectedDate();
        String getSearchText();

        void clearAppointments();
        void setAppointments(List<AppointmentViewModel> appointments);
        void setTotal(int total);

        void showAlert(String message);
    }

    private final View view;
    private final AppointmentHttpClient apiClient;

    public SearchAppointmentController(View view) {
        this.view = view;
        this.apiClient = new AppointmentHttpClient();
    }

    public void onInit() {
        view.setProfessionals(apiClient.getAllProfessionals());
        view.setTotal(0);
        view.clearAppointments();
    }

    public void onSearch() {
        view.clearAppointments();

        String professional = view.getSelectedProfessional();
        LocalDate date = view.getSelectedDate();

        if (professional == null || date == null) {
            view.setTotal(0);
            view.showAlert("Seleccione profesional y fecha.");
            return;
        }

        List<AppointmentDTO> list =
                apiClient.findByProfessionalAndDate(professional, date);

        List<AppointmentViewModel> vmList =
                AppointmentMapper.toViewModelList(list);

        view.setAppointments(vmList);
        view.setTotal(vmList.size());
    }
}

 */

package co.unicauca.frontend.controller;

import co.unicauca.frontend.client.AppointmentHttpClient;
import co.unicauca.frontend.dto.AppointmentDTO;
import co.unicauca.frontend.mapper.AppointmentMapper;
import co.unicauca.frontend.viewmodel.AppointmentViewModel;

import java.time.LocalDate;
import java.util.List;

public class SearchAppointmentController {

    public interface View {

        void setProfessionals(List<String> professionals);

        String getSelectedProfessional();

        LocalDate getSelectedDate();

        String getSearchText();

        void clearAppointments();

        void setAppointments(List<AppointmentViewModel> appointments);

        void setTotal(int total);

        void showAlert(String message);
    }

    private final View view;
    private final AppointmentHttpClient apiClient;

    public SearchAppointmentController(View view) {
        this.view = view;
        this.apiClient = new AppointmentHttpClient();
    }

    public void onInit() {
        try {
            view.setProfessionals(apiClient.getAllProfessionals());
            view.clearAppointments();
            view.setTotal(0);

        } catch (Exception e) {
            view.showAlert("Error cargando profesionales.");
            e.printStackTrace();
        }
    }

    public void onSearch() {

        view.clearAppointments();

        String professional = view.getSelectedProfessional();

        // Si no seleccionó combo, usar texto escrito
        if (professional == null || professional.isBlank()) {
            professional = view.getSearchText();
        }

        LocalDate date = view.getSelectedDate();

        if (professional == null || professional.isBlank() || date == null) {

            view.setTotal(0);

            view.showAlert(
                    "Seleccione o escriba un profesional y una fecha."
            );

            return;
        }

        try {

            List<AppointmentDTO> list =
                    apiClient.findByProfessionalAndDate(
                            professional,
                            date
                    );

            List<AppointmentViewModel> vmList =
                    AppointmentMapper.toViewModelList(list);

            view.setAppointments(vmList);

            view.setTotal(vmList.size());

            if (vmList.isEmpty()) {

                view.showAlert(
                        "No se encontraron citas para la búsqueda."
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            view.showAlert(
                    "Error consultando citas."
            );
        }
    }
}