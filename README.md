# TrafficSim — Plain Java Setup (no Maven)

## What you need

| Tool | Where to get it |
|---|---|
| JDK 21+ | https://adoptium.net |
| JavaFX 21 SDK | https://gluonhq.com/products/javafx/ |

---

## Step 1 — Install JDK 21

Download and install from https://adoptium.net  
Make sure `java -version` prints `21` or higher in your terminal.

---

## Step 2 — Download JavaFX SDK

1. Go to https://gluonhq.com/products/javafx/
2. Choose **Version 21**, your OS, type **SDK**
3. Download and unzip it
4. Move/rename the unzipped folder to `javafx` inside this project

Your folder should look like this:

```
TrafficSimPlain/
  javafx/
    lib/
      javafx.base.jar
      javafx.controls.jar
      javafx.graphics.jar
      ...
  src/
  compile.sh  (or compile.bat on Windows)
  run.sh      (or run.bat on Windows)
```

---

## Step 3 — Compile

**macOS / Linux:**
```bash
chmod +x compile.sh run.sh
./compile.sh
```

**Windows:**
```
compile.bat
```

---

## Step 4 — Run

**macOS / Linux:**
```bash
./run.sh
```

**Windows:**
```
run.bat
```

---

## Controls

| Key | Action |
|---|---|
| `↑` | Spawn from South |
| `↓` | Spawn from North |
| `→` | Spawn from East |
| `←` | Spawn from West |
| `R` | Spawn random |
| `ESC` | Quit |

---

## Traffic light logic

- **Min green time**: 3s — light won't switch before this
- **Max green time**: 10s — forced switch to prevent starvation
- Priority goes to the direction with the most queued vehicles
