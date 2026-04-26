package com.traffic.util;

import javafx.scene.paint.Color;

public final class Constants {
    private Constants() {}

    public static final double MIN_GREEN_TIME   = 3.0;   // seconds minimum before switch allowed
    public static final double MAX_GREEN_TIME   = 10.0;  // seconds max before forced switch
    public static final double CENTER_HALF      = 55.0;
    public static final double LANE_LENGTH      = 400.0;
    public static final double VEHICLE_LENGTH   = 20.0;
    public static final double SAFETY_GAP       = 25.0;
    public static final int    LANE_CAPACITY    = (int)(LANE_LENGTH / (VEHICLE_LENGTH + SAFETY_GAP));
    public static final double CAR_SPEED        = 0.25;
    public static final double CAR_SIZE         = 20.0;
    public static final double LANE_OFFSET      = 40.0;
    public static final double ROAD_WIDTH       = 120.0;
    public static final double DESPAWN_OFFSET   = 100.0;
    public static final double SPAWN_DIST_CHECK = 30.0;
    public static final double AHEAD_CHECK_DIST = 40.0;
    public static final double AHEAD_CHECK_LAT  = 15.0;
    public static final double LIGHT_OFFSET     = 70.0;
    public static final double LIGHT_SIZE       = 20.0;

    // Canvas size
    public static final double CANVAS_WIDTH     = 800.0;
    public static final double CANVAS_HEIGHT    = 800.0;

    // Colors
    public static final Color COLOR_GRASS          = Color.rgb(51, 102, 51);
    public static final Color COLOR_ASPHALT        = Color.rgb(51, 51, 51);
    public static final Color COLOR_MARKING_WHITE  = Color.rgb(230, 230, 230, 0.8);
    public static final Color COLOR_MARKING_YELLOW = Color.rgb(230, 204, 26, 0.8);
    public static final Color COLOR_GREEN_LIGHT    = Color.rgb(50, 220, 50);
    public static final Color COLOR_RED_LIGHT      = Color.rgb(220, 50, 50);
    public static final Color COLOR_DARKGRAY       = Color.rgb(64, 64, 64);

    // Vehicle colors by route
    public static final Color COLOR_ROUTE_RIGHT    = Color.YELLOW;
    public static final Color COLOR_ROUTE_LEFT     = Color.CORNFLOWERBLUE;
    public static final Color COLOR_ROUTE_STRAIGHT = Color.WHITE;
}
