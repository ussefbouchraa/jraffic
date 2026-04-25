#!/bin/bash
# ─────────────────────────────────────────────
#  TrafficSim — run
# ─────────────────────────────────────────────

JAVAFX_LIB="./javafx/lib"
OUT_DIR="./out"

if [ ! -d "$JAVAFX_LIB" ]; then
  echo ""
  echo "  JavaFX not found. Run setup first:"
  echo "    ./setup.sh"
  echo ""
  exit 1
fi

java \
  --module-path "$JAVAFX_LIB" \
  --add-modules javafx.controls,javafx.fxml \
  -cp "$OUT_DIR" \
  com.traffic.TrafficApp