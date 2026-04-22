package co.unicauca.Repository;

import co.unicauca.Entity.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAppointmentRepository extends JpaRepository<Appointment,Long> {
 //   void save(Appointment appointment);
 //   void update(Appointment appointment);
 //   void delete(Appointment appointment);
  //  Appointment findById(long id);

}
