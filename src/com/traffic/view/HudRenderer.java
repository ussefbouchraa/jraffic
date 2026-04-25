package com.traffic.view;

import com.traffic.controller.TrafficController;
import com.traffic.enums.Origin;
import com.traffic.model.Vehicle;
import com.traffic.util.Constants;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

public class HudRenderer {

    public static void draw(GraphicsContext gc, List<Vehicle> vehicles,
                            TrafficController tc, double w, double h) {

        gc.setFill(Color.BLACK.deriveColor(0, 1, 1, 0.55));
        gc.fillRoundRect(10, 10, 220, 155, 12, 12);

        gc.setFont(Font.font("Monospaced", FontWeight.BOLD, 13));


        gc.setFill(Color.WHITE);
        gc.fillText("INTERSECTION SIM", 20, 30);


        gc.setFill(Color.LIGHTGRAY);
        gc.setFont(Font.font("Monospaced", 11));
        gc.fillText("Total vehicles : " + vehicles.size(), 20, 50);


        for (Origin o : Origin.values()) {
            long cnt = vehicles.stream().filter(v -> v.origin == o && !v.turned).count();
            Color bar = cnt == 0 ? Color.GRAY : Color.LIMEGREEN;
            gc.setFill(bar);
            String line = String.format("%-5s : %d / %d", o.name(), cnt, Constants.LANE_CAPACITY);
            gc.fillText(line, 20, 65 + o.ordinal() * 16);
        }


        Origin ag = tc.getActiveGreen();
        gc.setFill(ag != null ? Constants.COLOR_GREEN_LIGHT : Color.GRAY);
        gc.fillText("Green : " + (ag != null ? ag.name() : "—"), 20, 140);


        double progress = tc.getTimerProgress();
        double barW = 200;
        gc.setFill(Color.WHITE.deriveColor(0, 1, 1, 0.15));
        gc.fillRoundRect(20, 148, barW, 8, 4, 4);
        Color barColor = progress < 0.6 ? Color.LIMEGREEN
                       : progress < 0.85 ? Color.ORANGE : Color.TOMATO;
        gc.setFill(barColor);
        gc.fillRoundRect(20, 148, barW * progress, 8, 4, 4);

        // Controls hint
        gc.setFill(Color.WHITE.deriveColor(0, 1, 1, 0.5));
        gc.setFont(Font.font("Monospaced", 9));
        gc.fillText("↑↓←→ spawn   R random   ESC quit", 20, h - 12);

        // Legend
        drawLegend(gc, w);
    }

    private static void drawLegend(GraphicsContext gc, double w) {
        double lx = w - 140;
        gc.setFill(Color.BLACK.deriveColor(0, 1, 1, 0.55));
        gc.fillRoundRect(lx - 5, 10, 130, 70, 10, 10);

        gc.setFont(Font.font("Monospaced", FontWeight.BOLD, 11));
        gc.setFill(Color.WHITE);
        gc.fillText("ROUTE", lx, 28);

        Object[][] rows = {
            {Constants.COLOR_ROUTE_RIGHT,    "Right turn"},
            {Constants.COLOR_ROUTE_LEFT,     "Left turn"},
            {Constants.COLOR_ROUTE_STRAIGHT, "Straight"},
        };
        for (int i = 0; i < rows.length; i++) {
            gc.setFill((Color) rows[i][0]);
            gc.fillRoundRect(lx, 35 + i * 16, 12, 12, 3, 3);
            gc.setFill(Color.LIGHTGRAY);
            gc.setFont(Font.font("Monospaced", 10));
            gc.fillText((String) rows[i][1], lx + 18, 45 + i * 16);
        }
    }
}
