package de.bushnaq.abdalla.base.ui.sprint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SprintRepository extends JpaRepository<Sprint, Long>, JpaSpecificationExecutor<Sprint> {

    @Query("SELECT s FROM Sprint s ORDER BY s.startDate DESC, s.creationDate DESC")
    List<Sprint> findAllOrderByStartDate();
}

