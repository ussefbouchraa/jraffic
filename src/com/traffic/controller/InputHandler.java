package com.traffic.controller;

import com.traffic.enums.Origin;
import com.traffic.model.Vehicle;
import com.traffic.model.VehicleSpawner;
import com.traffic.util.Constants;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.List;
import java.util.Random;

public class InputHandler {

    private static final Random RNG = new Random();

    private final List<Vehicle> vehicles;
    private double cx, cy, w, h;
    private long lastSpawnTime = 0;
    private static final long COOLDOWN = 3000;

    public InputHandler(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public void setContext(double cx, double cy, double w, double h) {
        this.cx = cx;
        this.cy = cy;
        this.w = w;
        this.h = h;
    }

    public void handle(KeyEvent e) {
        double s = Constants.CAR_SPEED;
        KeyCode code = e.getCode();

        long nC = count(Origin.NORTH);
        long sC = count(Origin.SOUTH);
        long eC = count(Origin.EAST);
        long wC = count(Origin.WEST);

        switch (code) {
            case UP -> {
                if (sC < Constants.LANE_CAPACITY)
                    VehicleSpawner.trySpawn(vehicles, cx + 20, h, Origin.SOUTH, 0, -s);
            }
            case DOWN -> {
                if (nC < Constants.LANE_CAPACITY)
                    VehicleSpawner.trySpawn(vehicles, cx - 20, 0, Origin.NORTH, 0, s);
            }
            case RIGHT -> {
                if (eC < Constants.LANE_CAPACITY)
                    VehicleSpawner.trySpawn(vehicles, 0, cy + 20, Origin.EAST, s, 0);
            }
            case LEFT -> {
                if (wC < Constants.LANE_CAPACITY)
                    VehicleSpawner.trySpawn(vehicles, w, cy - 20, Origin.WEST, -s, 0);
            }
            case R -> {
                long now = System.currentTimeMillis();
                if (System.currentTimeMillis() - lastSpawnTime >= COOLDOWN) {
                    spawnRandom(s, nC, sC, eC, wC);
                    lastSpawnTime = now;
                }
            }
        }
    }

    private void spawnRandom(double s, long nC, long sC, long eC, long wC) {
        int dir = RNG.nextInt(4);
        switch (dir) {
            case 0 -> {
                if (nC < Constants.LANE_CAPACITY)
                    VehicleSpawner.trySpawn(vehicles, cx - 20, 0, Origin.NORTH, 0, s);
            }
            case 1 -> {
                if (sC < Constants.LANE_CAPACITY)
                    VehicleSpawner.trySpawn(vehicles, cx + 20, h, Origin.SOUTH, 0, -s);
            }
            case 2 -> {
                if (eC < Constants.LANE_CAPACITY)
                    VehicleSpawner.trySpawn(vehicles, 0, cy + 20, Origin.EAST, s, 0);
            }
            case 3 -> {
                if (wC < Constants.LANE_CAPACITY)
                    VehicleSpawner.trySpawn(vehicles, w, cy - 20, Origin.WEST, -s, 0);
            }
        }
    }

    private long count(Origin origin) {
        return vehicles.stream().filter(v -> v.origin == origin && !v.turned).count();
    }
}
