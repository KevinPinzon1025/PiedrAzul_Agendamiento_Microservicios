package co.unicauca.frontend.util;

import java.util.ArrayList;
import java.util.List;

public class AppointmentSubject {
    private static final List<IAppointmentObserver> observers =
            new ArrayList<>();

    private AppointmentSubject() {
    }

    public static void addObserver(
            IAppointmentObserver observer
    ) {

        observers.add(observer);
    }

    public static void removeObserver(
            IAppointmentObserver observer
    ) {

        observers.remove(observer);
    }

    public static void notifyObservers() {

        for (IAppointmentObserver observer : observers) {

            observer.onAppointmentsChanged();
        }
    }
}
