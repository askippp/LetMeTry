package com.example.application.view.user;

import com.example.application.components.ClockView;
import com.example.application.components.Header;
import com.example.application.components.SidebarUser;
import com.example.application.model.UsersModel;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import static com.vaadin.flow.component.notification.Notification.Position.TOP_CENTER;

@Route("dashboard")
@PageTitle("Dashboard - LetMeTry")
public class DashboardUser extends VerticalLayout implements BeforeEnterObserver {

    private Div overlay;
    private Div drawer;
    private boolean isDrawerOpen = false;

    public DashboardUser() {}

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        UsersModel user = VaadinSession.getCurrent().getAttribute(UsersModel.class);
        if (user == null) {
            event.forwardTo("login");
            Notification.show("Kamu Harus Login Terlebih dahulu!", 2000, TOP_CENTER);
        } else if(user.getRole().equalsIgnoreCase("admin")) {
            event.forwardTo("users");
            Notification.show("Anda adalah Admin!", 2000, TOP_CENTER);
        } else if(user.getStatus().equalsIgnoreCase("Tidak Diterima")) {
            event.forwardTo("");
            Notification.show("Akun anda tidak diterima!", 2000, TOP_CENTER);
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

        VerticalLayout sidebar = SidebarUser.createSidebar();

        VerticalLayout mainContent = createMainContent();

        mainLayout.add(sidebar, mainContent);
        mainLayout.setFlexGrow(0, sidebar);
        mainLayout.setFlexGrow(1, mainContent);

        add(header, mainLayout);
        setFlexGrow(0, header);
        setFlexGrow(1, mainLayout);

        // Create overlay and drawer
        createDrawerElements();
        add(overlay, drawer);
    }
    UsersModel currentUser = VaadinSession.getCurrent().getAttribute(UsersModel.class);

    // Konstanta untuk minimum poin yang diperlukan
    private static final int LEVEL_2_MIN_POINTS = 200; // Sesuaikan dengan kebutuhan
    private static final int LEVEL_3_MIN_POINTS = 500; // Sesuaikan dengan kebutuhan

    private void createDrawerElements() {
        // Create overlay
        overlay = new Div();
        overlay.getStyle()
                .set("position", "fixed")
                .set("top", "0")
                .set("left", "0")
                .set("width", "100vw")
                .set("height", "100vh")
                .set("background-color", "rgba(0, 0, 0, 0.5)")
                .set("z-index", "9998")
                .set("opacity", "0")
                .set("visibility", "hidden")
                .set("transition", "all 0.3s ease");

        // Close drawer when clicking overlay
        overlay.addClickListener(e -> {
            closeDrawer();
        });

        // Create drawer
        drawer = new Div();
        drawer.getStyle()
                .set("position", "fixed")
                .set("top", "0")
                .set("right", "-300px") // Initially hidden
                .set("width", "300px")
                .set("height", "100vh")
                .set("background-color", "white")
                .set("z-index", "9999")
                .set("box-shadow", "-2px 0 10px rgba(0,0,0,0.2)")
                .set("transition", "right 0.3s ease")
                .set("overflow-y", "auto");

        // Add drawer content
        VerticalLayout drawerContent = createDrawerContent();
        drawer.add(drawerContent);
    }

    private VerticalLayout createDrawerContent() {
        VerticalLayout content = new VerticalLayout();
        content.setPadding(true);
        content.setSpacing(true);
        content.setSizeFull();

        // Header with close button
        HorizontalLayout drawerHeader = new HorizontalLayout();
        drawerHeader.setWidthFull();
        drawerHeader.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        drawerHeader.setAlignItems(FlexComponent.Alignment.CENTER);

        H3 title = new H3("Try Out");
        title.getStyle().set("margin", "0");

        Button closeButton = new Button("×");
        closeButton.getStyle()
                .set("background", "none")
                .set("border", "none")
                .set("font-size", "24px")
                .set("cursor", "pointer")
                .set("padding", "5px 10px")
                .set("border-radius", "50%")
                .set("width", "40px")
                .set("height", "40px")
                .set("color", "#6b7280");

        closeButton.addClickListener(e -> {
            closeDrawer();
        });

        drawerHeader.add(title, closeButton);
        content.add(drawerHeader);

        // Add user points info
        if (currentUser != null) {
            Div pointsInfo = new Div();
            pointsInfo.setText("Point Anda: " + currentUser.getPoint());
            pointsInfo.getStyle()
                    .set("background-color", "#e0f2fe")
                    .set("color", "#0277bd")
                    .set("padding", "8px 12px")
                    .set("border-radius", "6px")
                    .set("text-align", "center")
                    .set("font-weight", "500")
                    .set("margin-bottom", "10px");
            content.add(pointsInfo);
        }

        // Add levels with point requirements
        content.add(createLevelSection(
                "Level 1 (Easy)", "60 menit", "tryout-level1", 0)
        );
        content.add(createLevelSection(
                "Level 2 (Medium)", "50 menit", "tryout-level2", LEVEL_2_MIN_POINTS)
        );
        content.add(createLevelSection(
                "Level 3 (Hard)", "45 menit", "tryout-level3", LEVEL_3_MIN_POINTS)
        );

        return content;
    }

    private VerticalLayout createLevelSection(String levelName, String duration, String levelRoutePrefix, int requiredPoints) {
        VerticalLayout levelSection = new VerticalLayout();
        levelSection.setPadding(true);
        levelSection.setSpacing(true);

        // Check if user has enough points
        boolean isUnlocked = currentUser != null && currentUser.getPoint() >= requiredPoints;

        levelSection.getStyle()
                .set("border", "1px solid #e5e7eb")
                .set("border-radius", "12px")
                .set("background-color", isUnlocked ? "#f9fafb" : "#f5f5f5")
                .set("margin-bottom", "10px")
                .set("opacity", isUnlocked ? "1" : "0.6");

        // Level header
        HorizontalLayout levelHeader = new HorizontalLayout();
        levelHeader.setWidthFull();
        levelHeader.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        levelHeader.setAlignItems(FlexComponent.Alignment.CENTER);

        VerticalLayout titleSection = new VerticalLayout();
        titleSection.setPadding(false);
        titleSection.setSpacing(false);

        Span levelTitle = new Span(levelName);
        levelTitle.getStyle()
                .set("font-weight", "bold")
                .set("font-size", "1.1rem")
                .set("color", isUnlocked ? "#374151" : "#9ca3af");

        titleSection.add(levelTitle);

        // Add point requirement info if locked
        if (!isUnlocked && requiredPoints >= 0) {
            Span pointRequirement = new Span("Butuh " + requiredPoints + " poin");
            pointRequirement.getStyle()
                    .set("font-size", "0.8rem")
                    .set("color", "#ef4444")
                    .set("font-weight", "500");
            titleSection.add(pointRequirement);
        }

        Span durationText = new Span(duration);
        durationText.getStyle()
                .set("font-size", "0.9rem")
                .set("color", isUnlocked ? "#6b7280" : "#9ca3af")
                .set("background-color", isUnlocked ? "#e5e7eb" : "#d1d5db")
                .set("padding", "4px 8px")
                .set("border-radius", "6px");

        levelHeader.add(titleSection, durationText);

        // Subject buttons
        VerticalLayout subjectButtons = new VerticalLayout();
        subjectButtons.setPadding(false);
        subjectButtons.setSpacing(true);
        subjectButtons.setWidthFull();

        Button mtkButton = createSubjectButton(
                "Matematika", levelRoutePrefix + "/matematika", isUnlocked
        );
        Button ingButton = createSubjectButton(
                "Bahasa Inggris", levelRoutePrefix + "/inggris", isUnlocked
        );
        Button indButton = createSubjectButton(
                "Bahasa Indonesia", levelRoutePrefix + "/indonesia", isUnlocked
        );

        subjectButtons.add(mtkButton, ingButton, indButton);

        levelSection.add(levelHeader, subjectButtons);

        return levelSection;
    }

    private Button createSubjectButton(String text, String route, boolean isEnabled) {
        Button button = new Button(text);
        button.setWidthFull();
        button.setEnabled(isEnabled);

        if (isEnabled) {
            // Enabled button style
            button.getStyle()
                    .set("background-color", "white")
                    .set("border", "1px solid #d1d5db")
                    .set("border-radius", "8px")
                    .set("padding", "12px 16px")
                    .set("text-align", "left")
                    .set("cursor", "pointer")
                    .set("transition", "all 0.2s ease")
                    .set("font-weight", "500")
                    .set("color", "#374151");

            // Hover effect for enabled buttons
            button.getElement().setAttribute("onmouseover",
                    "this.style.backgroundColor='#f3f4f6'; this.style.borderColor='#9ca3af';");
            button.getElement().setAttribute("onmouseout",
                    "this.style.backgroundColor='white'; this.style.borderColor='#d1d5db';");

            button.addClickListener(e -> {
                closeDrawer();
                // Small delay to ensure drawer closes before navigation
                UI.getCurrent().getPage().executeJs(
                        "setTimeout(() => { window.location.href = '" + route + "'; }, 100);"
                );
            });
        } else {
            // Disabled button style
            button.getStyle()
                    .set("background-color", "#f5f5f5")
                    .set("border", "1px solid #d1d5db")
                    .set("border-radius", "8px")
                    .set("padding", "12px 16px")
                    .set("text-align", "left")
                    .set("cursor", "not-allowed")
                    .set("font-weight", "500")
                    .set("color", "#9ca3af")
                    .set("opacity", "0.6");

            // Add lock icon or indicator
            button.setText("🔒 " + text);

            // Optional: Show notification when clicked
            button.addClickListener(e -> {
                Notification.show("Level ini belum terbuka. Kumpulkan lebih banyak poin!", 3000, Notification.Position.MIDDLE);
            });
        }

        return button;
    }

    // Method untuk update drawer content ketika poin user berubah
    public void refreshDrawerContent() {
        if (drawer != null) {
            drawer.removeAll();
            VerticalLayout newContent = createDrawerContent();
            drawer.add(newContent);
        }
    }

    // Setter untuk current user
    public void setCurrentUser(UsersModel user) {
        this.currentUser = user;
        refreshDrawerContent(); // Refresh content when user changes
    }

    private void openDrawer() {
        if (!isDrawerOpen) {
            // Refresh content before opening to ensure latest user data
            refreshDrawerContent();

            isDrawerOpen = true;
            overlay.getStyle()
                    .set("visibility", "visible")
                    .set("opacity", "1");
            drawer.getStyle().set("right", "0px");
        }
    }

    private void closeDrawer() {
        if (isDrawerOpen) {
            isDrawerOpen = false;
            overlay.getStyle().set("opacity", "0");
            drawer.getStyle().set("right", "-400px");

            // Hide overlay after animation completes
            UI.getCurrent().getPage().executeJs(
                    "setTimeout(() => { " +
                            "if (arguments[0]) arguments[0].style.visibility = 'hidden'; " +
                            "}, 300);",
                    overlay.getElement()
            );
        }
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

        HorizontalLayout topLayout = createTopLayout();

        VerticalLayout matematika = new VerticalLayout();
        matematika.setPadding(false);
        matematika.setSpacing(false);

        Span titleMtk = new Span("MATERI MATEMATIKA");
        titleMtk.addClassName("lora-text");
        titleMtk.getStyle()
                .setFontSize("1.2rem")
                .setFontWeight("bold");

        HorizontalLayout cardMtk = new HorizontalLayout(
                createCard("MATEMATIKA", "Kelas 10", "mtk.png", "materi-mtk-10"),
                createCard("MATEMATIKA", "Kelas 11", "mtk.png", "materi-mtk-11"),
                createCard("MATEMATIKA", "Kelas 12", "mtk.png", "materi-mtk-12")
        );
        cardMtk.setWidthFull();
        cardMtk.setSpacing(true);
        cardMtk.setJustifyContentMode(JustifyContentMode.BETWEEN);
        matematika.add(titleMtk, cardMtk);

        VerticalLayout ing = new VerticalLayout();
        ing.setPadding(false);
        ing.setSpacing(false);

        Span titleIng = new Span("MATERI BAHASA INGGRIS");
        titleIng.addClassName("lora-text");
        titleIng.getStyle()
                .setFontSize("1.2rem")
                .setFontWeight("bold");

        HorizontalLayout cardIng = new HorizontalLayout(
                createCard("BAHASA INGGRIS", "Kelas 10", "english.png", "materi-ing-10"),
                createCard("BAHASA INGGRIS", "Kelas 11", "english.png", "materi-ing-11"),
                createCard("BAHASA INGGRIS", "Kelas 12", "english.png", "materi-ing-12")
        );
        cardIng.setWidthFull();
        cardIng.setSpacing(true);
        cardIng.setJustifyContentMode(JustifyContentMode.BETWEEN);
        ing.add(titleIng, cardIng);

        VerticalLayout ind = new VerticalLayout();
        ind.setPadding(false);
        ind.setSpacing(false);

        Span titleInd = new Span("MATERI BAHASA INDONESIA");
        titleInd.addClassName("lora-text");
        titleInd.getStyle()
                .setFontSize("1.2rem")
                .setFontWeight("bold");

        HorizontalLayout cardInd = new HorizontalLayout(
                createCard("BAHASA INDONESIA", "Kelas 10", "indo.png", "materi-ind-10"),
                createCard("BAHASA INDONESIA", "Kelas 11", "indo.png", "materi-ind-11"),
                createCard("BAHASA INDONESIA", "Kelas 12", "indo.png", "materi-ind-12")
        );
        cardInd.setWidthFull();
        cardInd.setSpacing(true);
        cardInd.setJustifyContentMode(JustifyContentMode.BETWEEN);
        ind.add(titleInd, cardInd);

        content.add(topLayout, matematika, ing, ind);

        return content;
    }

    private HorizontalLayout createTopLayout() {
        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidthFull();
        topLayout.setSpacing(false);
        topLayout.setAlignItems(Alignment.CENTER);
        topLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);

        ClockView clockView = new ClockView();
        clockView.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        clockView.setAlignItems(FlexComponent.Alignment.CENTER);
        clockView.getStyle()
                .setBackgroundColor("#f9fafb")
                .setMarginTop("-20px")
                .setBorderRadius("20px")
                .setPaddingLeft("10px")
                .setPaddingRight("15px")
                .setBorder("2px solid #d1d5db");

        Button tryout = new Button("MULAI TRYOUT");
        tryout.addClassNames("inter-text");
        tryout.getStyle()
                .setCursor("pointer")
                .setColor("#FFFFFF")
                .setBackgroundColor("#16C47F")
                .setBorderRadius("10px")
                .setBorderBottom("2px solid #0e9e66")
                .setPadding("10px 20px")
                .setFontWeight("bold")
                .setMarginRight("25px");

        // Updated click listener to open drawer instead of dialog
        tryout.addClickListener(e -> openDrawer());

        Span instruksi = new Span("Pelajari Instruksi Pengerjaan →");
        instruksi.getStyle()
                .setColor("#000000")
                .setCursor("pointer");

        String infoText = """
                1. Pelajari materi terlebih dahulu sebelum memulai tryout.
                
                2. Kerjakan latihan-latihan soal yang sudah disediakan di setiap materi pembelajaran.
                
                3. Pengerjaan TryOut diberi waktu dan level:
                
                   - Level 1 : 60 menit
                   - Level 2 : 50 menit
                   - Level 3 : 45 menit
                
                4. Terdapat skor hasil jawaban yang dikirim dan tersedia koreksi jawaban yang salah di history.
                
                5. Jangan lupa diperiksa kembali jawaban sebelum mengirimkan.
                """;

        Dialog info = new Dialog();
        info.setHeaderTitle("Petunjuk");

        Paragraph petunjuk = new Paragraph(infoText);
        petunjuk.getStyle()
                .setWhiteSpace(Style.WhiteSpace.PRE_LINE);

        info.add(petunjuk);

        info.setWidth("550px");
        info.setHeight("400px");

        instruksi.getElement().addEventListener("click", e -> info.open());

        VerticalLayout tryoutLayout = new VerticalLayout();
        tryoutLayout.setPadding(false);
        tryoutLayout.setSpacing(false);
        tryoutLayout.setAlignItems(Alignment.END);
        tryoutLayout.add(tryout, instruksi);

        topLayout.add(clockView, tryoutLayout);

        return topLayout;
    }

    private HorizontalLayout createCard(String mapel, String kelas, String imageFileName, String targetRoute) {
        HorizontalLayout card = new HorizontalLayout();
        card.setWidthFull();
        card.getStyle()
                .setBackgroundColor("#FFFFFF")
                .setBorderRadius("20px")
                .setPadding("10px")
                .setMarginBottom("10px")
                .setBorder("2px solid #d1d5db");

        card.addClickListener(e -> UI.getCurrent().navigate(targetRoute));
        card.getStyle().setCursor("pointer");

        Div imageWrapper = new Div();
        imageWrapper.getStyle()
                .setBackgroundColor("#16C47F")
                .setHeight("70px")
                .setWidth("100px")
                .setBorderRadius("10px")
                .setDisplay(Style.Display.FLEX)
                .setAlignItems(Style.AlignItems.CENTER)
                .setJustifyContent(Style.JustifyContent.CENTER);

        Image image = new Image("images/" + imageFileName, mapel);
        image.setHeight("70px");
        image.setWidth("70px");
        imageWrapper.add(image);

        VerticalLayout textCard = new VerticalLayout();
        textCard.setJustifyContentMode(JustifyContentMode.CENTER);
        textCard.setPadding(false);
        textCard.setSpacing(false);

        Span titleCard = new Span(mapel);
        titleCard.addClassName("lora-text");
        titleCard.getStyle()
                .setFontSize("1rem")
                .setFontWeight("bold");

        Span subTitleCard = new Span(kelas);
        subTitleCard.addClassName("lora-text");
        subTitleCard.getStyle()
                .setFontSize("1rem")
                .setFontWeight("bold");

        textCard.add(titleCard, subTitleCard);
        card.add(imageWrapper, textCard);

        card.getStyle().set("flex", "1");

        return card;
    }
}