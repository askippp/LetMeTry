package com.example.application.view.user;

import com.example.application.components.Header;
import com.example.application.components.SidebarUser;
import com.example.application.dao.UsersDAO;
import com.example.application.model.UsersModel;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static com.vaadin.flow.component.notification.Notification.Position.TOP_CENTER;

@Route("rankings")
@PageTitle("Rankings")
public class Rankings extends VerticalLayout implements BeforeEnterObserver {

    private UsersDAO usersDAO;
    private List<UsersModel> topUsers;

    public Rankings() {
        this.usersDAO = new UsersDAO();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        UsersModel user = VaadinSession.getCurrent().getAttribute(UsersModel.class);
        if (user == null) {
            event.forwardTo("login");
            Notification.show("Kamu Harus Login Terlebih dahulu!", 2000, TOP_CENTER);
        } else {
            try {
                loadRankingData();
                buildLayout();
            } catch (SQLException e) {
                Notification.show("Error loading ranking data: " + e.getMessage(), 3000, TOP_CENTER);
                e.printStackTrace();
            }
        }
    }

    private void loadRankingData() throws SQLException {
        topUsers = usersDAO.getUsersByRoleAndStatus("user", "Diterima")
                .stream()
                .sorted((u1, u2) -> Integer.compare(u2.getPoint(), u1.getPoint()))
                .collect(Collectors.toList());
    }

    private void buildLayout() {
        removeAll();
        setPadding(false);
        setMargin(false);
        setSpacing(false);
        setSizeFull();
        getStyle().set("overflow", "hidden");

        HorizontalLayout header = Header.headerWithLogo();
        header.getStyle().set("flex-shrink", "0");

        HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.setSizeFull();
        mainLayout.setPadding(false);
        mainLayout.setMargin(false);
        mainLayout.setSpacing(false);
        mainLayout.getStyle().set("overflow", "hidden");

        VerticalLayout sidebar = SidebarUser.createSidebar();
        sidebar.getStyle().set("flex-shrink", "0");
        sidebar.getStyle().set("overflow-y", "auto");

        Div mainContent = createMainContent();

        mainLayout.add(sidebar, mainContent);
        mainLayout.setFlexGrow(0, sidebar);
        mainLayout.setFlexGrow(1, mainContent);

        add(header, mainLayout);
        setFlexGrow(0, header);
        setFlexGrow(1, mainLayout);
    }

    private Div createMainContent() {
        Div content = new Div();
        content.setSizeFull();
        content.getStyle()
                .set("background-color", "#E8F5E8")
                .set("overflow-y", "auto")
                .set("padding", "20px");

        VerticalLayout container = new VerticalLayout();
        container.setWidth("100%");
        container.setMaxWidth("1200px");
        container.getStyle().set("margin", "0 auto");
        container.setPadding(false);
        container.setSpacing(true);

        if (topUsers.size() >= 3) {
            HorizontalLayout topThreeLayout = createTopThreeLayout();
            container.add(topThreeLayout);

            if (topUsers.size() > 3) {
                Component rankingTable = createRankingTable();
                container.add(rankingTable);
            }
        } else {
            H3 subtitle = new H3("Tidak cukup pengguna untuk menampilkan top 3");
            subtitle.getStyle()
                    .set("text-align", "center")
                    .set("color", "#666");

            Component rankingTable = createRankingTableAll();
            container.add(subtitle, rankingTable);
        }

        content.add(container);
        return content;
    }

    private HorizontalLayout createTopThreeLayout() {
        HorizontalLayout topThree = new HorizontalLayout();
        topThree.setWidthFull();
        topThree.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        topThree.setAlignItems(FlexComponent.Alignment.END);
        topThree.getStyle()
                .set("margin-bottom", "40px")
                .set("gap", "20px");

        UsersModel firstPlace = topUsers.get(0);
        UsersModel secondPlace = topUsers.size() > 1 ? topUsers.get(1) : null;
        UsersModel thirdPlace = topUsers.size() > 2 ? topUsers.get(2) : null;

        if (thirdPlace != null) {
            VerticalLayout thirdCard = createRankCard(
                    thirdPlace.getUsername(),
                    thirdPlace.getPoint(),
                    3,
                    "#CD7F32",
                    getAvatarForUser(thirdPlace),
                    false
            );
            topThree.add(thirdCard);
        }

        VerticalLayout firstCard = createRankCard(
                firstPlace.getUsername(),
                firstPlace.getPoint(),
                1,
                "#FFD700",
                getAvatarForUser(firstPlace),
                true
        );
        topThree.add(firstCard);

        if (secondPlace != null) {
            VerticalLayout secondCard = createRankCard(
                    secondPlace.getUsername(),
                    secondPlace.getPoint(),
                    2,
                    "#C0C0C0",
                    getAvatarForUser(secondPlace),
                    false
            );
            topThree.add(secondCard);
        }

        return topThree;
    }

    private Image getAvatarForUser(UsersModel user) {
        Image userImage = new Image();
        if (user != null && user.getFoto() != null && !user.getFoto().isEmpty()) {
            userImage.setSrc(user.getFoto());
            userImage.setWidth("50px");
            userImage.setHeight("50px");
        } else {
            userImage.setSrc("images/usericon.png");
            userImage.setWidth("50px");
            userImage.setHeight("50px");
        }

        userImage.getStyle()
                .set("border-radius", "50%")
                .set("object-fit", "cover");

        return userImage;
    }

    private VerticalLayout createRankCard(
            String username, int points, int rank, String medalColor,
            Image avatarProfile, boolean isFirst
    ) {
        VerticalLayout card = new VerticalLayout();
        card.setWidth("180px");
        if (isFirst) {
            card.setHeight("220px");
        } else {
            card.setHeight("200px");
        }
        card.setPadding(true);
        card.setSpacing(false);
        card.setAlignItems(FlexComponent.Alignment.CENTER);
        card.getStyle()
                .set("background-color", "white")
                .set("border-radius", "15px")
                .set("box-shadow", "0 4px 12px rgba(0,0,0,0.1)")
                .set("position", "relative")
                .set("margin", "0");

        // Medal
        Div medal = new Div();
        medal.setText(String.valueOf(rank));
        medal.getStyle()
                .set("position", "absolute")
                .set("top", "-15px")
                .set("right", "15px")
                .set("width", "35px")
                .set("height", "35px")
                .set("background-color", medalColor)
                .set("color", "white")
                .set("border-radius", "50%")
                .set("display", "flex")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("font-weight", "bold")
                .set("font-size", "16px")
                .set("border", "3px solid white")
                .set("z-index", "10");

        // Avatar
        Image avatar = avatarProfile;
        avatar.getStyle()
                .set("width", isFirst ? "90px" : "70px")
                .set("height", isFirst ? "90px" : "70px")
                .set("border-radius", "50%")
                .set("background-color", "#F0F0F0")
                .set("margin", "25px 0 15px 0");

        // Username
        H3 name = new H3(username);
        name.getStyle()
                .set("margin", "0 0 10px 0")
                .set("color", "#333")
                .set("font-size", isFirst ? "20px" : "18px")
                .set("text-align", "center");

        // Points
        HorizontalLayout pointsDiv = new HorizontalLayout();
        pointsDiv.setSpacing(false);
        pointsDiv.setAlignItems(FlexComponent.Alignment.CENTER);
        pointsDiv.getStyle()
                .set("gap", "5px")
                .set("margin", "0");

        Icon diamondIcon = new Icon(VaadinIcon.DIAMOND);
        diamondIcon.setColor("#4CAF50");
        diamondIcon.setSize("18px");

        Span pointsText = new Span(String.valueOf(points));
        pointsText.getStyle()
                .set("color", "#4CAF50")
                .set("font-weight", "bold")
                .set("font-size", "16px");

        pointsDiv.add(diamondIcon, pointsText);

        card.add(medal, avatar, name, pointsDiv);

        return card;
    }

    private Component createRankingTable() {
        VerticalLayout tableContainer = new VerticalLayout();
        tableContainer.setWidth("100%");
        tableContainer.setMaxWidth("800px");
        tableContainer.setPadding(false);
        tableContainer.setSpacing(false);
        tableContainer.getStyle()
                .set("background-color", "white")
                .set("border-radius", "15px")
                .set("box-shadow", "0 2px 8px rgba(0,0,0,0.1)")
                .set("margin", "0 auto")
                .set("overflow", "hidden");

        // Table Header
        HorizontalLayout header = createTableHeader();

        // Table Body
        VerticalLayout tableBody = new VerticalLayout();
        tableBody.setSpacing(false);
        tableBody.setPadding(false);
        tableBody.setWidthFull();

        // Add users from rank 4 onwards
        for (int i = 3; i < topUsers.size(); i++) {
            UsersModel user = topUsers.get(i);
            boolean isLast = (i == topUsers.size() - 1);
            HorizontalLayout tableRow = createTableRow(
                    String.format("%02d", i + 1),
                    getAvatarForUser(user),
                    user.getUsername(),
                    String.valueOf(user.getPoint()),
                    isLast
            );
            tableBody.add(tableRow);
        }

        tableContainer.add(header, tableBody);
        return tableContainer;
    }

    private Component createRankingTableAll() {
        VerticalLayout tableContainer = new VerticalLayout();
        tableContainer.setWidth("100%");
        tableContainer.setMaxWidth("800px");
        tableContainer.setPadding(false);
        tableContainer.setSpacing(false);
        tableContainer.getStyle()
                .set("background-color", "white")
                .set("border-radius", "15px")
                .set("box-shadow", "0 2px 8px rgba(0,0,0,0.1)")
                .set("margin", "0 auto")
                .set("overflow", "hidden");

        // Table Header
        HorizontalLayout header = createTableHeader();

        // Table Body
        VerticalLayout tableBody = new VerticalLayout();
        tableBody.setSpacing(false);
        tableBody.setPadding(false);
        tableBody.setWidthFull();

        // Add all users
        for (int i = 0; i < topUsers.size(); i++) {
            UsersModel user = topUsers.get(i);
            boolean isLast = (i == topUsers.size() - 1);
            HorizontalLayout tableRow = createTableRow(
                    String.format("%02d", i + 1),
                    getAvatarForUser(user),
                    user.getUsername(),
                    String.valueOf(user.getPoint()),
                    isLast
            );
            tableBody.add(tableRow);
        }

        tableContainer.add(header, tableBody);
        return tableContainer;
    }

    private HorizontalLayout createTableHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.getStyle()
                .set("background-color", "#F8F9FA")
                .set("padding", "15px 20px")
                .set("border-bottom", "1px solid #E0E0E0")
                .set("margin", "0");

        Span rankHeader = new Span("Rank");
        rankHeader.getStyle()
                .set("font-weight", "bold")
                .set("width", "80px")
                .set("color", "#666");

        Span usernameHeader = new Span("Username");
        usernameHeader.getStyle()
                .set("font-weight", "bold")
                .set("flex", "1")
                .set("color", "#666");

        Span pointHeader = new Span("Point");
        pointHeader.getStyle()
                .set("font-weight", "bold")
                .set("width", "100px")
                .set("text-align", "right")
                .set("color", "#666");

        header.add(rankHeader, usernameHeader, pointHeader);
        return header;
    }

    private HorizontalLayout createTableRow(
            String rank, Image avatarProfile, String username, String points, boolean isLast
    ) {
        HorizontalLayout row = new HorizontalLayout();
        row.setWidthFull();
        row.setAlignItems(FlexComponent.Alignment.CENTER);
        row.getStyle()
                .set("padding", "15px 20px")
                .set("margin", "0");

        if (!isLast) {
            row.getStyle().set("border-bottom", "1px solid #F0F0F0");
        }

        // Rank
        Span rankSpan = new Span(rank);
        rankSpan.getStyle()
                .set("width", "80px")
                .set("color", "#666")
                .set("font-weight", "500");

        // Avatar + Username
        HorizontalLayout userInfo = new HorizontalLayout();
        userInfo.setSpacing(false);
        userInfo.setAlignItems(FlexComponent.Alignment.CENTER);
        userInfo.getStyle()
                .set("flex", "1")
                .set("gap", "12px");

        Image avatar = avatarProfile;
        avatar.getStyle()
                .set("width", "35px")
                .set("height", "35px")
                .set("border-radius", "50%")
                .set("background-color", "#F5F5F5");

        Span usernameSpan = new Span(username);
        usernameSpan.getStyle()
                .set("color", "#333")
                .set("font-weight", "500");

        userInfo.add(avatar, usernameSpan);

        // Points
        HorizontalLayout pointsLayout = new HorizontalLayout();
        pointsLayout.setSpacing(false);
        pointsLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        pointsLayout.getStyle()
                .set("width", "100px")
                .set("justify-content", "flex-end")
                .set("gap", "5px");

        Icon diamondIcon = new Icon(VaadinIcon.DIAMOND);
        diamondIcon.setColor("#4CAF50");
        diamondIcon.setSize("16px");

        Span pointsSpan = new Span(points);
        pointsSpan.getStyle()
                .set("color", "#4CAF50")
                .set("font-weight", "bold")
                .set("font-size", "15px");

        pointsLayout.add(diamondIcon, pointsSpan);

        row.add(rankSpan, userInfo, pointsLayout);

        return row;
    }
}