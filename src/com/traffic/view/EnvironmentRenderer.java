package com.traffic.view;

import com.traffic.util.Constants;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class EnvironmentRenderer {

    public static void draw(GraphicsContext gc, double cx, double cy, double w, double h) {
        // Grass
        gc.setFill(Constants.COLOR_GRASS);
        gc.fillRect(0, 0, w, h);

        // Roads
        gc.setFill(Constants.COLOR_ASPHALT);
        gc.fillRect(cx - Constants.ROAD_WIDTH / 2, 0, Constants.ROAD_WIDTH, h);
        gc.fillRect(0, cy - Constants.ROAD_WIDTH / 2, w, Constants.ROAD_WIDTH);
        
        gc.fillRect(cx - Constants.ROAD_WIDTH / 2, cy - Constants.ROAD_WIDTH / 2,
                Constants.ROAD_WIDTH, Constants.ROAD_WIDTH);

        gc.setStroke(Constants.COLOR_MARKING_YELLOW);
        gc.setLineWidth(2);
        gc.strokeLine(cx, 0, cx, cy - Constants.ROAD_WIDTH / 2);
        gc.strokeLine(cx, cy + Constants.ROAD_WIDTH / 2, cx, h);
        gc.strokeLine(0, cy, cx - Constants.ROAD_WIDTH / 2, cy);
        gc.strokeLine(cx + Constants.ROAD_WIDTH / 2, cy, w, cy);

        gc.setStroke(Constants.COLOR_MARKING_WHITE);
        gc.setLineWidth(1);

        gc.strokeLine(cx - Constants.LANE_OFFSET, 0,
                cx - Constants.LANE_OFFSET, cy - Constants.ROAD_WIDTH / 2);
        gc.strokeLine(cx - Constants.LANE_OFFSET, cy + Constants.ROAD_WIDTH / 2,
                cx - Constants.LANE_OFFSET, h);
        gc.strokeLine(cx + Constants.LANE_OFFSET, 0,
                cx + Constants.LANE_OFFSET, cy - Constants.ROAD_WIDTH / 2);
        gc.strokeLine(cx + Constants.LANE_OFFSET, cy + Constants.ROAD_WIDTH / 2,
                cx + Constants.LANE_OFFSET, h);

        gc.strokeLine(0, cy - Constants.LANE_OFFSET,
                cx - Constants.ROAD_WIDTH / 2, cy - Constants.LANE_OFFSET);
        gc.strokeLine(cx + Constants.ROAD_WIDTH / 2, cy - Constants.LANE_OFFSET, w, cy - Constants.LANE_OFFSET);
        gc.strokeLine(0, cy + Constants.LANE_OFFSET,
                cx - Constants.ROAD_WIDTH / 2, cy + Constants.LANE_OFFSET);
        gc.strokeLine(cx + Constants.ROAD_WIDTH / 2, cy + Constants.LANE_OFFSET,
                w, cy + Constants.LANE_OFFSET);

        // Stop lines
        gc.setStroke(Constants.COLOR_MARKING_WHITE);
        gc.setLineWidth(4);
        gc.strokeLine(cx, cy - Constants.ROAD_WIDTH / 2,
                cx - Constants.ROAD_WIDTH / 2, cy - Constants.ROAD_WIDTH / 2);
        gc.strokeLine(cx, cy + Constants.ROAD_WIDTH / 2,
                cx + Constants.ROAD_WIDTH / 2, cy + Constants.ROAD_WIDTH / 2);
        gc.strokeLine(cx - Constants.ROAD_WIDTH / 2, cy,
                cx - Constants.ROAD_WIDTH / 2, cy + Constants.ROAD_WIDTH / 2);
        gc.strokeLine(cx + Constants.ROAD_WIDTH / 2, cy,
                cx + Constants.ROAD_WIDTH / 2, cy - Constants.ROAD_WIDTH / 2);
    }
}
