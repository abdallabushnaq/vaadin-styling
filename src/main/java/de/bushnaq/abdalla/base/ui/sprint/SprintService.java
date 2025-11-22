package de.bushnaq.abdalla.base.ui.sprint;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
public class SprintService {

    private final SprintRepository sprintRepository;

    SprintService(SprintRepository sprintRepository) {
        this.sprintRepository = sprintRepository;
    }

    @Transactional
    public Sprint createSprint(String name, @Nullable LocalDate startDate, @Nullable LocalDate endDate, @Nullable String goal) {
        var sprint = new Sprint(name, Instant.now());
        sprint.setStartDate(startDate);
        sprint.setEndDate(endDate);
        sprint.setGoal(goal);
        return sprintRepository.saveAndFlush(sprint);
    }

    @Transactional(readOnly = true)
    public List<Sprint> getAllSprints() {
        return sprintRepository.findAllOrderByStartDate();
    }

    @Transactional(readOnly = true)
    public Sprint getSprint(Long id) {
        return sprintRepository.findById(id).orElseThrow();
    }
}

