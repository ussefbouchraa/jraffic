package com.traffic.model;

import com.traffic.controller.InputHandler;
import com.traffic.controller.TrafficController;
import com.traffic.enums.Origin;
import com.traffic.view.EnvironmentRenderer;
import com.traffic.view.HudRenderer;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public class Intersection {

    private final List<Vehicle> vehicles = new ArrayList<>();
    private final TrafficController trafficController = new TrafficController();
    private final InputHandler inputHandler;

    private double cx, cy, w, h;
    private double time = 0;

    public Intersection() {
        inputHandler = new InputHandler(vehicles);
    }

    public void setDimensions(double w, double h) {
        this.w  = w; this.h  = h;
        this.cx = w / 2.0;
        this.cy = h / 2.0;
        inputHandler.setContext(cx, cy, w, h);
    }

    public InputHandler getInputHandler() { return inputHandler; }

    public void update(double dt) {
        time += dt;
        trafficController.update(dt, vehicles, cx, cy);
        Origin activeGreen = trafficController.getActiveGreen();

        List<Vehicle> toRemove = new ArrayList<>();
        for (Vehicle v : vehicles) {
            if (v.shouldDespawn(w, h)) {
                toRemove.add(v);
            } else {
                if (v.isSafeToMove(vehicles, activeGreen, cx, cy)) {
                    v.update(cx, cy);
                }
            }
        }
        vehicles.removeAll(toRemove);
    }

    public void draw(GraphicsContext gc) {
        EnvironmentRenderer.draw(gc, cx, cy, w, h);
        trafficController.draw(gc, cx, cy);
        for (Vehicle v : vehicles) {
            v.draw(gc, time);
        }
        HudRenderer.draw(gc, vehicles, trafficController, w, h);
    }
}
