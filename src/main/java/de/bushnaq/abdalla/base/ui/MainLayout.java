package de.bushnaq.abdalla.base.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.menu.MenuConfiguration;
import com.vaadin.flow.server.menu.MenuEntry;

import java.util.HashMap;
import java.util.Map;

@Layout
public final class MainLayout extends AppLayout implements BeforeEnterObserver {

    private final Map<Tab, String> tabToPathMap = new HashMap<>();
    private       Tabs             tabs;

    MainLayout() {
        addClassName("main-layout"); // scope CSS to this layout

        // Create horizontal header with logo, app name, and tabs
        var header = createHeader();
        addToNavbar(header);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {

        final String pathToMatch = event.getLocation().getPath();
        tabToPathMap.forEach((tab, path) -> {
            if (("/" + pathToMatch).equals(path)) {
                tabs.setSelectedTab(tab);
            }
        });
    }

    private Component createHeader() {
        // Application logo
        var appLogo = VaadinIcon.CUBES.create();
        appLogo.setSize("32px");
        appLogo.getStyle().setMarginRight("12px");

        // Application name
        var appName = new Span("App");
        appName.getStyle().setFontWeight(Style.FontWeight.BOLD);
        appName.getStyle().setFontSize("1.25rem");
        appName.getStyle().setMarginRight("2rem");

        // Create tabs from menu entries
        tabs = createTabs();

        // Horizontal layout for header
        var header = new HorizontalLayout(appLogo, appName, tabs);
        header.setWidthFull();
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setSpacing(true);
        header.setPadding(true);
        header.getStyle().set("box-shadow", "0 1px 3px rgba(0, 0, 0, 0.12)");

        return header;
    }

    private Tab createTab(MenuEntry menuEntry) {
        Tab tab = new Tab();

        if (menuEntry.icon() != null) {
            Icon icon = new Icon(menuEntry.icon());
            icon.getStyle().setMarginRight("8px");

            Span label = new Span(menuEntry.title());

            HorizontalLayout tabLayout = new HorizontalLayout(icon, label);
            tabLayout.setSpacing(false);
            tabLayout.setAlignItems(FlexComponent.Alignment.CENTER);

            tab.add(tabLayout);
        } else {
            tab.add(new Span(menuEntry.title()));
        }

        return tab;
    }

    private Tabs createTabs() {
        var tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.HORIZONTAL);

        // Create tabs from menu configuration
        MenuConfiguration.getMenuEntries().forEach(entry -> {
            Tab tab = createTab(entry);
            tabs.add(tab);
            tabToPathMap.put(tab, entry.path());
        });

        // Handle tab selection changes
        tabs.addSelectedChangeListener(event -> {
            Tab selectedTab = event.getSelectedTab();
            if (selectedTab != null) {
                String path = tabToPathMap.get(selectedTab);
                if (path != null) {
                    getUI().ifPresent(ui -> ui.navigate(path));
                }
            }
        });

        return tabs;
    }

}
