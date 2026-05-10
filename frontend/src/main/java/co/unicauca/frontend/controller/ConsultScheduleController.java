package co.unicauca.frontend.controller;

import co.unicauca.frontend.client.AppointmentHttpClient;
import co.unicauca.frontend.dto.AppointmentDTO;
import co.unicauca.frontend.dto.ProfessionalDTO;
import co.unicauca.frontend.dto.SlotResponseDTO;
import co.unicauca.frontend.mapper.AppointmentMapper;
import co.unicauca.frontend.util.AppointmentSubject;
import co.unicauca.frontend.util.IAppointmentObserver;
import co.unicauca.frontend.viewmodel.AppointmentViewModel;
import javafx.application.Platform;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ConsultScheduleController
        implements IAppointmentObserver {

    public interface View {

        void setAvailableSlots(
                List<SlotResponseDTO> slots
        );

        void setAppointments(
                List<AppointmentDTO> appointments
        );
    }

    private final View view;

    private final AppointmentHttpClient httpClient;

    private String currentProfessional;

    private LocalDate currentDate;

    private boolean showingAvailableTab = true;

    public ConsultScheduleController(
            View view,
            String professional,
            LocalDate date
    ) {

        this.view = view;

        this.currentProfessional = professional;

        this.currentDate = date;

        this.httpClient =
                new AppointmentHttpClient();
    }


    public void onInit() {

        AppointmentSubject.addObserver(this);

        refresh();
    }

    public void onClose() {

        AppointmentSubject.removeObserver(this);
    }

    @Override
    public void onAppointmentsChanged() {

        Platform.runLater(this::refresh);
    }

    public void onShowAvailableTab() {

        showingAvailableTab = true;

        refresh();
    }

    public void onShowAppointmentsTab() {

        showingAvailableTab = false;

        refresh();
    }

    public void updateFilters(
            String professional,
            LocalDate date
    ) {

        this.currentProfessional = professional;

        this.currentDate = date;

        refresh();
    }

    public String getCurrentProfessional() {

        return currentProfessional;
    }


    private void refresh() {

        try {

            if (currentProfessional == null ||
                    currentDate == null) {

                return;
            }

            Long professionalId =
                    httpClient.getProfessionalIdByName(
                            currentProfessional
                    );

            // =========================
            // HORARIOS DISPONIBLES
            // =========================
            List<String> slots =
                    httpClient.getAvailableSlots(
                            professionalId,
                            currentDate
                    );

            List<SlotResponseDTO> availableSlots =
                    new ArrayList<>();

            for (String slot : slots) {

                SlotResponseDTO dto =
                        new SlotResponseDTO();

                dto.setStartTime(slot);

                dto.setEndTime("");

                dto.setAvailable(true);

                availableSlots.add(dto);
            }

            view.setAvailableSlots(
                    availableSlots
            );

            // =========================
            // CITAS DEL PROFESIONAL
            // =========================



            List<AppointmentDTO> appointments =
                    httpClient.findByProfessionalAndDate(
                            currentProfessional,
                            currentDate
                    );

            view.setAppointments(
                    appointments
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}