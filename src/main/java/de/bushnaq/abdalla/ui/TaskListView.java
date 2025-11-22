package de.bushnaq.abdalla.ui;

import de.bushnaq.abdalla.base.ui.component.ViewToolbar;
import de.bushnaq.abdalla.base.ui.task.Task;
import de.bushnaq.abdalla.base.ui.task.TaskService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dnd.GridDropLocation;
import com.vaadin.flow.component.grid.dnd.GridDropMode;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Optional;

import static com.vaadin.flow.spring.data.VaadinSpringDataHelpers.toSpringPageRequest;

@Route("")
@PageTitle("Task List")
@Menu(order = 0, icon = "vaadin:clipboard-check", title = "Task List")
@Slf4j
class TaskListView extends VerticalLayout {

    final         Button      createBtn;
    final         TextField   description;
    private       Task        draggedTask;
    final         DatePicker  dueDate;
    final         Grid<Task>  taskGrid;
    private final TaskService taskService;

    TaskListView(TaskService taskService) {
        this.taskService = taskService;

        description = new TextField();
        description.setPlaceholder("What do you want to do?");
        description.setAriaLabel("Task description");
        description.setMaxLength(Task.DESCRIPTION_MAX_LENGTH);
        description.setMinWidth("20em");

        dueDate = new DatePicker();
        dueDate.setPlaceholder("Due date");
        dueDate.setAriaLabel("Due date");

        createBtn = new Button("Create", event -> createTask());
        createBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        var dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(getLocale())
                .withZone(ZoneId.systemDefault());
        var dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(getLocale());

        taskGrid = new Grid<>();
        taskGrid.addClassName("jira-backlog-grid");
        taskGrid.setItems(query -> taskService.list(toSpringPageRequest(query)).stream());
        taskGrid.addColumn(Task::getDescription).setHeader("Description");
        taskGrid.addColumn(task -> Optional.ofNullable(task.getDueDate()).map(dateFormatter::format).orElse("Never"))                .setHeader("Due Date");
        taskGrid.addColumn(task -> dateTimeFormatter.format(task.getCreationDate())).setHeader("Creation Date");
        taskGrid.setEmptyStateText("You have no tasks to complete");
        taskGrid.setWidthFull();
        taskGrid.setAllRowsVisible(true);
        taskGrid.addThemeVariants(GridVariant.LUMO_COMPACT);
        taskGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        taskGrid.setRowsDraggable(true);
        taskGrid.setDropMode(GridDropMode.ON_TOP);
        taskGrid.setDragFilter(task -> task.getDescription().length() > 3);
        taskGrid.setDropFilter(task -> task.getDueDate() != null);
        taskGrid.addDragStartListener(event -> {
            draggedTask = event.getDraggedItems().stream().findFirst().orElse(null);
        });
        taskGrid.addDragEndListener(event -> {
            draggedTask = null;
        });
        taskGrid.addDropListener(event -> {
            var targetTask = event.getDropTargetItem();

            if (draggedTask != null && targetTask.isPresent() && event.getDropLocation() == GridDropLocation.ON_TOP) {
                Notification.show(
                        "Dropped '" + draggedTask.getDescription() + "' on top of '" + targetTask.get().getDescription() + "'",
                        3000,
                        Notification.Position.BOTTOM_END
                ).addThemeVariants(NotificationVariant.LUMO_CONTRAST);
            }
        });

        setWidthFull();
        addClassName("grid-panel-container");

        // Create a panel to wrap the grid
        var gridPanel = new VerticalLayout(taskGrid);
        gridPanel.setWidthFull();
        gridPanel.addClassName("grid-panel");

        add(new ViewToolbar("Task List", ViewToolbar.group(description, dueDate, createBtn)));
        add(gridPanel);
    }

    private void createTask() {
        taskService.createTask(description.getValue(), dueDate.getValue());
        taskGrid.getDataProvider().refreshAll();
        description.clear();
        dueDate.clear();
        Notification.show("Task added", 3000, Notification.Position.BOTTOM_END)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

}
