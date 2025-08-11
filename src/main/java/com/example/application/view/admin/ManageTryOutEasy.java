package com.example.application.view.admin;

import com.example.application.components.Header;
import com.example.application.components.SidebarAdmin;
import com.example.application.dao.MateriDAO;
import com.example.application.dao.TryOutEasyDAO;
import com.example.application.model.TryOutEasyModel;
import com.example.application.model.UsersModel;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.util.ArrayList;
import java.util.List;

import static com.vaadin.flow.component.notification.Notification.Position.MIDDLE;

@Route("try-out-easy")
@PageTitle("TryOut Easy - Admin")
public class ManageTryOutEasy extends VerticalLayout implements BeforeEnterObserver {

    private final TryOutEasyDAO tryOutEasyDAO = new TryOutEasyDAO();
    private final MateriDAO materiDAO = new MateriDAO();

    private Grid<TryOutEasyModel> soalGrid;
    private Integer selectedMapel;
    private List<TryOutEasyModel> currentSoalList = new ArrayList<>();

    public ManageTryOutEasy() {}

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        UsersModel user = VaadinSession.getCurrent().getAttribute(UsersModel.class);
        if (user == null) {
            event.forwardTo("login");
            Notification.show("Kamu Harus Login Terlebih dahulu!", 2000, MIDDLE);
        } else if (user.getRole().equalsIgnoreCase("user")) {
            event.forwardTo("dashboard");
            Notification.show("Kamu Bukan Admin!", 2000, MIDDLE);
        } else {
            buildLayout();
        }
    }

    private void buildLayout() {
        setPadding(false);
        setMargin(false);
        setSpacing(false);
        setSizeFull();

        HorizontalLayout header = Header.headerWithLogo();

        HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.setSizeFull();
        mainLayout.setPadding(false);
        mainLayout.setMargin(false);
        mainLayout.setSpacing(false);

        VerticalLayout sidebar = SidebarAdmin.createSidebar();
        VerticalLayout mainContent = createMainContent();

        mainLayout.add(sidebar, mainContent);
        mainLayout.setFlexGrow(0, sidebar);
        mainLayout.setFlexGrow(1, mainContent);

        add(header, mainLayout);
        setFlexGrow(0, header);
        setFlexGrow(1, mainLayout);
    }

    private VerticalLayout createMainContent() {
        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.setPadding(true);
        content.setSpacing(true);
        content.getStyle()
                .set("background-image", "url('images/background.jpg')")
                .set("background-size", "cover")
                .set("background-position", "center")
                .set("background-repeat", "no-repeat");

        H2 title = new H2("TryOut Easy Management");
        title.addClassName("lora-text");
        title.getStyle().setColor("#118B50");

        content.add(title);
        return content;
    }
}