package co.unicauca.Repository;

import co.unicauca.Entity.model.TypeProfessional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITypeProfessionalRepository extends JpaRepository<TypeProfessional, Long> {
}
