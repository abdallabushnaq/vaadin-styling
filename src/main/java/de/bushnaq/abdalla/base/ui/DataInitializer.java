package de.bushnaq.abdalla.base.ui;

import de.bushnaq.abdalla.base.ui.sprint.Sprint;
import de.bushnaq.abdalla.base.ui.sprint.SprintRepository;
import de.bushnaq.abdalla.base.ui.task.Task;
import de.bushnaq.abdalla.base.ui.task.TaskRepository;
import de.bushnaq.abdalla.base.ui.task.TaskType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final TaskRepository taskRepository;
    private final SprintRepository sprintRepository;

    public DataInitializer(TaskRepository taskRepository, SprintRepository sprintRepository) {
        this.taskRepository = taskRepository;
        this.sprintRepository = sprintRepository;
    }

    @Override
    public void run(String... args) {
        // Only initialize if tables are empty
        if (sprintRepository.count() == 0 && taskRepository.count() == 0) {
            initializeSprintsWithStories();
        }
    }

    private void initializeSprintsWithStories() {
        // Sprint 1: Current Sprint
        var sprint1 = new Sprint("Sprint 23 - Authentication & Security", Instant.now());
        sprint1.setStartDate(LocalDate.now().minusDays(7));
        sprint1.setEndDate(LocalDate.now().plusDays(7));
        sprint1.setGoal("Implement core authentication features and enhance security");
        sprintRepository.save(sprint1);

        var story1_1 = new Task("User authentication system", Instant.now(), TaskType.STORY);
        story1_1.setDueDate(LocalDate.now().plusDays(5));
        story1_1.setSprint(sprint1);

        var task1_1_1 = new Task("Implement login functionality", Instant.now(), TaskType.TASK);
        task1_1_1.setDueDate(LocalDate.now().plusDays(2));
        story1_1.addChild(task1_1_1);

        var task1_1_2 = new Task("Add password reset feature", Instant.now(), TaskType.TASK);
        task1_1_2.setDueDate(LocalDate.now().plusDays(4));
        story1_1.addChild(task1_1_2);

        var task1_1_3 = new Task("Implement OAuth integration", Instant.now(), TaskType.TASK);
        task1_1_3.setDueDate(LocalDate.now().plusDays(5));
        story1_1.addChild(task1_1_3);

        taskRepository.save(story1_1);

        var story1_2 = new Task("Security audit and improvements", Instant.now(), TaskType.STORY);
        story1_2.setDueDate(LocalDate.now().plusDays(6));
        story1_2.setSprint(sprint1);

        var task1_2_1 = new Task("Run security scan", Instant.now(), TaskType.TASK);
        task1_2_1.setDueDate(LocalDate.now().plusDays(1));
        story1_2.addChild(task1_2_1);

        var task1_2_2 = new Task("Fix identified vulnerabilities", Instant.now(), TaskType.TASK);
        task1_2_2.setDueDate(LocalDate.now().plusDays(5));
        story1_2.addChild(task1_2_2);

        taskRepository.save(story1_2);

        // Sprint 2: Next Sprint
        var sprint2 = new Sprint("Sprint 24 - E-commerce Features", Instant.now());
        sprint2.setStartDate(LocalDate.now().plusDays(8));
        sprint2.setEndDate(LocalDate.now().plusDays(21));
        sprint2.setGoal("Build out core shopping cart and payment functionality");
        sprintRepository.save(sprint2);

        var story2_1 = new Task("Shopping cart functionality", Instant.now(), TaskType.STORY);
        story2_1.setDueDate(LocalDate.now().plusDays(14));
        story2_1.setSprint(sprint2);

        var task2_1_1 = new Task("Add items to cart", Instant.now(), TaskType.TASK);
        task2_1_1.setDueDate(LocalDate.now().plusDays(10));
        story2_1.addChild(task2_1_1);

        var task2_1_2 = new Task("Update cart quantities", Instant.now(), TaskType.TASK);
        task2_1_2.setDueDate(LocalDate.now().plusDays(12));
        story2_1.addChild(task2_1_2);

        var task2_1_3 = new Task("Calculate total and discounts", Instant.now(), TaskType.TASK);
        task2_1_3.setDueDate(LocalDate.now().plusDays(13));
        story2_1.addChild(task2_1_3);

        var task2_1_4 = new Task("Save cart for later", Instant.now(), TaskType.TASK);
        task2_1_4.setDueDate(LocalDate.now().plusDays(14));
        story2_1.addChild(task2_1_4);

        taskRepository.save(story2_1);

        var story2_2 = new Task("Payment integration", Instant.now(), TaskType.STORY);
        story2_2.setDueDate(LocalDate.now().plusDays(20));
        story2_2.setSprint(sprint2);

        var task2_2_1 = new Task("Integrate Stripe API", Instant.now(), TaskType.TASK);
        task2_2_1.setDueDate(LocalDate.now().plusDays(16));
        story2_2.addChild(task2_2_1);

        var task2_2_2 = new Task("Handle payment errors", Instant.now(), TaskType.TASK);
        task2_2_2.setDueDate(LocalDate.now().plusDays(18));
        story2_2.addChild(task2_2_2);

        var task2_2_3 = new Task("Add payment confirmation emails", Instant.now(), TaskType.TASK);
        task2_2_3.setDueDate(LocalDate.now().plusDays(20));
        story2_2.addChild(task2_2_3);

        taskRepository.save(story2_2);

        // Sprint 3: Future Sprint
        var sprint3 = new Sprint("Sprint 25 - Admin & Analytics", Instant.now());
        sprint3.setStartDate(LocalDate.now().plusDays(22));
        sprint3.setEndDate(LocalDate.now().plusDays(35));
        sprint3.setGoal("Create comprehensive admin dashboard with analytics");
        sprintRepository.save(sprint3);

        var story3_1 = new Task("Admin dashboard", Instant.now(), TaskType.STORY);
        story3_1.setDueDate(LocalDate.now().plusDays(30));
        story3_1.setSprint(sprint3);

        var task3_1_1 = new Task("Create user management interface", Instant.now(), TaskType.TASK);
        task3_1_1.setDueDate(LocalDate.now().plusDays(24));
        story3_1.addChild(task3_1_1);

        var task3_1_2 = new Task("Add analytics charts", Instant.now(), TaskType.TASK);
        task3_1_2.setDueDate(LocalDate.now().plusDays(27));
        story3_1.addChild(task3_1_2);

        var task3_1_3 = new Task("Implement role-based access", Instant.now(), TaskType.TASK);
        task3_1_3.setDueDate(LocalDate.now().plusDays(29));
        story3_1.addChild(task3_1_3);

        var task3_1_4 = new Task("Add audit logging", Instant.now(), TaskType.TASK);
        task3_1_4.setDueDate(LocalDate.now().plusDays(31));
        story3_1.addChild(task3_1_4);

        var task3_1_5 = new Task("Create export functionality", Instant.now(), TaskType.TASK);
        task3_1_5.setDueDate(LocalDate.now().plusDays(33));
        story3_1.addChild(task3_1_5);

        taskRepository.save(story3_1);

        var story3_2 = new Task("Email notification service", Instant.now(), TaskType.STORY);
        story3_2.setDueDate(LocalDate.now().plusDays(28));
        story3_2.setSprint(sprint3);

        var task3_2_1 = new Task("Set up email templates", Instant.now(), TaskType.TASK);
        task3_2_1.setDueDate(LocalDate.now().plusDays(24));
        story3_2.addChild(task3_2_1);

        var task3_2_2 = new Task("Implement email queue", Instant.now(), TaskType.TASK);
        task3_2_2.setDueDate(LocalDate.now().plusDays(27));
        story3_2.addChild(task3_2_2);

        taskRepository.save(story3_2);

        var story3_3 = new Task("Performance optimization", Instant.now(), TaskType.STORY);
        story3_3.setDueDate(LocalDate.now().plusDays(35));
        story3_3.setSprint(sprint3);

        var task3_3_1 = new Task("Optimize database queries", Instant.now(), TaskType.TASK);
        task3_3_1.setDueDate(LocalDate.now().plusDays(30));
        story3_3.addChild(task3_3_1);

        var task3_3_2 = new Task("Implement caching strategy", Instant.now(), TaskType.TASK);
        task3_3_2.setDueDate(LocalDate.now().plusDays(33));
        story3_3.addChild(task3_3_2);

        var task3_3_3 = new Task("Add performance monitoring", Instant.now(), TaskType.TASK);
        task3_3_3.setDueDate(LocalDate.now().plusDays(35));
        story3_3.addChild(task3_3_3);

        taskRepository.save(story3_3);

        System.out.println("✅ Initialized Sprint structure:");
        System.out.println("   - 3 Sprints");
        System.out.println("   - 7 Stories");
        System.out.println("   - 20 Tasks");
    }
}

