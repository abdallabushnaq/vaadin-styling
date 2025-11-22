package de.bushnaq.abdalla.base.ui.sprint;

import de.bushnaq.abdalla.base.ui.task.Task;
import jakarta.persistence.*;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sprint")
public class Sprint {

    public static final int NAME_MAX_LENGTH = 200;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "sprint_id")
    private Long id;

    @Column(name = "name", nullable = false, length = NAME_MAX_LENGTH)
    private String name = "";

    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @Column(name = "start_date")
    @Nullable
    private LocalDate startDate;

    @Column(name = "end_date")
    @Nullable
    private LocalDate endDate;

    @Column(name = "goal", length = 500)
    @Nullable
    private String goal;

    @OneToMany(mappedBy = "sprint", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Task> stories = new ArrayList<>();

    protected Sprint() { // To keep Hibernate happy
    }

    public Sprint(String name, Instant creationDate) {
        setName(name);
        this.creationDate = creationDate;
    }

    public @Nullable Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name.length() > NAME_MAX_LENGTH) {
            throw new IllegalArgumentException("Name length exceeds " + NAME_MAX_LENGTH);
        }
        this.name = name;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public @Nullable LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(@Nullable LocalDate startDate) {
        this.startDate = startDate;
    }

    public @Nullable LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(@Nullable LocalDate endDate) {
        this.endDate = endDate;
    }

    public @Nullable String getGoal() {
        return goal;
    }

    public void setGoal(@Nullable String goal) {
        this.goal = goal;
    }

    public List<Task> getStories() {
        return stories;
    }

    public void addStory(Task story) {
        stories.add(story);
        story.setSprint(this);
    }

    public void removeStory(Task story) {
        stories.remove(story);
        story.setSprint(null);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        Sprint other = (Sprint) obj;
        return getId() != null && getId().equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

