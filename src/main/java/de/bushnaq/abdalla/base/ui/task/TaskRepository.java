package de.bushnaq.abdalla.base.ui.task;

import de.bushnaq.abdalla.base.ui.sprint.Sprint;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

    // If you don't need a total row count, Slice is better than Page as it only performs a select query.
    // Page performs both a select and a count query.
    Slice<Task> findAllBy(Pageable pageable);

    // Find all root tasks (stories without parents)
    @Query("SELECT t FROM Task t WHERE t.parent IS NULL ORDER BY t.creationDate DESC")
    List<Task> findRootTasks();

    // Find children of a specific task
    @Query("SELECT t FROM Task t WHERE t.parent = :parent ORDER BY t.creationDate DESC")
    List<Task> findByParent(Task parent);

    // Find root tasks (stories) for a specific sprint
    @Query("SELECT t FROM Task t WHERE t.sprint = :sprint AND t.parent IS NULL ORDER BY t.creationDate DESC")
    List<Task> findStoriesBySprint(Sprint sprint);
}
