package co.unicauca.Repository;

import co.unicauca.Entity.model.Scheduler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISchedulerRepository extends JpaRepository<Scheduler,Long> {
}
