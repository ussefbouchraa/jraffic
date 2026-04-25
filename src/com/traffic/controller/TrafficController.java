package com.traffic.controller;

import com.traffic.enums.Origin;
import com.traffic.model.Vehicle;
import com.traffic.util.Constants;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

public class TrafficController {

    private Origin activeGreen = null;
    private double greenTimer  = 0.0;
    private boolean forcedSwitch = false;

    private static final Origin[] CYCLE = {Origin.NORTH, Origin.SOUTH, Origin.EAST, Origin.WEST};

    private double blinkTimer = 0.0;
    private boolean blinkOn   = true;

    public void update(double dt, List<Vehicle> vehicles, double cx, double cy) {
        greenTimer += dt;
        blinkTimer += dt;
        if (blinkTimer > 0.5) { blinkOn = !blinkOn; blinkTimer = 0; }

        long centerCount = vehicles.stream()
            .filter(v -> Math.abs(v.x - cx) < Constants.CENTER_HALF
                      && Math.abs(v.y - cy) < Constants.CENTER_HALF)
            .count();
        boolean centerEmpty = centerCount == 0;

        boolean minElapsed = greenTimer >= Constants.MIN_GREEN_TIME;
        boolean maxElapsed = greenTimer >= Constants.MAX_GREEN_TIME;

        boolean shouldSwitch = activeGreen == null
            || (minElapsed && centerEmpty)
            || maxElapsed;

        if (shouldSwitch) {
            forcedSwitch = maxElapsed;
            switchLight(vehicles);
        }
    }

    private void switchLight(List<Vehicle> vehicles) {
        long nC = count(vehicles, Origin.NORTH);
        long sC = count(vehicles, Origin.SOUTH);
        long eC = count(vehicles, Origin.EAST);
        long wC = count(vehicles, Origin.WEST);

        Origin best = null;

        if (forcedSwitch && activeGreen != null) {
            int currentIdx = 0;
            for (int i = 0; i < CYCLE.length; i++) {
                if (CYCLE[i] == activeGreen) { currentIdx = i; break; }
            }
            for (int i = 1; i <= CYCLE.length; i++) {
                Origin candidate = CYCLE[(currentIdx + i) % CYCLE.length];
                if (countFor(candidate, nC, sC, eC, wC) > 0) {
                    best = candidate;
                    break;
                }
            }
            if (best == null && countFor(activeGreen, nC, sC, eC, wC) > 0) {
                best = activeGreen;
            }
        } else {
            double bestScore = -1;
            for (Origin o : CYCLE) {
                if (o == activeGreen) continue; // never re-pick current on normal switch
                long c = countFor(o, nC, sC, eC, wC);
                if (c == 0) continue;
                double score = (double) c / Constants.LANE_CAPACITY;
                if (score > bestScore) { bestScore = score; best = o; }
            }
            if (best == null && activeGreen != null
                    && countFor(activeGreen, nC, sC, eC, wC) > 0) {
                best = activeGreen;
            }
        }

        activeGreen  = best;
        greenTimer   = 0.0;
        forcedSwitch = false;
    }

    private long countFor(Origin o, long nC, long sC, long eC, long wC) {
        return switch (o) {
            case NORTH -> nC;
            case SOUTH -> sC;
            case EAST  -> eC;
            case WEST  -> wC;
        };
    }

    private long count(List<Vehicle> vehicles, Origin origin) {
        return vehicles.stream()
            .filter(v -> v.origin == origin && !v.turned)
            .count();
    }

    public Origin getActiveGreen() { return activeGreen; }

    public double getTimerProgress() {
        return Math.min(greenTimer / Constants.MAX_GREEN_TIME, 1.0);
    }

    public double getRemainingSeconds() {
        return Math.max(0, Constants.MAX_GREEN_TIME - greenTimer);
    }

    // ── Drawing ──────────────────────────────────────────────────────────────

    public void draw(GraphicsContext gc, double cx, double cy) {
        drawLight(gc, cx - Constants.LIGHT_OFFSET, cy - Constants.LIGHT_OFFSET, Origin.NORTH);
        drawLight(gc, cx + Constants.LANE_OFFSET,  cy + Constants.LANE_OFFSET,  Origin.SOUTH);
        drawLight(gc, cx - Constants.LIGHT_OFFSET, cy + Constants.LANE_OFFSET,  Origin.EAST);
        drawLight(gc, cx + Constants.LANE_OFFSET,  cy - Constants.LIGHT_OFFSET, Origin.WEST);

        if (activeGreen != null) {
            double[] pos = lightPos(cx, cy, activeGreen);
            drawTimerArc(gc, pos[0], pos[1], getTimerProgress());
        }
    }

    private void drawLight(GraphicsContext gc, double x, double y, Origin origin) {
        double pad  = 5;
        double size = Constants.LIGHT_SIZE;
        boolean isGreen = activeGreen == origin;

        gc.setFill(Constants.COLOR_DARKGRAY);
        gc.fillRoundRect(x - pad, y - pad, size + pad * 2, size + pad * 2, 8, 8);

        Color base  = isGreen ? Constants.COLOR_GREEN_LIGHT : Constants.COLOR_RED_LIGHT;
        Color drawn = (isGreen && blinkOn) ? base : (isGreen ? base.deriveColor(0, 1, 0.6, 1) : base);
        gc.setFill(drawn);
        gc.fillOval(x, y, size, size);

        gc.setFill(Color.WHITE.deriveColor(0, 1, 1, 0.25));
        gc.fillOval(x + size * 0.15, y + size * 0.1, size * 0.35, size * 0.3);

        String label = switch (origin) {
            case NORTH -> "N"; case SOUTH -> "S";
            case EAST  -> "E"; case WEST  -> "W";
        };
        gc.setFill(Color.WHITE.deriveColor(0, 1, 1, 0.7));
        gc.setFont(javafx.scene.text.Font.font("Monospaced", 9));
        gc.fillText(label, x + size / 2 - 3, y + size + 10);
    }

    private void drawTimerArc(GraphicsContext gc, double x, double y, double progress) {
        double size = Constants.LIGHT_SIZE;
        double pad  = 5;
        double r    = size / 2 + pad + 2;
        double cx   = x + size / 2;
        double cy2  = y + size / 2;

        gc.setStroke(Color.WHITE.deriveColor(0, 1, 1, 0.15));
        gc.setLineWidth(3);
        gc.strokeOval(cx - r, cy2 - r, r * 2, r * 2);

        double remaining = 1.0 - progress;
        if (remaining > 0) {
            gc.setStroke(Color.LIMEGREEN.deriveColor(0, 1, 1, 0.85));
            gc.setLineWidth(3);
            gc.strokeArc(cx - r, cy2 - r, r * 2, r * 2,
                         90, remaining * 360,
                         javafx.scene.shape.ArcType.OPEN);
        }
    }

    private double[] lightPos(double cx, double cy, Origin origin) {
        return switch (origin) {
            case NORTH -> new double[]{cx - Constants.LIGHT_OFFSET, cy - Constants.LIGHT_OFFSET};
            case SOUTH -> new double[]{cx + Constants.LANE_OFFSET,  cy + Constants.LANE_OFFSET};
            case EAST  -> new double[]{cx - Constants.LIGHT_OFFSET, cy + Constants.LANE_OFFSET};
            case WEST  -> new double[]{cx + Constants.LANE_OFFSET,  cy - Constants.LIGHT_OFFSET};
        };
    }
}