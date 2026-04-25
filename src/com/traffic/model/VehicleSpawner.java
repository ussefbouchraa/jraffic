package com.traffic.model;

import com.traffic.enums.Origin;
import com.traffic.enums.Route;
import com.traffic.util.Constants;

import java.util.List;
import java.util.Random;

public class VehicleSpawner {

    private static final Random RNG = new Random();

    public static void trySpawn(List<Vehicle> vehicles, double px, double py,
                                Origin origin, double vx, double vy) {
        boolean tooClose = vehicles.stream().anyMatch(v -> {
            double dx = v.x - px;
            double dy = v.y - py;
            return Math.sqrt(dx * dx + dy * dy) < Constants.SPAWN_DIST_CHECK;
        });
        if (tooClose) return;

        Route route = switch (RNG.nextInt(3)) {
            case 0  -> Route.RIGHT;
            case 1  -> Route.LEFT;
            default -> Route.STRAIGHT;
        };

        javafx.scene.paint.Color color = switch (route) {
            case RIGHT    -> Constants.COLOR_ROUTE_RIGHT;
            case LEFT     -> Constants.COLOR_ROUTE_LEFT;
            case STRAIGHT -> Constants.COLOR_ROUTE_STRAIGHT;
        };

        vehicles.add(new Vehicle(px, py, vx, vy, origin, route, color));
    }
}
