package com.example.application.view.user;

import com.example.application.components.Header;
import com.example.application.components.SidebarUser;
import com.example.application.dao.HistoryDAO;
import com.example.application.model.HistoryModel;
import com.example.application.model.UsersModel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.vaadin.flow.component.notification.Notification.Position.TOP_CENTER;

@Route("my-history")
@PageTitle("My - History")
public class MyHistory extends VerticalLayout implements BeforeEnterObserver {

    private String currentSubject = "MTK";
    private int currentIdMapel = 1; // Default MTK
    private VerticalLayout tableContainer;
    private UsersModel currentUser;

    public MyHistory() {}

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        UsersModel user = VaadinSession.getCurrent().getAttribute(UsersModel.class);
        if (user == null) {
            event.forwardTo("login");
            Notification.show("Kamu Harus Login Terlebih dahulu!", 2000, TOP_CENTER);
        } else {
            this.currentUser = user;
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
        H2 title = new H2("Riwayat Pelajaran");
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

        // Right side - Table with database data
        tableContainer = createTableWithDatabaseData();

        mainSection.add(leftSection, tableContainer);
        mainSection.setFlexGrow(0, leftSection);
        mainSection.setFlexGrow(1, tableContainer);

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

        // MTK Button (Active by default)
        Button mtkButton = new Button("MTK");
        styleActiveSubjectButton(mtkButton);
        mtkButton.setWidthFull();

        // BI Button
        Button biButton = new Button("BI");
        styleInactiveSubjectButton(biButton);
        biButton.setWidthFull();

        // BE Button (Bahasa Inggris)
        Button beButton = new Button("BE");
        styleInactiveSubjectButton(beButton);
        beButton.setWidthFull();

        // Add click listeners
        mtkButton.addClickListener(e -> switchSubject("MTK", 1, mtkButton, biButton, beButton));
        biButton.addClickListener(e -> switchSubject("BI", 3, biButton, mtkButton, beButton));
        beButton.addClickListener(e -> switchSubject("BE", 2, beButton, mtkButton, biButton));

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

    private void switchSubject(String subject, int idMapel, Button activeButton, Button... inactiveButtons) {
        // Reset all buttons to inactive
        for (Button button : inactiveButtons) {
            button.removeThemeVariants(ButtonVariant.LUMO_PRIMARY);
            styleInactiveSubjectButton(button);
        }

        // Set active button
        activeButton.removeThemeVariants(ButtonVariant.LUMO_TERTIARY);
        styleActiveSubjectButton(activeButton);

        // Update current subject and reload data
        this.currentSubject = subject;
        this.currentIdMapel = idMapel;

        // Refresh table with new data
        refreshTableData();
    }

    private void refreshTableData() {
        if (tableContainer != null) {
            tableContainer.removeAll();
            VerticalLayout newTable = createTableWithDatabaseData();

            newTable.setSizeFull();
            newTable.setPadding(false);
            newTable.setSpacing(false);
            newTable.getStyle().set("border", "3px solid #4CAF50");
            newTable.getStyle().set("border-radius", "15px");
            newTable.getStyle().set("overflow", "hidden");
            newTable.getStyle().set("background", "white");

            tableContainer.add(newTable);
        }
    }

    private VerticalLayout createTableWithDatabaseData() {
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

        // Data container
        VerticalLayout dataContainer = new VerticalLayout();
        dataContainer.setPadding(false);
        dataContainer.setMargin(false);
        dataContainer.setSpacing(false);
        dataContainer.setWidthFull();
        dataContainer.getStyle().set("flex", "1");

        // Load data from database
        try {
            HistoryDAO historyDAO = new HistoryDAO();
            List<HistoryModel> historyList = historyDAO.getHistoryByUserAndMapel(
                    currentUser.getIdUsers(), currentIdMapel);

            if (historyList.isEmpty()) {
                // Show no data message
                Div noDataRow = new Div();
                noDataRow.setText("Belum ada riwayat tryout untuk mata pelajaran " + currentSubject);
                noDataRow.getStyle().set("text-align", "center");
                noDataRow.getStyle().set("padding", "40px");
                noDataRow.getStyle().set("color", "#666");
                noDataRow.getStyle().set("font-style", "italic");
                dataContainer.add(noDataRow);
            } else {
                // Add data rows
                for (HistoryModel history : historyList) {
                    HorizontalLayout dataRow = createDataRowFromHistory(history);
                    dataContainer.add(dataRow);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Div errorRow = new Div();
            errorRow.setText("Gagal mengambil data riwayat");
            errorRow.getStyle().set("text-align", "center");
            errorRow.getStyle().set("padding", "40px");
            errorRow.getStyle().set("color", "#f44336");
            dataContainer.add(errorRow);
        }

        tableContainer.add(headerRow, dataContainer);
        tableContainer.setFlexGrow(0, headerRow);
        tableContainer.setFlexGrow(1, dataContainer);

        return tableContainer;
    }

    private HorizontalLayout createDataRowFromHistory(HistoryModel history) {
        HorizontalLayout row = new HorizontalLayout();
        row.setWidthFull();
        row.setHeight("70px");
        row.setPadding(false);
        row.setMargin(false);
        row.setSpacing(false);
        row.getStyle().set("border-bottom", "1px solid #e0e0e0");

        // Format tanggal
        String formattedDate = formatDate(history.getTanggal());

        Div tanggalCell = createDataCell(formattedDate, false);
        Div levelCell = createDataCell(history.getLevel(), false);
        Div skorCell = createDataCell(String.valueOf(history.getNilai()), false);
        Div persentaseCell = createPercentageButtonCell(
                history.getPersentase(),
                formattedDate,
                history.getLevel(),
                String.valueOf(history.getNilai()),
                history.getIdTryout()
        );

        row.add(tanggalCell, levelCell, skorCell, persentaseCell);

        return row;
    }

    private String formatDate(String dbDate) {
        try {
            // Assuming database returns date in format like "2025-07-10" or "2025-07-10 10:30:00"
            if (dbDate != null && dbDate.length() >= 10) {
                String datePart = dbDate.substring(0, 10); // Get YYYY-MM-DD part
                String[] parts = datePart.split("-");
                if (parts.length == 3) {
                    return parts[2] + "/" + parts[1] + "/" + parts[0]; // DD/MM/YYYY
                }
            }
            return dbDate;
        } catch (Exception e) {
            return dbDate;
        }
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

    private Div createPercentageButtonCell(int percentage, String date, String level, String skor, int idTryout) {
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

        if (percentage >= 80) {
            percentageButton.getStyle().set("background", "linear-gradient(to right, #4CAF50 " + percentage + "%, #ff6b6b " + percentage + "%)");
            percentageButton.getStyle().set("color", "white");
        } else {
            percentageButton.getStyle().set("background", "linear-gradient(to right, #4CAF50 " + percentage + "%, #ff6b6b " + percentage + "%)");
            percentageButton.getStyle().set("color", "white");
        }

        percentageButton.setText(percentage + "%");

        percentageButton.addClickListener(e -> {
            String dateParam = date.replace("/", "_"); // Replace / with _ for URL safety
            String parameter = currentSubject + "-" + dateParam + "-" + level + "-" + skor + "-" + percentage + "-" + idTryout;
            percentageButton.getUI().ifPresent(ui -> ui.navigate("result-detail/" + parameter));
        });

        cell.add(percentageButton);
        return cell;
    }
}