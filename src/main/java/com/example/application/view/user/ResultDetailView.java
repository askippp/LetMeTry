package com.example.application.view.user;

import com.example.application.components.Header;
import com.example.application.components.SidebarUser;
import com.example.application.model.UsersModel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;

import java.util.List;
import java.util.Map;

import static com.vaadin.flow.component.notification.Notification.Position.TOP_CENTER;

@Route("result-detail")
@PageTitle("Detail Hasil - Quiz")
public class ResultDetailView extends VerticalLayout implements BeforeEnterObserver, HasUrlParameter<String> {

    private String subject;
    private String date;
    private String level;
    private int score;
    private int percentage;
    private int totalQuestions;
    private int correctAnswers;
    private int wrongAnswers;

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
        // Format: subject-date-level-score-percentage
        if (parameter != null && !parameter.isEmpty()) {
            String[] parts = parameter.split("-");
            if (parts.length >= 5) {
                this.subject = parts[0];
                this.date = parts[1].replace("_", "/");
                this.level = parts[2];
                this.score = Integer.parseInt(parts[3]);
                this.percentage = Integer.parseInt(parts[4]);

                // Calculate questions based on score and percentage
                this.totalQuestions = (int) Math.round((double) score * 100 / percentage);
                this.correctAnswers = (int) Math.round((double) totalQuestions * percentage / 100);
                this.wrongAnswers = totalQuestions - correctAnswers;
            }
        }

        // Default values jika parameter tidak ada
        if (this.subject == null) {
            this.subject = "MTK";
            this.date = "12/07/2025";
            this.level = "Easy";
            this.score = 80;
            this.percentage = 80;
            this.totalQuestions = 30;
            this.correctAnswers = 23;
            this.wrongAnswers = 7;
        }
    }

    private void buildLayout() {
        setPadding(false);
        setMargin(false);
        setSpacing(false);
        setSizeFull();
        // Set overflow hidden untuk main container
        getStyle().set("overflow", "hidden");

        HorizontalLayout header = Header.headerWithLogo();

        HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.setSizeFull();
        mainLayout.setPadding(false);
        mainLayout.setMargin(false);
        mainLayout.setSpacing(false);
        // Set overflow hidden untuk main layout
        mainLayout.getStyle().set("overflow", "hidden");

        VerticalLayout sidebar = SidebarUser.createSidebar();
        // Fix sidebar agar tidak bisa scroll dan tetap di posisi
        sidebar.getStyle().set("overflow", "hidden");
        sidebar.getStyle().set("position", "fixed");
        sidebar.getStyle().set("height", "calc(100vh - 60px)"); // Adjust based on header height
        sidebar.getStyle().set("z-index", "1000");

        // Create scrollable main content
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
        // Set margin-left untuk memberikan ruang untuk sidebar
        scrollableWrapper.getStyle().set("margin-left", "250px"); // Adjust based on sidebar width
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
        content.getStyle().set("padding-top", "20px"); // Menaikkan posisi content

        // Questions detail section (removed back button and title)
        VerticalLayout detailSection = createQuestionDetails();

        content.add(detailSection);

        return content;
    }

    private VerticalLayout createQuestionDetails() {
        VerticalLayout section = new VerticalLayout();
        section.setPadding(false);
        section.setSpacing(true);
        section.getStyle().set("margin-top", "30px");

        H3 detailTitle = new H3("Rincian Soal");
        detailTitle.getStyle().set("color", "#2d5a27");
        detailTitle.getStyle().set("margin-bottom", "20px");

        // Create containers for correct and wrong answers
        VerticalLayout questionsContainer = new VerticalLayout();
        questionsContainer.setPadding(false);
        questionsContainer.setSpacing(true);
        questionsContainer.getStyle().set("max-width", "900px");

        // Correct answers card
        if (correctAnswers > 0) {
            Div correctCard = createQuestionTypeCard("BENAR", "Soal Benar",
                    "Try Out TKA Matematika", correctAnswers, "#4CAF50");
            questionsContainer.add(correctCard);
        }

        // Wrong answers card
        if (wrongAnswers > 0) {
            Div wrongCard = createQuestionTypeCard("SALAH", "Soal Salah",
                    "Try Out TKA Matematika", wrongAnswers, "#f44336");
            questionsContainer.add(wrongCard);
        }

        section.add(detailTitle, questionsContainer);
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

        // Status header bar (kotak panjang BENAR/SALAH)
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

        // Left side - Number in outlined square box
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
        numberText.getStyle().set("line-height", "1");

        numberContainer.add(numberText);

        // Middle section - Content
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
        titleDiv.getStyle().set("font-weight", "400");

        Div subtitleDiv = new Div();
        subtitleDiv.setText(subtitle);
        subtitleDiv.getStyle().set("font-weight", "bold");
        subtitleDiv.getStyle().set("font-size", "16px");
        subtitleDiv.getStyle().set("color", "#333");
        subtitleDiv.getStyle().set("margin-bottom", "12px");
        subtitleDiv.getStyle().set("text-decoration", "underline");

        // Info row with compact outlined buttons
        HorizontalLayout infoRow = new HorizontalLayout();
        infoRow.setAlignItems(Alignment.CENTER);
        infoRow.setSpacing(true);
        infoRow.setPadding(false);
        infoRow.setMargin(false);
        infoRow.getStyle().set("gap", "8px");

        // Date info as compact outlined button
        Button dateButton = new Button();
        dateButton.getStyle().set("background", "transparent");
        dateButton.getStyle().set("border", "1px solid #e0e0e0");
        dateButton.getStyle().set("border-radius", "6px");
        dateButton.getStyle().set("padding", "6px 10px");
        dateButton.getStyle().set("color", "#666");
        dateButton.getStyle().set("font-size", "12px");
        dateButton.getStyle().set("cursor", "default");
        dateButton.getStyle().set("display", "flex");
        dateButton.getStyle().set("align-items", "center");
        dateButton.getStyle().set("gap", "4px");

        Icon calendarIcon = new Icon(VaadinIcon.CALENDAR);
        calendarIcon.getStyle().set("color", "#4CAF50");
        calendarIcon.getStyle().set("font-size", "14px");

        dateButton.setIcon(calendarIcon);
        dateButton.setText(date);

        // Time info as compact outlined button
        Button timeButton = new Button();
        timeButton.getStyle().set("background", "transparent");
        timeButton.getStyle().set("border", "1px solid #e0e0e0");
        timeButton.getStyle().set("border-radius", "6px");
        timeButton.getStyle().set("padding", "6px 10px");
        timeButton.getStyle().set("color", "#666");
        timeButton.getStyle().set("font-size", "12px");
        timeButton.getStyle().set("cursor", "default");
        timeButton.getStyle().set("display", "flex");
        timeButton.getStyle().set("align-items", "center");
        timeButton.getStyle().set("gap", "4px");

        Icon clockIcon = new Icon(VaadinIcon.CLOCK);
        clockIcon.getStyle().set("color", "#4CAF50");
        clockIcon.getStyle().set("font-size", "14px");

        timeButton.setIcon(clockIcon);
        timeButton.setText("07:00 WIB");

        // Detail info as compact outlined button with arrow icons
        Button detailButton = new Button();
        detailButton.getStyle().set("background", "transparent");
        detailButton.getStyle().set("border", "1px solid #e0e0e0");
        detailButton.getStyle().set("border-radius", "6px");
        detailButton.getStyle().set("padding", "6px 10px");
        detailButton.getStyle().set("color", "#333");
        detailButton.getStyle().set("font-size", "12px");
        detailButton.getStyle().set("cursor", "pointer");
        detailButton.getStyle().set("display", "flex");
        detailButton.getStyle().set("align-items", "center");
        detailButton.getStyle().set("gap", "4px");
        detailButton.getStyle().set("font-weight", "500");

        // Create content for detail button (text + arrows)
        HorizontalLayout buttonContent = new HorizontalLayout();
        buttonContent.setPadding(false);
        buttonContent.setMargin(false);
        buttonContent.setSpacing(false);
        buttonContent.setAlignItems(Alignment.CENTER);
        buttonContent.getStyle().set("gap", "4px");

        Div detailText = new Div();
        detailText.setText("Detail");

        // Arrow icons inside the button
        HorizontalLayout arrowGroup = new HorizontalLayout();
        arrowGroup.setPadding(false);
        arrowGroup.setMargin(false);
        arrowGroup.setSpacing(false);
        arrowGroup.getStyle().set("gap", "1px");

        Icon arrow1 = new Icon(VaadinIcon.ANGLE_RIGHT);
        Icon arrow2 = new Icon(VaadinIcon.ANGLE_RIGHT);
        Icon arrow3 = new Icon(VaadinIcon.ANGLE_RIGHT);

        arrow1.getStyle().set("color", "#4CAF50");
        arrow2.getStyle().set("color", "#4CAF50");
        arrow3.getStyle().set("color", "#4CAF50");

        arrow1.getStyle().set("font-size", "14px");
        arrow2.getStyle().set("font-size", "14px");
        arrow3.getStyle().set("font-size", "14px");

        arrowGroup.add(arrow1, arrow2, arrow3);
        buttonContent.add(detailText, arrowGroup);

        detailButton.getElement().removeAllChildren();
        detailButton.getElement().appendChild(buttonContent.getElement());

        infoRow.add(dateButton, timeButton, detailButton);

        contentSection.add(titleDiv, subtitleDiv, infoRow);

        mainContent.add(numberContainer, contentSection);
        mainContent.setFlexGrow(0, numberContainer);
        mainContent.setFlexGrow(1, contentSection);

        card.add(statusHeader, mainContent);

        // Add click listener to detail button
        detailButton.addClickListener(e -> {
            Notification.show("Detail " + title + " - " + count + " soal", 2000, TOP_CENTER);
        });

        // Add hover effect
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
}