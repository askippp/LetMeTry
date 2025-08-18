package com.example.application.components;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class Timer extends HorizontalLayout {

    private final Span timerLabel;
    private boolean isRunning = false;
    private int initialSeconds; // waktu awal dalam detik
    private int remainingSeconds; // waktu sisa saat ini
    private Runnable onTimeUp; // callback ketika waktu habis

    public Timer(int initialSeconds) {
        this.initialSeconds = initialSeconds;
        this.remainingSeconds = initialSeconds;

        timerLabel = new Span();
        timerLabel.getStyle()
                .setFontSize("1.3rem")
                .setFontWeight("bold")
                .setColor("#1f2937");
        timerLabel.setId("timer-display");

        add(timerLabel);

        updateLabel(initialSeconds);
    }

    public void setOnTimeUpCallback(Runnable onTimeUp) {
        this.onTimeUp = onTimeUp;
    }

    private void updateLabel(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        timerLabel.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        startJavaScriptTimer(remainingSeconds);
        isRunning = true;
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        stopJavaScriptTimer();
        isRunning = false;
    }

    @ClientCallable
    public void handleTimeUp() {
        if (onTimeUp != null) {
            onTimeUp.run();
        }
    }

    @ClientCallable
    public void updateRemainingTime(int remaining) {
        this.remainingSeconds = remaining;
    }

    public int getRemainingSeconds() {
        return remainingSeconds;
    }

    public int getElapsedSeconds() {
        return initialSeconds - remainingSeconds;
    }

    private void startJavaScriptTimer(int seconds) {
        String script = """
            if (window.timerInterval) {
                clearInterval(window.timerInterval);
            }

            let remaining = %d;
            const component = this;
            
            function updateTimer() {
                if (remaining < 0) {
                    clearInterval(window.timerInterval);
                    component.$server.handleTimeUp();
                    return;
                }
                
                const hrs = String(Math.floor(remaining / 3600)).padStart(2, '0');
                const mins = String(Math.floor((remaining %% 3600) / 60)).padStart(2, '0');
                const secs = String(remaining %% 60).padStart(2, '0');
                const timeString = hrs + ':' + mins + ':' + secs;
                const timerElement = document.getElementById('timer-display');
                if (timerElement) {
                    timerElement.textContent = timeString;
                }
                
                // Update remaining time di server
                component.$server.updateRemainingTime(remaining);
                remaining--;
            }
            
            updateTimer();
            window.timerInterval = setInterval(updateTimer, 1000);
            """.formatted(seconds);

        getElement().executeJs(script);
    }

    private void stopJavaScriptTimer() {
        String script = """
            if (window.timerInterval) {
                clearInterval(window.timerInterval);
                window.timerInterval = null;
            }
            """;

        getElement().executeJs(script);
    }
}