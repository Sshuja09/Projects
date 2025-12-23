# WellingtonTransport

A Java-based interactive transportation network visualization and pathfinding application for Wellington, New Zealand. This application provides a graphical interface to explore the public transport network, find shortest paths between stops, analyze network connectivity, and identify critical infrastructure points.

## Features

### 🗺️ Interactive Map Visualization
- Visual representation of Wellington's public transport network
- Interactive map with zoom and pan capabilities
- Color-coded transportation types (buses, trains, cable cars, ferries, walking)
- Fare zone visualization
- Stop highlighting and selection

### 🚌 Pathfinding
- **A* Algorithm**: Find shortest paths between any two stops
- Optimize by distance or time
- Visual path highlighting with detailed route information
- Support for multi-modal transportation (buses, trains, walking)

### 🔗 Network Analysis
- **Connected Components**: Identify strongly connected components using Kosaraju's algorithm
- **Articulation Points**: Find critical nodes whose removal would disconnect the network
- Color-coded visualization of network components

### 🚶 Walking Connections
- Configurable walking distance (0-400 meters)
- Automatic generation of walking edges between nearby stops
- Toggle walking connections on/off

### 🌐 Multi-language Support
- English (en_NZ)
- Te Reo Māori (mi_NZ)
- Easy language switching via UI buttons

## Technologies Used

- **Java**: Core programming language
- **JavaFX**: GUI framework for the interactive interface
- **FXML**: UI layout definition
- **Graph Algorithms**: A*, Kosaraju's algorithm, articulation point detection

## Project Structure

```
WellingtonTransport/
├── Main.java                 # Application entry point and JavaFX setup
├── Controller.java           # Main controller handling UI interactions
├── Graph.java                # Graph data structure and network management
├── Stop.java                 # Represents a transport stop
├── Edge.java                 # Represents a connection between stops
├── Line.java                 # Represents a transport line/route
├── AStar.java                # A* pathfinding algorithm implementation
├── Components.java           # Strongly connected components (Kosaraju)
├── ArticulationPoints.java   # Articulation point detection
├── Transport.java            # Transportation type constants and utilities
├── GisPoint.java             # Geographic coordinate representation
├── Projection.java           # Coordinate system transformations
├── Zoning.java               # Fare zone data management
├── PathItem.java             # Path item for A* algorithm
├── data/                     # Data files
│   ├── stops.txt            # Stop information (ID, name, coordinates)
│   ├── lines.txt            # Line/route information
│   └── WellingtonZones.csv  # Fare zone boundaries
├── resources/                # Localization resources
│   ├── strings_en_NZ.properties
│   └── strings_mi_NZ.properties
├── MapView.fxml              # UI layout definition
└── Test*.java                # Test files for various components
```

## Installation & Setup

### Prerequisites
- Java Development Kit (JDK) 11 or higher
- JavaFX SDK (if not included with JDK)
- An IDE (IntelliJ IDEA, Eclipse, or BlueJ) or command-line tools

### Running the Application

1. **Using an IDE**:
   - Open the project in your IDE
   - Ensure JavaFX is configured in your project settings
   - Run `Main.java`

2. **Using Command Line**:
   ```bash
   javac --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml *.java
   java --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml Main
   ```

3. **Data Files**:
   - Ensure the `data/` directory contains:
     - `stops.txt`: Stop data with tab-separated values
     - `lines.txt`: Line/route data with tab-separated values
     - `WellingtonZones.csv`: Fare zone boundary data

## Usage

### Finding a Path

1. **Using Text Fields**:
   - Type a stop name in the "Start" field (autocomplete suggestions appear)
   - Type a stop name in the "Goal" field
   - Press Enter in either field to calculate the path
   - The path will be displayed on the map and detailed in the text area

2. **Using Mouse Clicks**:
   - Click on a stop to set it as the start location
   - Click on another stop to set it as the goal and calculate the path
   - Hold Shift while clicking to add waypoints

### Network Analysis

- **Connected Components**: Click the "Connected Components" button to identify and color-code all strongly connected components in the network
- **Articulation Points**: Click the "Articulation Points" button to highlight critical nodes (displayed in red)

### Walking Connections

- Check the "Walking" checkbox to enable walking connections
- Adjust the walking distance using the slider (0-400 meters) or text field
- Walking edges appear in purple on the map

### Map Navigation

- **Zoom**: Scroll with mouse wheel
- **Pan**: Click and drag on the map
- **Stop Information**: Click on any stop to see its details

### Language Switching

- Click "English" or "Māori" buttons to switch the interface language

## Key Algorithms

### A* Pathfinding
- **Purpose**: Find shortest path between two stops
- **Optimization**: Can optimize by distance or time
- **Heuristic**: Uses straight-line distance (or time estimate) to goal
- **Implementation**: Priority queue-based search with back-pointers for path reconstruction

### Kosaraju's Algorithm
- **Purpose**: Find strongly connected components in directed graph
- **Process**: 
  1. Forward DFS to build finish-time stack
  2. Backward DFS on transposed graph to identify components
- **Result**: All stops are assigned a component ID and color-coded

### Articulation Point Detection
- **Purpose**: Identify critical nodes in the network
- **Algorithm**: Depth-first search with reach-back values
- **Significance**: These nodes are critical for network connectivity

## Data Format

### stops.txt
Tab-separated values with columns:
- stop_id, stop_code, stop_name, stop_desc, stop_lat, stop_lon, zone_id, location_type, parent_station, stop_url, stop_timezone

### lines.txt
Tab-separated values with columns:
- line_id, stop_id, timepoint

### WellingtonZones.csv
CSV file containing fare zone boundary polygons

## Testing

The project includes several test files:
- `TestAStar.java`: Tests for A* pathfinding
- `TestGraphAndComponents.java`: Tests for graph structure and components
- `TestArticPts.java`: Tests for articulation point detection
- `CustomTests.java`: Additional custom test cases

Run tests individually or as part of a test suite to verify functionality.

## Color Coding

- **Buses**: Rosy Brown
- **Trains**: Orange
- **Walking**: Purple
- **Path Highlight**: Red/Black/Green (depending on transport type)
- **Highlighted Stops**: Red (double size)
- **Articulation Points**: Red
- **Connected Components**: Color-coded by component ID (hue-based)

## Notes

- The application assumes data files are in the `data/` directory relative to the project root
- Map scale defaults to 5000 (1 pixel ≈ 20 meters)
- Walking edges are dynamically computed based on distance threshold
- The network supports buses, trains, cable cars, ferries, and walking connections

## Author

Created as part of a Java programming course project focusing on graph algorithms and GUI development.

