package com.example.application.components;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.dom.Style;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ClockView extends HorizontalLayout {

    private Span clockLabel;
    private boolean isRunning = false;

    public ClockView() {
        Image clockImage = new Image("images/jam.png", "Jam");
        clockImage.getStyle()
                .setWidth("40px")
                .setHeight("40px");

        clockLabel = new Span();
        clockLabel.getStyle()
                .setMarginLeft("-10px")
                .setFontSize("1.3rem")
                .setFontWeight("bold")
                .setColor("#1f2937");
        clockLabel.setId("clock-display");

        HorizontalLayout clockSection = new HorizontalLayout();
        clockSection.getStyle()
                        .setAlignItems(Style.AlignItems.CENTER);
        clockSection.add(clockImage, clockLabel);

        // Set initial time immediately
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        clockLabel.setText(LocalTime.now().format(formatter));

        // Menambahkan komponen langsung ke layout
        add(clockSection);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        startJavaScriptClock();
        isRunning = true;
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        stopJavaScriptClock();
        isRunning = false;
    }

    private void startJavaScriptClock() {
        String script = """
            if (window.clockInterval) {
                clearInterval(window.clockInterval);
            }
            
            window.clockInterval = setInterval(function() {
                const now = new Date();
                const hours = String(now.getHours()).padStart(2, '0');
                const minutes = String(now.getMinutes()).padStart(2, '0');
                const seconds = String(now.getSeconds()).padStart(2, '0');
                const timeString = hours + ':' + minutes + ':' + seconds;
                
                const clockElement = document.getElementById('clock-display');
                if (clockElement) {
                    clockElement.textContent = timeString;
                }
            }, 1000);
            """;

        getElement().executeJs(script);
    }

    private void stopJavaScriptClock() {
        String script = """
            if (window.clockInterval) {
                clearInterval(window.clockInterval);
                window.clockInterval = null;
            }
            """;

        getElement().executeJs(script);
    }
}