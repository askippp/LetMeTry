package com.example.application.view.user;

import com.example.application.components.Header;
import com.example.application.components.SidebarUser;
import com.example.application.model.UsersModel;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
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

import static com.vaadin.flow.component.notification.Notification.Position.TOP_CENTER;

@Route("rankings")
@PageTitle("Rankings")
public class Rankings extends VerticalLayout implements BeforeEnterObserver {

    public Rankings() {}

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        UsersModel user = VaadinSession.getCurrent().getAttribute(UsersModel.class);
        if (user == null) {
            event.forwardTo("login");
            Notification.show("Kamu Harus Login Terlebih dahulu!", 2000, TOP_CENTER);
        } else {
            buildLayout();
        }
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

        // Container untuk semua konten
        VerticalLayout container = new VerticalLayout();
        container.setWidth("100%");
        container.setMaxWidth("1200px");
        container.getStyle().set("margin", "0 auto");
        container.setPadding(false);
        container.setSpacing(true);

        // Title
        H2 title = new H2("Leaderboard");
        title.getStyle()
                .set("margin", "0 0 30px 0")
                .set("color", "#2D5A2D")
                .set("text-align", "center");

        // Top 3 Users Section
        HorizontalLayout topThreeLayout = createTopThreeLayout();

        // Ranking Table Section
        Component rankingTable = createRankingTable();

        container.add(title, topThreeLayout, rankingTable);
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

        // 3rd Place (Left)
        VerticalLayout thirdPlace = createRankCard("Brimob", 700, 3, "#CD7F32", "👤", false);

        // 1st Place (Center - Taller)
        VerticalLayout firstPlace = createRankCard("Fahrur", 1000, 1, "#FFD700", "🎒", true);

        // 2nd Place (Right)
        VerticalLayout secondPlace = createRankCard("Daud", 800, 2, "#C0C0C0", "👤", false);

        topThree.add(thirdPlace, firstPlace, secondPlace);

        return topThree;
    }

    private VerticalLayout createRankCard(String username, int points, int rank, String medalColor, String avatarEmoji, boolean isFirst) {
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
        Div avatar = new Div();
        avatar.setText(avatarEmoji);
        avatar.getStyle()
                .set("width", isFirst ? "90px" : "70px")
                .set("height", isFirst ? "90px" : "70px")
                .set("border-radius", "50%")
                .set("background-color", "#F0F0F0")
                .set("display", "flex")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("font-size", isFirst ? "45px" : "35px")
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

        // Table Body
        VerticalLayout tableBody = new VerticalLayout();
        tableBody.setSpacing(false);
        tableBody.setPadding(false);
        tableBody.setWidthFull();

        // Sample data for ranks 4-7
        String[][] rankData = {
                {"04", "👤", "Udi", "655"},
                {"05", "🔴", "Bokur", "650"},
                {"06", "👤", "Cipto", "555"},
                {"07", "🌐", "Murfiq", "400"}
        };

        for (int i = 0; i < rankData.length; i++) {
            String[] row = rankData[i];
            HorizontalLayout tableRow = createTableRow(row[0], row[1], row[2], row[3], i == rankData.length - 1);
            tableBody.add(tableRow);
        }

        tableContainer.add(header, tableBody);

        return tableContainer;
    }

    private HorizontalLayout createTableRow(String rank, String avatarEmoji, String username, String points, boolean isLast) {
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

        Div avatar = new Div();
        avatar.setText(avatarEmoji);
        avatar.getStyle()
                .set("width", "35px")
                .set("height", "35px")
                .set("border-radius", "50%")
                .set("background-color", "#F5F5F5")
                .set("display", "flex")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("font-size", "18px");

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