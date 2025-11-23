package de.bushnaq.abdalla.ui;

import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.bushnaq.abdalla.base.ui.sprint.Sprint;
import de.bushnaq.abdalla.base.ui.sprint.SprintService;
import de.bushnaq.abdalla.base.ui.task.Task;
import de.bushnaq.abdalla.base.ui.task.TaskService;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Optional;

@Route("")
@PageTitle("Sprints")
@Menu(order = 0, icon = "vaadin:calendar", title = "Sprints")
@Slf4j
class SprintsView extends VerticalLayout {

    private final SprintService sprintService;
    private final TaskService   taskService;

    SprintsView(SprintService sprintService, TaskService taskService) {
        this.sprintService = sprintService;
        this.taskService   = taskService;

        setWidthFull();
        setPadding(false);
        setSpacing(false);
        addClassName("sprints-view");

        // Load all sprints and create a grid for each
        var sprints = sprintService.getAllSprints();

        for (Sprint sprint : sprints) {
            add(createSprintSection(sprint));
        }

        if (sprints.isEmpty()) {
            var emptyMessage = new Div();
            emptyMessage.setText("No sprints available");
            emptyMessage.getStyle().set("padding", "24px").set("text-align", "center").set("color", "var(--lumo-secondary-text-color)");
            add(emptyMessage);
        }
    }

    private TreeGrid<Task> createSprintGrid(Sprint sprint) {
        var dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withZone(ZoneId.systemDefault());
        var dateFormatter     = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
        var grid              = new TreeGrid<Task>();
        grid.addClassName("sprint-tree-grid");
        // Set up hierarchical data provider for this sprint
        grid.setItems(taskService.getStoriesBySprint(sprint), taskService::getChildTasks);

        // Add columns
        grid.addColumn(Task::getTaskType).setHeader("Type").setWidth("100px").setFlexGrow(0);
        grid.addHierarchyColumn(Task::getDescription).setHeader("Description");
        grid.addColumn(task -> Optional.ofNullable(task.getDueDate()).map(dateFormatter::format).orElse("Never")).setHeader("Due Date").setWidth("150px").setFlexGrow(0);
        grid.addColumn(task -> dateTimeFormatter.format(task.getCreationDate())).setHeader("Creation Date").setWidth("200px").setFlexGrow(0);

        grid.setEmptyStateText("No stories in this sprint");
        grid.setWidthFull();
        grid.setAllRowsVisible(true);
        grid.addThemeVariants(GridVariant.LUMO_COMPACT);

        return grid;
    }

    private VerticalLayout createSprintSection(Sprint sprint) {
        var section = new VerticalLayout();
        section.setPadding(false);
        section.setSpacing(false);
        section.setWidthFull();
        section.addClassName("sprint-grid-panel-wrapper");

        var innerWrapper = new VerticalLayout();
        innerWrapper.setPadding(false);
        innerWrapper.setSpacing(false);
        section.add(innerWrapper);

        // Sprint header
        var header = new Div();
        header.addClassName("sprint-header");
//        header.setWidthFull();

        var title = new H2(sprint.getName());
        title.getStyle().set("margin", "0");
        header.add(title);

        var dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);

        if (sprint.getStartDate() != null && sprint.getEndDate() != null) {
            var dates = new Span(dateFormatter.format(sprint.getStartDate()) + " - " + dateFormatter.format(sprint.getEndDate()));
            dates.addClassName("sprint-dates");
            header.add(dates);
        }

        if (sprint.getGoal() != null && !sprint.getGoal().isEmpty()) {
            var goal = new Span("Goal: " + sprint.getGoal());
            goal.addClassName("sprint-goal");
            header.add(goal);
        }

        innerWrapper.add(header);

        // Create TreeGrid for this sprint's stories and tasks
        var grid = createSprintGrid(sprint);

        var gridPanel = new VerticalLayout(grid);
        gridPanel.setPadding(false);
        gridPanel.setSpacing(false);
        gridPanel.setWidthFull();
        gridPanel.addClassName("sprint-grid-panel");

        innerWrapper.add(gridPanel);

        return section;
    }
}

