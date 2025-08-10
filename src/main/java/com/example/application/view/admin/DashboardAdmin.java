package com.example.application.view.admin;

import com.example.application.components.Header;
import com.example.application.components.SidebarAdmin;
import com.example.application.dao.UsersDAO;
import com.example.application.model.UsersModel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.sql.SQLException;
import java.util.List;

import static com.vaadin.flow.component.notification.Notification.Position.TOP_CENTER;

@Route("users")
@PageTitle("Dashboard - Admin")
public class DashboardAdmin extends VerticalLayout implements BeforeEnterObserver {

    private UsersDAO usersDAO;
    private VerticalLayout pendingUsersContainer;
    private VerticalLayout acceptedUsersContainer;

    public DashboardAdmin() {
        this.usersDAO = new UsersDAO();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        UsersModel user = VaadinSession.getCurrent().getAttribute(UsersModel.class);
        if (user == null) {
            event.forwardTo("login");
            Notification.show("Kamu Harus Login Terlebih dahulu!", 2000, TOP_CENTER);
        } else if (user.getRole().equalsIgnoreCase("user")) {
            event.forwardTo("dashboard");
            Notification.show("Kamu Bukan Admin!", 2000, TOP_CENTER);
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

        H2 title = new H2("User Management");
        title.addClassName("lora-text");
        title.getStyle()
                .setColor("#118B50");
        content.add(title);

        HorizontalLayout listAllUsers = new HorizontalLayout();
        listAllUsers.setWidthFull();
        listAllUsers.setJustifyContentMode(JustifyContentMode.BETWEEN);

        listAllUsers.add(createListUserNotAcc(), createListUserAcc());

        content.add(listAllUsers);

        return content;
    }

    private VerticalLayout createListUserNotAcc() {
        VerticalLayout container = new VerticalLayout();
        container.setWidthFull();
        container.setHeight("480px");
        container.setPadding(true);
        container.setSpacing(true);
        container.getStyle()
                .setOverflow(Style.Overflow.AUTO)
                .setBackgroundColor("white")
                .setBoxShadow("0 4px 12px rgba(0,0,0,0.2)")
                .set("border-radius", "20px");

        H3 sectionTitle = new H3("Users - Tidak Diterima");
        sectionTitle.addClassName("lora-text");
        sectionTitle.getStyle()
                .setColor("#d32f2f")
                .setMarginTop("0")
                .setMarginBottom("0");
        container.add(sectionTitle);

        pendingUsersContainer = new VerticalLayout();
        pendingUsersContainer.setWidthFull();
        pendingUsersContainer.setPadding(false);
        pendingUsersContainer.setSpacing(true);

        loadPendingUsers();

        Scroller scroller = new Scroller(pendingUsersContainer);
        scroller.setHeight("380px");
        scroller.setWidthFull();

        container.add(scroller);
        return container;
    }

    private VerticalLayout createListUserAcc() {
        VerticalLayout container = new VerticalLayout();
        container.setWidthFull();
        container.setHeight("480px");
        container.setPadding(true);
        container.setSpacing(true);
        container.getStyle()
                .setOverflow(Style.Overflow.AUTO)
                .setBackgroundColor("white")
                .setBoxShadow("0 4px 12px rgba(0,0,0,0.2)")
                .set("border-radius", "20px");

        H3 sectionTitle = new H3("Users - Tidak Diterima");
        sectionTitle.addClassName("lora-text");
        sectionTitle.getStyle()
                .setColor("#2e7d32")
                .setMarginTop("0")
                .setMarginBottom("0");
        container.add(sectionTitle);

        acceptedUsersContainer = new VerticalLayout();
        acceptedUsersContainer.setWidthFull();
        acceptedUsersContainer.setPadding(false);
        acceptedUsersContainer.setSpacing(true);

        loadAcceptedUsers();

        Scroller scroller = new Scroller(acceptedUsersContainer);
        scroller.setHeight("380px");
        scroller.setWidthFull();

        container.add(scroller);
        return container;
    }

    private void loadPendingUsers() {
        try {
            List<UsersModel> pendingUsers = usersDAO.getUsersByRoleAndStatus("user", "Tidak Diterima");
            pendingUsersContainer.removeAll();

            if (pendingUsers.isEmpty()) {
                Span noUsersMessage = new Span("Tidak ada user yang menunggu persetujuan.");
                noUsersMessage.getStyle().set("font-style", "italic").set("color", "#666");
                pendingUsersContainer.add(noUsersMessage);
            } else {
                for (UsersModel user : pendingUsers) {
                    pendingUsersContainer.add(createUserCard(user, false));
                }
            }
        } catch (SQLException e) {
            Notification.show("Error loading pending users: " + e.getMessage(), 3000, TOP_CENTER);
        }
    }

    private void loadAcceptedUsers() {
        try {
            List<UsersModel> acceptedUsers = usersDAO.getUsersByRoleAndStatus("user", "Diterima");
            acceptedUsersContainer.removeAll();

            if (acceptedUsers.isEmpty()) {
                Span noUsersMessage = new Span("Tidak ada user yang diterima.");
                noUsersMessage.getStyle().set("font-style", "italic").set("color", "#666");
                acceptedUsersContainer.add(noUsersMessage);
            } else {
                for (UsersModel user : acceptedUsers) {
                    acceptedUsersContainer.add(createUserCard(user, true));
                }
            }
        } catch (SQLException e) {
            Notification.show("Error loading accepted users: " + e.getMessage(), 3000, TOP_CENTER);
        }
    }

    private HorizontalLayout createUserCard(UsersModel user, boolean isAccepted) {
        HorizontalLayout card = new HorizontalLayout();
        card.setWidthFull();
        card.setPadding(true);
        card.setSpacing(true);
        card.setAlignItems(Alignment.CENTER);
        card.getStyle()
                .set("border", "1px solid #e0e0e0")
                .set("border-radius", "20px")
                .set("background-color", "white")
                .set("box-shadow", "0 2px 4px rgba(0,0,0,0.1)");

        Image userPhoto = new Image();
        if (user.getFoto() != null && !user.getFoto().isEmpty()) {
            userPhoto.setSrc(user.getFoto());
        } else {
            userPhoto.setSrc("images/usericon.png");
        }
        userPhoto.setWidth("60px");
        userPhoto.setHeight("60px");
        userPhoto.getStyle()
                .set("border-radius", "50%")
                .set("object-fit", "cover");

        VerticalLayout userInfo = new VerticalLayout();
        userInfo.setPadding(false);
        userInfo.setSpacing(false);

        Span username = new Span(user.getUsername());
        username.getStyle().set("font-weight", "bold").set("font-size", "16px");

        Span email = new Span(user.getEmail());
        email.getStyle().set("color", "#666").set("font-size", "14px");

        Span status = new Span("Status: " + user.getStatus());
        status.getStyle()
                .set("font-size", "12px")
                .set("color", isAccepted ? "#2e7d32" : "#d32f2f")
                .set("font-weight", "bold");

        userInfo.add(username, email, status);

        HorizontalLayout actionButtons = new HorizontalLayout();
        actionButtons.setSpacing(true);

        if (!isAccepted) {
            Button acceptButton = new Button("Terima");
            acceptButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
            acceptButton.addClickListener(e -> acceptUser(user));

            Button rejectButton = new Button("Tolak");
            rejectButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            rejectButton.addClickListener(e -> rejectUser(user));

            actionButtons.add(acceptButton, rejectButton);
        } else {
            Button revokeButton = new Button("Hapus");
            revokeButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            revokeButton.addClickListener(e -> revokeUser(user));

            actionButtons.add(revokeButton);
        }

        card.add(userPhoto, userInfo, actionButtons);
        card.setFlexGrow(0, userPhoto);
        card.setFlexGrow(1, userInfo);
        card.setFlexGrow(0, actionButtons);

        return card;
    }

    private void acceptUser(UsersModel user) {
        try {
            usersDAO.updateUserStatus(user.getIdUsers(), "Diterima");
            Notification.show("User " + user.getUsername() + " berhasil diterima!", 3000, TOP_CENTER);
            refreshUserLists();
        } catch (SQLException e) {
            Notification.show("Error accepting user: " + e.getMessage(), 3000, TOP_CENTER);
        }
    }

    private void rejectUser(UsersModel user) {
        try {
            usersDAO.deleteUser(user.getIdUsers());
            Notification.show("User " + user.getUsername() + " berhasil dihapus.", 3000, TOP_CENTER);
            refreshUserLists();
        } catch (SQLException e) {
            Notification.show("Error rejecting user: " + e.getMessage(), 3000, TOP_CENTER);
        }
    }

    private void revokeUser(UsersModel user) {
        try {
            usersDAO.deleteUser(user.getIdUsers());
            Notification.show("User " + user.getUsername() + " berhasil dihapus.", 3000, TOP_CENTER);
            refreshUserLists();
        } catch (SQLException e) {
            Notification.show("Error revoking user: " + e.getMessage(), 3000, TOP_CENTER);
        }
    }

    private void refreshUserLists() {
        loadPendingUsers();
        loadAcceptedUsers();
    }
}