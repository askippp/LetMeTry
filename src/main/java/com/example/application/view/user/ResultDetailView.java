package com.example.application.view.user;

import com.example.application.components.Header;
import com.example.application.components.SidebarUser;
import com.example.application.dao.HistoryDAO;
import com.example.application.model.HistoryModel;
import com.example.application.model.UsersModel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;

import java.sql.SQLException;

import static com.vaadin.flow.component.notification.Notification.Position.TOP_CENTER;

@Route("result-detail")
@PageTitle("Detail Hasil - Quiz")
public class ResultDetailView extends VerticalLayout implements BeforeEnterObserver, HasUrlParameter<String> {

    private String subject;
    private String date;
    private String level;
    private int score;
    private int percentage;
    private int idTryout;
    private HistoryModel historyData;

    public ResultDetailView() {}

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

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        // Parse parameter untuk ambil data quiz
        // Format: subject-date-level-score-percentage-idTryout
        if (parameter != null && !parameter.isEmpty()) {
            String[] parts = parameter.split("-");
            if (parts.length >= 6) {
                this.subject = parts[0];
                this.date = parts[1].replace("_", "/");
                this.level = parts[2];
                this.score = Integer.parseInt(parts[3]);
                this.percentage = Integer.parseInt(parts[4]);
                this.idTryout = Integer.parseInt(parts[5]);

                // Load data from database
                loadHistoryData();
            }
        }

        // Default values jika parameter tidak ada atau loading gagal
        if (this.historyData == null) {
            this.subject = "MTK";
            this.date = "12/07/2025";
            this.level = "Easy";
            this.score = 80;
            this.percentage = 80;
            this.idTryout = 1;

            // Create dummy data for fallback
            this.historyData = new HistoryModel();
            this.historyData.setLevel(this.level);
            this.historyData.setTanggal(this.date);
            this.historyData.setNilai(this.score);
            this.historyData.setPersentase(this.percentage);
            this.historyData.setTotalSoal(30);
            this.historyData.setJumlahBenar(24);
            this.historyData.setJumlahSalah(6);
            this.historyData.setNamaMapel("Matematika");
        }
    }

    private void loadHistoryData() {
        try {
            HistoryDAO historyDAO = new HistoryDAO();
            this.historyData = historyDAO.getDetailHistory(this.level, this.idTryout);

            if (this.historyData != null) {
                // Update local variables from database
                this.subject = mapMapelNameToCode(this.historyData.getNamaMapel());
                this.score = this.historyData.getNilai();
                this.percentage = this.historyData.getPersentase();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Notification.show("Gagal mengambil data detail", 3000, TOP_CENTER);
            this.historyData = null;
        }
    }

    private String mapMapelNameToCode(String namaMapel) {
        if (namaMapel == null) return "MTK";

        switch (namaMapel.toLowerCase()) {
            case "matematika":
                return "MTK";
            case "bahasa indonesia":
                return "BI";
            case "bahasa inggris":
                return "BE";
            default:
                return "MTK";
        }
    }

    private void buildLayout() {
        setPadding(false);
        setMargin(false);
        setSpacing(false);
        setSizeFull();
        getStyle().set("overflow", "hidden");

        HorizontalLayout header = Header.headerWithLogo();

        HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.setSizeFull();
        mainLayout.setPadding(false);
        mainLayout.setMargin(false);
        mainLayout.setSpacing(false);
        mainLayout.getStyle().set("overflow", "hidden");

        VerticalLayout sidebar = SidebarUser.createSidebar();
        sidebar.getStyle().set("overflow", "hidden");
        sidebar.getStyle().set("position", "fixed");
        sidebar.getStyle().set("height", "calc(100vh - 60px)");
        sidebar.getStyle().set("z-index", "1000");

        Div scrollableContent = createScrollableMainContent();

        mainLayout.add(sidebar, scrollableContent);
        mainLayout.setFlexGrow(0, sidebar);
        mainLayout.setFlexGrow(1, scrollableContent);

        add(header, mainLayout);
        setFlexGrow(0, header);
        setFlexGrow(1, mainLayout);
    }

    private Div createScrollableMainContent() {
        Div scrollableWrapper = new Div();
        scrollableWrapper.setSizeFull();
        scrollableWrapper.getStyle().set("margin-left", "250px");
        scrollableWrapper.getStyle().set("overflow-y", "auto");
        scrollableWrapper.getStyle().set("overflow-x", "hidden");
        scrollableWrapper.getStyle().set("height", "100%");

        VerticalLayout mainContent = createMainContent();
        scrollableWrapper.add(mainContent);

        return scrollableWrapper;
    }

    private VerticalLayout createMainContent() {
        VerticalLayout content = new VerticalLayout();
        content.setPadding(true);
        content.setSpacing(true);
        content.setWidthFull();
        content.getStyle().set("background-color", "#f8f9fa");
        content.getStyle().set("min-height", "100vh");
        content.getStyle().set("padding-top", "20px");

        VerticalLayout detailSection = createQuestionDetails();
        content.add(detailSection);

        return content;
    }

    private VerticalLayout createQuestionDetails() {
        VerticalLayout section = new VerticalLayout();
        section.setPadding(false);
        section.setSpacing(true);
        section.getStyle().set("margin-top", "30px");

        VerticalLayout questionsContainer = new VerticalLayout();
        questionsContainer.setPadding(false);
        questionsContainer.setSpacing(true);
        questionsContainer.getStyle().set("max-width", "900px");

        // Correct answers card
        if (historyData.getJumlahBenar() > 0) {
            Div correctCard = createQuestionTypeCard(
                    "BENAR",
                    "Soal Benar",
                    "Try Out " + historyData.getNamaMapel() + " - " + historyData.getLevel(),
                    historyData.getJumlahBenar(),
                    "#4CAF50"
            );
            questionsContainer.add(correctCard);
        }

        // Wrong answers card
        if (historyData.getJumlahSalah() > 0) {
            Div wrongCard = createQuestionTypeCard(
                    "SALAH",
                    "Soal Salah",
                    "Try Out " + historyData.getNamaMapel() + " - " + historyData.getLevel(),
                    historyData.getJumlahSalah(),
                    "#f44336"
            );
            questionsContainer.add(wrongCard);
        }

        // Empty/not answered card (if any)
        int kosong = historyData.getTotalSoal() - historyData.getTotalTerjawab();
        if (kosong > 0) {
            Div emptyCard = createQuestionTypeCard(
                    "KOSONG",
                    "Soal Tidak Dijawab",
                    "Try Out " + historyData.getNamaMapel() + " - " + historyData.getLevel(),
                    kosong,
                    "#ff9800"
            );
            questionsContainer.add(emptyCard);
        }

        section.add(questionsContainer);
        return section;
    }

    private Div createQuestionTypeCard(String status, String title, String subtitle, int count, String color) {
        Div card = new Div();
        card.getStyle().set("background", "white");
        card.getStyle().set("border-radius", "8px");
        card.getStyle().set("padding", "0");
        card.getStyle().set("box-shadow", "0 2px 4px rgba(0,0,0,0.1)");
        card.getStyle().set("border", "1px solid #e0e0e0");
        card.getStyle().set("overflow", "hidden");
        card.getStyle().set("margin-bottom", "15px");
        card.getStyle().set("cursor", "pointer");
        card.getStyle().set("transition", "transform 0.2s ease, box-shadow 0.2s ease");
        card.getStyle().set("width", "100%");
        card.getStyle().set("max-width", "900px");

        // Status header bar
        Div statusHeader = new Div();
        statusHeader.setText(status);
        statusHeader.getStyle().set("background-color", color);
        statusHeader.getStyle().set("color", "white");
        statusHeader.getStyle().set("padding", "8px 16px");
        statusHeader.getStyle().set("font-weight", "bold");
        statusHeader.getStyle().set("font-size", "12px");
        statusHeader.getStyle().set("text-transform", "uppercase");

        // Main content area
        HorizontalLayout mainContent = new HorizontalLayout();
        mainContent.setWidthFull();
        mainContent.setPadding(false);
        mainContent.setMargin(false);
        mainContent.setSpacing(false);
        mainContent.setAlignItems(Alignment.CENTER);
        mainContent.getStyle().set("padding", "20px");
        mainContent.getStyle().set("min-height", "100px");

        // Left side - Number box
        Div numberContainer = new Div();
        numberContainer.getStyle().set("border", "1px solid #e0e0e0");
        numberContainer.getStyle().set("border-radius", "8px");
        numberContainer.getStyle().set("width", "80px");
        numberContainer.getStyle().set("height", "80px");
        numberContainer.getStyle().set("display", "flex");
        numberContainer.getStyle().set("align-items", "center");
        numberContainer.getStyle().set("justify-content", "center");
        numberContainer.getStyle().set("background", "#fafafa");
        numberContainer.getStyle().set("min-width", "80px");

        Div numberText = new Div();
        numberText.setText(String.valueOf(count));
        numberText.getStyle().set("font-size", "32px");
        numberText.getStyle().set("font-weight", "bold");
        numberText.getStyle().set("color", "#333");

        numberContainer.add(numberText);

        // Content section
        VerticalLayout contentSection = new VerticalLayout();
        contentSection.setPadding(false);
        contentSection.setMargin(false);
        contentSection.setSpacing(false);
        contentSection.getStyle().set("flex", "1");
        contentSection.getStyle().set("padding-left", "20px");
        contentSection.setJustifyContentMode(JustifyContentMode.CENTER);

        Div titleDiv = new Div();
        titleDiv.setText(title);
        titleDiv.getStyle().set("color", "#999");
        titleDiv.getStyle().set("font-size", "14px");
        titleDiv.getStyle().set("margin-bottom", "6px");

        Div subtitleDiv = new Div();
        subtitleDiv.setText(subtitle);
        subtitleDiv.getStyle().set("font-weight", "bold");
        subtitleDiv.getStyle().set("font-size", "16px");
        subtitleDiv.getStyle().set("color", "#333");
        subtitleDiv.getStyle().set("margin-bottom", "12px");
        subtitleDiv.getStyle().set("text-decoration", "underline");

        // Info row with buttons
        HorizontalLayout infoRow = new HorizontalLayout();
        infoRow.setAlignItems(Alignment.CENTER);
        infoRow.setSpacing(true);
        infoRow.setPadding(false);
        infoRow.setMargin(false);
        infoRow.getStyle().set("gap", "8px");

        // Date button
        Button dateButton = createInfoButton(VaadinIcon.CALENDAR, formatDisplayDate(historyData.getTanggal()));

        // Time button - calculate from waktuPengerjaan
        String timeText = calculateTimeFromSeconds(historyData.getWaktuPengerjaan());
        Button timeButton = createInfoButton(VaadinIcon.CLOCK, timeText);

        // Detail button
        Button detailButton = createDetailButton();
        detailButton.addClickListener(e -> {
            Notification.show("Detail " + title + " - " + count + " soal", 2000, TOP_CENTER);
        });

        infoRow.add(dateButton, timeButton, detailButton);
        contentSection.add(titleDiv, subtitleDiv, infoRow);

        mainContent.add(numberContainer, contentSection);
        mainContent.setFlexGrow(0, numberContainer);
        mainContent.setFlexGrow(1, contentSection);

        card.add(statusHeader, mainContent);

        // Add hover effects
        card.getElement().addEventListener("mouseenter", event -> {
            card.getStyle().set("transform", "translateY(-1px)");
            card.getStyle().set("box-shadow", "0 4px 8px rgba(0,0,0,0.15)");
        });

        card.getElement().addEventListener("mouseleave", event -> {
            card.getStyle().set("transform", "translateY(0)");
            card.getStyle().set("box-shadow", "0 2px 4px rgba(0,0,0,0.1)");
        });

        return card;
    }

    private Button createInfoButton(VaadinIcon iconType, String text) {
        Button button = new Button();
        button.getStyle().set("background", "transparent");
        button.getStyle().set("border", "1px solid #e0e0e0");
        button.getStyle().set("border-radius", "6px");
        button.getStyle().set("padding", "6px 10px");
        button.getStyle().set("color", "#666");
        button.getStyle().set("font-size", "12px");
        button.getStyle().set("cursor", "default");

        Icon icon = new Icon(iconType);
        icon.getStyle().set("color", "#4CAF50");
        icon.getStyle().set("font-size", "14px");

        button.setIcon(icon);
        button.setText(text);

        return button;
    }

    private Button createDetailButton() {
        Button button = new Button();
        button.getStyle().set("background", "transparent");
        button.getStyle().set("border", "1px solid #e0e0e0");
        button.getStyle().set("border-radius", "6px");
        button.getStyle().set("padding", "6px 10px");
        button.getStyle().set("color", "#333");
        button.getStyle().set("font-size", "12px");
        button.getStyle().set("cursor", "pointer");
        button.getStyle().set("font-weight", "500");

        HorizontalLayout buttonContent = new HorizontalLayout();
        buttonContent.setPadding(false);
        buttonContent.setMargin(false);
        buttonContent.setSpacing(false);
        buttonContent.setAlignItems(Alignment.CENTER);
        buttonContent.getStyle().set("gap", "4px");

        Div detailText = new Div();
        detailText.setText("Detail");

        HorizontalLayout arrowGroup = new HorizontalLayout();
        arrowGroup.setPadding(false);
        arrowGroup.setMargin(false);
        arrowGroup.setSpacing(false);
        arrowGroup.getStyle().set("gap", "1px");

        for (int i = 0; i < 3; i++) {
            Icon arrow = new Icon(VaadinIcon.ANGLE_RIGHT);
            arrow.getStyle().set("color", "#4CAF50");
            arrow.getStyle().set("font-size", "14px");
            arrowGroup.add(arrow);
        }

        buttonContent.add(detailText, arrowGroup);
        button.getElement().removeAllChildren();
        button.getElement().appendChild(buttonContent.getElement());

        return button;
    }

    private String formatDisplayDate(String dbDate) {
        if (dbDate == null) return "N/A";

        try {
            if (dbDate.length() >= 10) {
                String datePart = dbDate.substring(0, 10);
                String[] parts = datePart.split("-");
                if (parts.length == 3) {
                    return parts[2] + "/" + parts[1] + "/" + parts[0];
                }
            }
            return dbDate;
        } catch (Exception e) {
            return dbDate;
        }
    }

    private String calculateTimeFromSeconds(String waktuPengerjaan) {
        if (waktuPengerjaan == null) return "N/A";

        try {
            int totalSeconds = Integer.parseInt(waktuPengerjaan);
            int hours = totalSeconds / 3600;
            int minutes = (totalSeconds % 3600) / 60;
            int seconds = totalSeconds % 60;

            if (hours > 0) {
                return String.format("%02d:%02d:%02d", hours, minutes, seconds);
            } else {
                return String.format("%02d:%02d", minutes, seconds);
            }
        } catch (NumberFormatException e) {
            return waktuPengerjaan;
        }
    }
}