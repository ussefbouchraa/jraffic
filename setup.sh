#!/bin/bash
# ─────────────────────────────────────────────
#  TrafficSim — one-time setup: downloads JavaFX
# ─────────────────────────────────────────────

set -e

JAVAFX_VERSION="21.0.2"
JAVAFX_DIR="./javafx"

if [ -d "$JAVAFX_DIR/lib" ]; then
  echo "JavaFX already present — nothing to do."
  echo "Run: ./compile.sh && ./run.sh"
  exit 0
fi

OS="$(uname -s)"
ARCH="$(uname -m)"

case "$OS" in
  Linux*)
    [ "$ARCH" = "aarch64" ] && PLATFORM="linux-aarch64" || PLATFORM="linux-x64"
    ;;
  Darwin*)
    [ "$ARCH" = "arm64" ] && PLATFORM="osx-aarch64" || PLATFORM="osx-x64"
    ;;
  MINGW*|MSYS*|CYGWIN*)
    PLATFORM="windows-x64"
    ;;
  *)
    echo "Unknown OS: $OS — download manually from https://gluonhq.com/products/javafx/"
    exit 1
    ;;
esac

FILENAME="openjfx-${JAVAFX_VERSION}_${PLATFORM}_bin-sdk.zip"
URL="https://download2.gluonhq.com/openjfx/${JAVAFX_VERSION}/${FILENAME}"

echo "Detected: $OS $ARCH → $PLATFORM"
echo "Downloading JavaFX $JAVAFX_VERSION..."
echo "  $URL"
echo ""

TMP_FILE="/tmp/${FILENAME}"

curl -L --progress-bar -o "$TMP_FILE" "$URL"

# Verify it's actually a zip
if ! file "$TMP_FILE" | grep -q "Zip"; then
  echo ""
  echo "ERROR: Downloaded file is not a valid zip."
  echo "Try downloading manually from:"
  echo "  https://gluonhq.com/products/javafx/"
  echo "Then unzip and rename the folder to 'javafx' inside this project."
  rm -f "$TMP_FILE"
  exit 1
fi

echo "Extracting..."
unzip -q "$TMP_FILE" -d "/tmp/javafx_extract"

EXTRACTED=$(find /tmp/javafx_extract -maxdepth 1 -type d -name "javafx-sdk-*" | head -1)

if [ -z "$EXTRACTED" ]; then
  echo "ERROR: could not find sdk folder after extraction"
  exit 1
fi

mv "$EXTRACTED" "$JAVAFX_DIR"
rm -f "$TMP_FILE"
rm -rf /tmp/javafx_extract

echo ""
echo "JavaFX ready at: $JAVAFX_DIR"
echo ""
echo "Now run:"
echo "  ./compile.sh"
echo "  ./run.sh"