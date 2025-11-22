package de.bushnaq.abdalla.base.ui.story;

import jakarta.persistence.*;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "story")
public class Story {

    public static final int TITLE_MAX_LENGTH = 300;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "story_id")
    private Long id;

    @Column(name = "title", nullable = false, length = TITLE_MAX_LENGTH)
    private String title = "";

    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @Column(name = "target_date")
    @Nullable
    private LocalDate targetDate;

    @Column(name = "story_points")
    @Nullable
    private Integer storyPoints;

    protected Story() { // To keep Hibernate happy
    }

    public Story(String title, Instant creationDate) {
        setTitle(title);
        this.creationDate = creationDate;
    }

    public @Nullable Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title.length() > TITLE_MAX_LENGTH) {
            throw new IllegalArgumentException("Title length exceeds " + TITLE_MAX_LENGTH);
        }
        this.title = title;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public @Nullable LocalDate getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(@Nullable LocalDate targetDate) {
        this.targetDate = targetDate;
    }

    public @Nullable Integer getStoryPoints() {
        return storyPoints;
    }

    public void setStoryPoints(@Nullable Integer storyPoints) {
        this.storyPoints = storyPoints;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        Story other = (Story) obj;
        return getId() != null && getId().equals(other.getId());
    }

    @Override
    public int hashCode() {
        // Hashcode should never change during the lifetime of an object. Because of
        // this we can't use getId() to calculate the hashcode. Unless you have sets
        // with lots of entities in them, returning the same hashcode should not be a
        // problem.
        return getClass().hashCode();
    }
}

