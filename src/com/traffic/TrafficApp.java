package com.traffic;

import com.traffic.model.Intersection;
import com.traffic.util.Constants;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class TrafficApp extends Application {

    private static final double FPS_TARGET = 60.0;
    private static final double DT         = 1.0 / FPS_TARGET;

    @Override
    public void start(Stage stage) {
        double W = Constants.CANVAS_WIDTH;
        double H = Constants.CANVAS_HEIGHT;

        Canvas canvas = new Canvas(W, H);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Intersection intersection = new Intersection();
        intersection.setDimensions(W, H);

        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, W, H);

        // Key input
        scene.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
                javafx.application.Platform.exit();
            } else {
                intersection.getInputHandler().handle(e);
            }
        });

        // Game loop via AnimationTimer (fires each rendered frame)
        AnimationTimer timer = new AnimationTimer() {
            private long lastNano = 0;

            @Override
            public void handle(long now) {
                if (lastNano == 0) { lastNano = now; return; }
                double elapsed = (now - lastNano) / 1_000_000_000.0;
                lastNano = now;

                // Cap dt to avoid spiral-of-death on lag spikes
                double dt = Math.min(elapsed, 0.05);

                intersection.update(dt);

                gc.clearRect(0, 0, W, H);
                intersection.draw(gc);
            }
        };
        timer.start();

        stage.setTitle("Road Intersection Simulation");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
