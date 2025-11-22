package de.bushnaq.abdalla.base.ui.story;

import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
public class StoryService {

    private final StoryRepository storyRepository;

    StoryService(StoryRepository storyRepository) {
        this.storyRepository = storyRepository;
    }

    @Transactional
    public void createStory(String title, @Nullable LocalDate targetDate, @Nullable Integer storyPoints) {
        var story = new Story(title, Instant.now());
        story.setTargetDate(targetDate);
        story.setStoryPoints(storyPoints);
        storyRepository.saveAndFlush(story);
    }

    @Transactional(readOnly = true)
    public List<Story> list(Pageable pageable) {
        return storyRepository.findAllBy(pageable).toList();
    }

}

