#!/bin/bash
# ─────────────────────────────────────────────
#  TrafficSim — compile
# ─────────────────────────────────────────────

set -e

JAVAFX_LIB="./javafx/lib"
SRC_DIR="./src"
OUT_DIR="./out"

if [ ! -d "$JAVAFX_LIB" ]; then
  echo ""
  echo "  JavaFX not found. Run setup first:"
  echo "    ./setup.sh"
  echo ""
  exit 1
fi

mkdir -p "$OUT_DIR"
SOURCES=$(find "$SRC_DIR" -name "*.java")

echo "Compiling..."

javac \
  --module-path "$JAVAFX_LIB" \
  --add-modules javafx.controls,javafx.fxml \
  -d "$OUT_DIR" \
  $SOURCES

echo "Done. Run with: ./run.sh"