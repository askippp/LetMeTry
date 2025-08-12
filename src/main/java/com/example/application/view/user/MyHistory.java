package com.example.application.view.user;

import com.example.application.components.Header;
import com.example.application.components.SidebarUser;
import com.example.application.model.UsersModel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import static com.vaadin.flow.component.notification.Notification.Position.TOP_CENTER;

@Route("my-history")
@PageTitle("My - History")
public class MyHistory extends VerticalLayout implements BeforeEnterObserver {

    public MyHistory() {}

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
    }

    private VerticalLayout createMainContent() {
        VerticalLayout content = new VerticalLayout();
        content.setPadding(true);
        content.setSpacing(true);
        content.setSizeFull();
        content.getStyle().set("background-color", "#f8f9fa");

        // Title
        H2 title = new H2("Pelajaran");
        title.getStyle().set("margin", "0 0 20px 0");
        title.getStyle().set("color", "#2d5a27");
        title.getStyle().set("font-weight", "bold");

        // Main section dengan tombol kiri dan tabel kanan
        HorizontalLayout mainSection = new HorizontalLayout();
        mainSection.setSizeFull();
        mainSection.setSpacing(true);
        mainSection.setPadding(false);

        // Left side - Subject buttons
        VerticalLayout leftSection = createSubjectButtons();

        // Right side - Table with Vaadin components
        VerticalLayout tableSection = createTableWithButtons();

        mainSection.add(leftSection, tableSection);
        mainSection.setFlexGrow(0, leftSection);
        mainSection.setFlexGrow(1, tableSection);

        content.add(title, mainSection);
        content.setFlexGrow(0, title);
        content.setFlexGrow(1, mainSection);

        return content;
    }

    private VerticalLayout createSubjectButtons() {
        VerticalLayout leftSection = new VerticalLayout();
        leftSection.setPadding(false);
        leftSection.setSpacing(true);
        leftSection.setWidth("200px");
        leftSection.getStyle().set("margin-right", "20px");

        // MTK Button (Active)
        Button mtkButton = new Button("MTK");
        styleActiveSubjectButton(mtkButton);
        mtkButton.setWidthFull();

        // BI Button
        Button biButton = new Button("BI");
        styleInactiveSubjectButton(biButton);
        biButton.setWidthFull();

        // BE Button
        Button beButton = new Button("BE");
        styleInactiveSubjectButton(beButton);
        beButton.setWidthFull();

        // Add click listeners
        mtkButton.addClickListener(e -> switchSubject("MTK", mtkButton, biButton, beButton));
        biButton.addClickListener(e -> switchSubject("BI", biButton, mtkButton, beButton));
        beButton.addClickListener(e -> switchSubject("BE", beButton, mtkButton, biButton));

        leftSection.add(mtkButton, biButton, beButton);
        return leftSection;
    }

    private void styleActiveSubjectButton(Button button) {
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.getStyle().set("background-color", "#4CAF50");
        button.getStyle().set("color", "white");
        button.getStyle().set("border", "3px solid #4CAF50");
        button.getStyle().set("border-radius", "25px");
        button.getStyle().set("height", "60px");
        button.getStyle().set("font-weight", "700");
        button.getStyle().set("font-size", "18px");
        button.getStyle().set("box-shadow", "0 4px 12px rgba(76, 175, 80, 0.3)");
    }

    private void styleInactiveSubjectButton(Button button) {
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        button.getStyle().set("background-color", "white");
        button.getStyle().set("color", "#4CAF50");
        button.getStyle().set("border", "3px solid #4CAF50");
        button.getStyle().set("border-radius", "25px");
        button.getStyle().set("height", "60px");
        button.getStyle().set("font-weight", "700");
        button.getStyle().set("font-size", "18px");
        button.getStyle().set("box-shadow", "0 2px 8px rgba(0, 0, 0, 0.1)");
    }

    private void switchSubject(String subject, Button activeButton, Button... inactiveButtons) {
        // Reset all buttons to inactive
        for (Button button : inactiveButtons) {
            button.removeThemeVariants(ButtonVariant.LUMO_PRIMARY);
            styleInactiveSubjectButton(button);
        }

        // Set active button
        activeButton.removeThemeVariants(ButtonVariant.LUMO_TERTIARY);
        styleActiveSubjectButton(activeButton);

        Notification.show("Switched to " + subject, 2000, TOP_CENTER);
    }

    private VerticalLayout createTableWithButtons() {
        VerticalLayout tableContainer = new VerticalLayout();
        tableContainer.setSizeFull();
        tableContainer.setPadding(false);
        tableContainer.setSpacing(false);
        tableContainer.getStyle().set("border", "3px solid #4CAF50");
        tableContainer.getStyle().set("border-radius", "15px");
        tableContainer.getStyle().set("overflow", "hidden");
        tableContainer.getStyle().set("background", "white");

        // Header
        HorizontalLayout headerRow = new HorizontalLayout();
        headerRow.setWidthFull();
        headerRow.setHeight("60px");
        headerRow.setPadding(false);
        headerRow.setMargin(false);
        headerRow.setSpacing(false);
        headerRow.getStyle().set("background-color", "#4CAF50");

        Div tanggalHeader = createHeaderCell("Tanggal");
        Div levelHeader = createHeaderCell("Level");
        Div skorHeader = createHeaderCell("Skor");
        Div persentaseHeader = createHeaderCell("Persentase");

        headerRow.add(tanggalHeader, levelHeader, skorHeader, persentaseHeader);

        // Data rows
        VerticalLayout dataContainer = new VerticalLayout();
        dataContainer.setPadding(false);
        dataContainer.setMargin(false);
        dataContainer.setSpacing(false);
        dataContainer.setWidthFull();
        dataContainer.getStyle().set("flex", "1");

        // Row 1
        HorizontalLayout row1 = createDataRow("10/07/2025", "Easy", "80", 80, "10/07/2025", "Easy");
        // Row 2
        HorizontalLayout row2 = createDataRow("12/07/2025", "Easy", "100", 100, "12/07/2025", "Easy");
        // Row 3
        HorizontalLayout row3 = createDataRow("15/07/2025", "Medium", "75", 75, "15/07/2025", "Medium");

        dataContainer.add(row1, row2, row3);

        tableContainer.add(headerRow, dataContainer);
        tableContainer.setFlexGrow(0, headerRow);
        tableContainer.setFlexGrow(1, dataContainer);

        return tableContainer;
    }

    private Div createHeaderCell(String text) {
        Div cell = new Div();
        cell.setText(text);
        cell.getStyle().set("display", "flex");
        cell.getStyle().set("align-items", "center");
        cell.getStyle().set("justify-content", "center");
        cell.getStyle().set("color", "white");
        cell.getStyle().set("font-weight", "bold");
        cell.getStyle().set("font-size", "16px");
        cell.getStyle().set("height", "100%");
        cell.getStyle().set("width", "25%");
        if (!text.equals("Persentase")) {
            cell.getStyle().set("border-right", "2px solid #2E7D32");
        }
        return cell;
    }

    private HorizontalLayout createDataRow(String tanggal, String level, String skor, int percentage, String originalDate, String originalLevel) {
        HorizontalLayout row = new HorizontalLayout();
        row.setWidthFull();
        row.setHeight("70px");
        row.setPadding(false);
        row.setMargin(false);
        row.setSpacing(false);
        row.getStyle().set("border-bottom", "1px solid #e0e0e0");

        Div tanggalCell = createDataCell(tanggal, false);
        Div levelCell = createDataCell(level, false);
        Div skorCell = createDataCell(skor, false);
        Div persentaseCell = createPercentageButtonCell(percentage, originalDate, originalLevel, skor);

        row.add(tanggalCell, levelCell, skorCell, persentaseCell);

        return row;
    }

    private Div createDataCell(String text, boolean isLast) {
        Div cell = new Div();
        cell.setText(text);
        cell.getStyle().set("display", "flex");
        cell.getStyle().set("align-items", "center");
        cell.getStyle().set("justify-content", "center");
        cell.getStyle().set("color", "#333");
        cell.getStyle().set("font-size", "16px");
        cell.getStyle().set("height", "100%");
        cell.getStyle().set("width", "25%");
        if (!isLast) {
            cell.getStyle().set("border-right", "2px solid #4CAF50");
        }
        return cell;
    }

    private Div createPercentageButtonCell(int percentage, String date, String level, String skor) {
        Div cell = new Div();
        cell.getStyle().set("display", "flex");
        cell.getStyle().set("align-items", "center");
        cell.getStyle().set("justify-content", "center");
        cell.getStyle().set("height", "100%");
        cell.getStyle().set("width", "25%");

        // Create percentage button with progress bar styling
        Button percentageButton = new Button();
        percentageButton.getStyle().set("width", "120px");
        percentageButton.getStyle().set("height", "40px");
        percentageButton.getStyle().set("border-radius", "20px");
        percentageButton.getStyle().set("border", "none");
        percentageButton.getStyle().set("font-size", "16px");
        percentageButton.getStyle().set("font-weight", "600");
        percentageButton.getStyle().set("cursor", "pointer");
        percentageButton.getStyle().set("position", "relative");
        percentageButton.getStyle().set("overflow", "hidden");

        // Set background gradient based on percentage
        if (percentage >= 80) {
            percentageButton.getStyle().set("background", "linear-gradient(to right, #4CAF50 " + percentage + "%, #ff6b6b " + percentage + "%)");
            percentageButton.getStyle().set("color", "white");
        } else {
            percentageButton.getStyle().set("background", "linear-gradient(to right, #4CAF50 " + percentage + "%, #ff6b6b " + percentage + "%)");
            percentageButton.getStyle().set("color", "white");
        }

        percentageButton.setText(percentage + "%");

        // Add click listener
        percentageButton.addClickListener(e -> {
            // Format parameter: subject-date-level-score-percentage
            String dateParam = date.replace("/", "_"); // Replace / with _ for URL safety
            String parameter = "MTK-" + dateParam + "-" + level + "-" + skor + "-" + percentage;
            percentageButton.getUI().ifPresent(ui -> ui.navigate("result-detail/" + parameter));
        });

        cell.add(percentageButton);
        return cell;
    }
}