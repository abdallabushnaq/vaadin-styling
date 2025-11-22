package de.bushnaq.abdalla.ui;

import de.bushnaq.abdalla.base.ui.component.ViewToolbar;
import de.bushnaq.abdalla.base.ui.story.Story;
import de.bushnaq.abdalla.base.ui.story.StoryService;
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
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Optional;

import static com.vaadin.flow.spring.data.VaadinSpringDataHelpers.toSpringPageRequest;

@Route("stories")
@PageTitle("Story List")
@Menu(order = 1, icon = "vaadin:book", title = "Story List")
@Slf4j
class StoryListView extends VerticalLayout {

    final         Button       createBtn;
    private       Story        draggedStory;
    final         Grid<Story>  storyGrid;
    final         IntegerField storyPoints;
    private final StoryService storyService;
    final         DatePicker   targetDate;
    final         TextField    title;

    StoryListView(StoryService storyService) {
        this.storyService = storyService;

        title = new TextField();
        title.setPlaceholder("Story title");
        title.setAriaLabel("Story title");
        title.setMaxLength(Story.TITLE_MAX_LENGTH);
        title.setMinWidth("20em");

        targetDate = new DatePicker();
        targetDate.setPlaceholder("Target date");
        targetDate.setAriaLabel("Target date");

        storyPoints = new IntegerField();
        storyPoints.setPlaceholder("Points");
        storyPoints.setAriaLabel("Story points");
        storyPoints.setMin(0);
        storyPoints.setMax(100);
        storyPoints.setWidth("8em");

        createBtn = new Button("Create", event -> createStory());
        createBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        var dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(getLocale())
                .withZone(ZoneId.systemDefault());
        var dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(getLocale());

        storyGrid = new Grid<>();
        storyGrid.addClassName("jira-backlog-grid");
        storyGrid.setItems(query -> storyService.list(toSpringPageRequest(query)).stream());
        storyGrid.addColumn(Story::getTitle).setHeader("Title");
        storyGrid.addColumn(story -> Optional.ofNullable(story.getStoryPoints()).map(String::valueOf).orElse("-"))
                .setHeader("Story Points");
        storyGrid.addColumn(story -> Optional.ofNullable(story.getTargetDate()).map(dateFormatter::format).orElse("Not set"))
                .setHeader("Target Date");
        storyGrid.addColumn(story -> dateTimeFormatter.format(story.getCreationDate())).setHeader("Creation Date");
        storyGrid.setEmptyStateText("You have no stories yet");
        storyGrid.setSizeFull();
        storyGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        storyGrid.addThemeVariants(GridVariant.LUMO_COMPACT);
        storyGrid.setSelectionMode(Grid.SelectionMode.SINGLE);

        // Configure drag and drop with BETWEEN mode
        storyGrid.setRowsDraggable(true);

        storyGrid.setDragFilter(story -> {
            return story.getStoryPoints() != null && story.getStoryPoints() > 0;
        });
        storyGrid.setDropFilter(story -> {
            log.info("DropFilter check for story '{}': targetDate={} return {}", story.getTitle(), story.getTargetDate(), story.getTargetDate() != null);
            return story.getTargetDate() != null;
        });
        storyGrid.addDragStartListener(event -> {
            log.info("DragStartListener invoked");
            storyGrid.setDropMode(GridDropMode.BETWEEN);
            draggedStory = event.getDraggedItems().stream().findFirst().orElse(null);
        });
        storyGrid.addDragEndListener(event -> {
            draggedStory = null;
            storyGrid.setDropMode(null);
        });
        storyGrid.addDropListener(event -> {
            var targetStory = event.getDropTargetItem();

            if (draggedStory != null && targetStory.isPresent()) {
                String locationText = event.getDropLocation() == GridDropLocation.BELOW ? "below" : "above";
                Notification.show(
                        "Dropped '" + draggedStory.getTitle() + "' " + locationText + " '" + targetStory.get().getTitle() + "'",
                        3000,
                        Notification.Position.BOTTOM_END
                ).addThemeVariants(NotificationVariant.LUMO_CONTRAST);
            }
        });

        storyGrid.setWidthFull();
        storyGrid.setAllRowsVisible(true);
        getStyle().setOverflow(Style.Overflow.HIDDEN);

        add(new ViewToolbar("Story List", ViewToolbar.group(title, targetDate, storyPoints, createBtn)));

        addClassName("grid-panel-container");
        var gridPanel = new VerticalLayout(storyGrid);
        gridPanel.setWidthFull();
        gridPanel.addClassName("grid-panel");

        add(gridPanel);
    }

    private void createStory() {
        storyService.createStory(title.getValue(), targetDate.getValue(), storyPoints.getValue());
        storyGrid.getDataProvider().refreshAll();
        title.clear();
        targetDate.clear();
        storyPoints.clear();
        Notification.show("Story added", 3000, Notification.Position.BOTTOM_END)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

}

