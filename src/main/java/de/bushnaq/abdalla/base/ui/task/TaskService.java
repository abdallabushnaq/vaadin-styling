package de.bushnaq.abdalla.base.ui.task;

import de.bushnaq.abdalla.base.ui.sprint.Sprint;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional
    public void createTask(String description, @Nullable LocalDate dueDate) {
        var task = new Task(description, Instant.now(), TaskType.TASK);
        task.setDueDate(dueDate);
        taskRepository.saveAndFlush(task);
    }

    @Transactional
    public void createStory(String description, @Nullable LocalDate dueDate) {
        var story = new Task(description, Instant.now(), TaskType.STORY);
        story.setDueDate(dueDate);
        taskRepository.saveAndFlush(story);
    }

    @Transactional
    public void createTaskUnderStory(String description, @Nullable LocalDate dueDate, Task parent) {
        var task = new Task(description, Instant.now(), TaskType.TASK);
        task.setDueDate(dueDate);
        parent.addChild(task);
        taskRepository.saveAndFlush(parent);
    }

    @Transactional(readOnly = true)
    public List<Task> list(Pageable pageable) {
        return taskRepository.findAllBy(pageable).toList();
    }

    @Transactional(readOnly = true)
    public List<Task> getRootTasks() {
        return taskRepository.findRootTasks();
    }

    @Transactional(readOnly = true)
    public List<Task> getChildTasks(Task parent) {
        return taskRepository.findByParent(parent);
    }

    @Transactional(readOnly = true)
    public List<Task> getStoriesBySprint(Sprint sprint) {
        return taskRepository.findStoriesBySprint(sprint);
    }

}
