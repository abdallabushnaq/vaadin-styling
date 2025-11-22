package de.bushnaq.abdalla.base.ui;

import de.bushnaq.abdalla.base.ui.story.Story;
import de.bushnaq.abdalla.base.ui.story.StoryRepository;
import de.bushnaq.abdalla.base.ui.task.Task;
import de.bushnaq.abdalla.base.ui.task.TaskRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final TaskRepository  taskRepository;
    private final StoryRepository storyRepository;

    public DataInitializer(TaskRepository taskRepository, StoryRepository storyRepository) {
        this.taskRepository = taskRepository;
        this.storyRepository = storyRepository;
    }

    @Override
    public void run(String... args) {
        // Only initialize if tables are empty
        if (taskRepository.count() == 0) {
            initializeTasks();
        }

        if (storyRepository.count() == 0) {
            initializeStories();
        }
    }

    private void initializeTasks() {
        // Create tasks with various properties to test drag and drop filters

        // Tasks with due dates (can be drop targets)
        var task1 = new Task("Review pull requests", Instant.now());
        task1.setDueDate(LocalDate.now().plusDays(2));
        taskRepository.save(task1);

        var task2 = new Task("Update documentation", Instant.now());
        task2.setDueDate(LocalDate.now().plusDays(5));
        taskRepository.save(task2);

        var task3 = new Task("Fix critical bug in authentication", Instant.now());
        task3.setDueDate(LocalDate.now().plusDays(1));
        taskRepository.save(task3);

        var task4 = new Task("Implement new feature", Instant.now());
        task4.setDueDate(LocalDate.now().plusDays(7));
        taskRepository.save(task4);

        var task5 = new Task("Write unit tests", Instant.now());
        task5.setDueDate(LocalDate.now().plusDays(3));
        taskRepository.save(task5);

        // Tasks without due dates (cannot be drop targets, but can be dragged)
        var task6 = new Task("Refactor legacy code", Instant.now());
        taskRepository.save(task6);

        var task7 = new Task("Research new frameworks", Instant.now());
        taskRepository.save(task7);

        // Short description task (cannot be dragged - description <= 3 chars)
        var task8 = new Task("Fix", Instant.now());
        task8.setDueDate(LocalDate.now().plusDays(1));
        taskRepository.save(task8);

        var task9 = new Task("Deploy to production", Instant.now());
        task9.setDueDate(LocalDate.now().plusDays(10));
        taskRepository.save(task9);

        var task10 = new Task("Code review meeting", Instant.now());
        task10.setDueDate(LocalDate.now().plusDays(4));
        taskRepository.save(task10);

        System.out.println("✅ Initialized 10 sample tasks");
    }

    private void initializeStories() {
        // Create stories with various properties to test drag and drop filters

        // Stories with both story points and target dates (can be dragged and dropped)
        var story1 = new Story("User authentication system", Instant.now());
        story1.setStoryPoints(8);
        story1.setTargetDate(LocalDate.now().plusWeeks(2));
        storyRepository.save(story1);

        var story2 = new Story("Shopping cart functionality", Instant.now());
        story2.setStoryPoints(13);
        story2.setTargetDate(LocalDate.now().plusWeeks(3));
        storyRepository.save(story2);

        var story3 = new Story("Payment integration", Instant.now());
        story3.setStoryPoints(5);
        story3.setTargetDate(LocalDate.now().plusWeeks(1));
        storyRepository.save(story3);

        var story4 = new Story("Admin dashboard", Instant.now());
        story4.setStoryPoints(21);
        story4.setTargetDate(LocalDate.now().plusWeeks(4));
        storyRepository.save(story4);

        var story5 = new Story("Email notification service", Instant.now());
        story5.setStoryPoints(3);
        story5.setTargetDate(LocalDate.now().plusWeeks(1));
        storyRepository.save(story5);

        // Stories with target dates but no story points (cannot be dragged, but can be drop targets)
        var story6 = new Story("Performance optimization", Instant.now());
        story6.setTargetDate(LocalDate.now().plusWeeks(5));
        storyRepository.save(story6);

        var story7 = new Story("Mobile app development", Instant.now());
        story7.setTargetDate(LocalDate.now().plusWeeks(6));
        storyRepository.save(story7);

        // Stories with story points but no target dates (can be dragged but cannot be drop targets)
        var story8 = new Story("API documentation", Instant.now());
        story8.setStoryPoints(5);
        storyRepository.save(story8);

        var story9 = new Story("Database migration", Instant.now());
        story9.setStoryPoints(8);
        storyRepository.save(story9);

        // Story with neither (cannot be dragged or dropped)
        var story10 = new Story("Research new technologies", Instant.now());
        storyRepository.save(story10);

        System.out.println("✅ Initialized 10 sample stories");
    }
}

