package com.traffic.model;

import com.traffic.enums.Origin;
import com.traffic.enums.Route;
import com.traffic.util.Constants;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Vehicle {

    private static final AtomicInteger ID_COUNTER = new AtomicInteger(0);

    public final int id;
    public double x, y;
    public double vx, vy;
    public final Origin origin;
    public final Route route;
    public final Color color;
    public boolean turned = false;

    private double pulsePhase;

    public Vehicle(double x, double y, double vx, double vy, Origin origin, Route route, Color color) {
        this.id     = ID_COUNTER.getAndIncrement();
        this.x      = x;
        this.y      = y;
        this.vx     = vx;
        this.vy     = vy;
        this.origin = origin;
        this.route  = route;
        this.color  = color;
        this.pulsePhase = Math.random() * Math.PI * 2;
    }

    public void draw(GraphicsContext gc, double time) {
        pulsePhase += 0.05;
        double size = Constants.CAR_SIZE;


        gc.setFill(color.deriveColor(0, 1, 0.5, 0.3));
        gc.fillRoundRect(x - size / 2 - 3, y - size / 2 - 3, size + 6, size + 6, 6, 6);


        gc.setFill(color);
        gc.fillRoundRect(x - size / 2, y - size / 2, size, size, 5, 5);


        gc.setFill(Color.WHITE.deriveColor(0, 1, 1, 0.35));
        gc.fillRoundRect(x - size / 2 + 3, y - size / 2 + 3, size - 6, size / 2 - 2, 3, 3);


        Color dotColor = switch (route) {
            case RIGHT    -> Color.ORANGE;
            case LEFT     -> Color.DEEPSKYBLUE;
            case STRAIGHT -> Color.LIMEGREEN;
        };
        gc.setFill(dotColor);
        gc.fillOval(x - 3, y - 3, 6, 6);
    }

    public void update(double cx, double cy) {
        x += vx;
        y += vy;
        if (!turned) {
            updateTurning(cx, cy);
        }
    }

    public boolean shouldDespawn(double w, double h) {
        return x < -Constants.DESPAWN_OFFSET || x > w + Constants.DESPAWN_OFFSET
            || y < -Constants.DESPAWN_OFFSET || y > h + Constants.DESPAWN_OFFSET;
    }

    private void updateTurning(double cx, double cy) {
        double s = Constants.CAR_SPEED;
        switch (route) {
            case STRAIGHT -> {
                boolean passed = switch (origin) {
                    case SOUTH -> y < cy - Constants.LANE_OFFSET;
                    case NORTH -> y > cy + Constants.LANE_OFFSET;
                    case EAST  -> x > cx + Constants.LANE_OFFSET;
                    case WEST  -> x < cx - Constants.LANE_OFFSET;
                };
                if (passed) turned = true;
            }
            case RIGHT -> {
                boolean turnP = switch (origin) {
                    case SOUTH -> y < cy + Constants.CAR_SIZE;
                    case NORTH -> y > cy - Constants.CAR_SIZE;
                    case EAST  -> x > cx - Constants.CAR_SIZE;
                    case WEST  -> x < cx + Constants.CAR_SIZE;
                };
                if (turnP) {
                    double[] spd = switch (origin) {
                        case SOUTH -> new double[]{s, 0};
                        case NORTH -> new double[]{-s, 0};
                        case EAST  -> new double[]{0, s};
                        case WEST  -> new double[]{0, -s};
                    };
                    vx = spd[0]; vy = spd[1];
                    turned = true;
                }
            }
            case LEFT -> {
                boolean turnP = switch (origin) {
                    case SOUTH -> y < cy - Constants.CAR_SIZE;
                    case NORTH -> y > cy + Constants.CAR_SIZE;
                    case EAST  -> x > cx + Constants.CAR_SIZE;
                    case WEST  -> x < cx - Constants.CAR_SIZE;
                };
                if (turnP) {
                    double[] spd = switch (origin) {
                        case SOUTH -> new double[]{-s, 0};
                        case NORTH -> new double[]{s, 0};
                        case EAST  -> new double[]{0, -s};
                        case WEST  -> new double[]{0, s};
                    };
                    vx = spd[0]; vy = spd[1];
                    turned = true;
                }
            }
        }
    }

    public boolean isSafeToMove(List<Vehicle> vehicles, Origin activeGreen, double cx, double cy) {
        if (!turned) {
            boolean isAtStop = switch (origin) {
                case SOUTH -> y > cy + Constants.LANE_OFFSET && y < cy + Constants.LANE_OFFSET + Constants.CAR_SIZE;
                case NORTH -> y < cy - Constants.LANE_OFFSET && y > cy - Constants.LANE_OFFSET - Constants.CAR_SIZE;
                case EAST  -> x < cx - Constants.LANE_OFFSET && x > cx - Constants.LANE_OFFSET - Constants.CAR_SIZE;
                case WEST  -> x > cx + Constants.LANE_OFFSET && x < cx + Constants.LANE_OFFSET + Constants.CAR_SIZE;
            };
            if (isAtStop && origin != activeGreen) return false;
        }

        for (Vehicle other : vehicles) {
            if (other.id == this.id) continue;
            double dx = x - other.x;
            double dy = y - other.y;
            double dist = Math.sqrt(dx * dx + dy * dy);
            if (dist < Constants.AHEAD_CHECK_DIST && isAhead(other.x, other.y)) return false;
        }
        return true;
    }

    private boolean isAhead(double ox, double oy) {
        if (vx >  0.1) return ox > x && Math.abs(oy - y) < Constants.AHEAD_CHECK_LAT;
        if (vx < -0.1) return ox < x && Math.abs(oy - y) < Constants.AHEAD_CHECK_LAT;
        if (vy >  0.1) return oy > y && Math.abs(ox - x) < Constants.AHEAD_CHECK_LAT;
        if (vy < -0.1) return oy < y && Math.abs(ox - x) < Constants.AHEAD_CHECK_LAT;
        return false;
    }
}
