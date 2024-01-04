import ecs100.UI;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * WellingtonTrains
 * A program to answer queries about Wellington train lines and timetables for
 * the train services on those train lines.
 */

public class WellingtonTrains {
    //Fields to store the collections of Stations and Lines
    Map<String, Station> allStations = new HashMap<>();
    Map<String, TrainLine> trainLines = new HashMap<>();


    // Fields for the GUI.
    private String stationName;        // station to get info about, or to start journey from
    private String lineName;           // train line to get info about.
    private String destinationName;    // Station the user wants to reach
    private int startTime = 0;         // time for enquiring about

    /**
     * main method:  load the data and set up the user interface
     */
    public static void main(String[] args) {
        WellingtonTrains wel = new WellingtonTrains();
        wel.loadData();   // load all the data
        wel.setupGUI();   // set up the interface
    }

    /**
     * Load data files
     */
    public void loadData() {
        loadStationData();
        UI.println("Loaded Stations");
        loadTrainLineData();
        UI.println("Loaded Train Lines");
        loadTrainServicesData();
        UI.println("Loaded Train Services");
    }

    /**
     * User interface has buttons for the queries and text fields to enter stations and train line
     */
    public void setupGUI() {
        UI.addButton("All Stations", this::listAllStations);
        UI.addButton("Stations by name", this::listStationsByName);
        UI.addButton("All Lines", this::listAllTrainLines);
        UI.addTextField("Station", (String name) -> {
            this.stationName = name;
        });
        UI.addTextField("Train Line", (String name) -> {
            this.lineName = name;
        });
        UI.addTextField("Destination", (String name) -> {
            this.destinationName = name;
        });
        UI.addTextField("Time (24hr)", (String time) ->
        {
            try {
                this.startTime = Integer.parseInt(time);
            } catch (Exception e) {
                UI.println("Enter four digits");
            }
        });
        UI.addButton("Lines of Station", () -> {
            listLinesOfStation(this.stationName);
        });
        UI.addButton("Stations on Line", () -> {
            listStationsOnLine(this.lineName);
        });
        UI.addButton("Stations connected?", () -> {
            checkConnected(this.stationName, this.destinationName);
        });
        UI.addButton("Next Services", () -> {
            findNextServices(this.stationName, this.startTime);
        });
        UI.addButton("Find Trip", () -> {
            findTrip(this.stationName, this.destinationName, this.startTime);
        });

        UI.addButton("Quit", UI::quit);

        UI.setWindowSize(900, 400);
        UI.setDivider(0.2);
        // this is just a reminder to start the program using main!
        if (allStations.isEmpty()) {
            UI.setFontSize(36);
            UI.drawString("Start the program from main", 2, 36);
            UI.drawString("in order to load the data", 2, 80);
            UI.sleep(2000);
            UI.quit();
        } else {
            UI.drawImage("data/geographic-map.png", 0, 0);
            UI.drawString("Click to list closest stations", 2, 12);
        }
    }

    /**
     * Makes the trainLine objects and add the stations and train service to them
     */
    private void loadTrainLineData() {
        try {
            trainLines.clear();
            List<String> lines = Files.readAllLines(Path.of("data\\train-lines.data"));
            for (String line : lines) {
                Scanner scan = new Scanner(line);
                String name = scan.nextLine();
                trainLines.put(name, new TrainLine(name));
                TrainLine currentLine = trainLines.get(name);
                addStationsToLine(name, currentLine);
                addTrainserviceToLine(name, currentLine);
            }
            addLineToStation();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds the TrainLine objects to the station
     */
    private void addLineToStation() {
        for (TrainLine line : trainLines.values()) {
            for (Station lineStation : line.getStations()) {
                for (Station station : allStations.values()) {
                    if (lineStation.getName().equals(station.getName())) {
                        station.addTrainLine(line);
                    }
                }

            }
        }
    }

    /**
     * Adds the Train service objects to the Train Line
     */
    private void addTrainserviceToLine(String name, TrainLine currentLine) throws IOException {
        List<String> services = Files.readAllLines(Path.of("data\\" + name + "-services.data"));
        for (int i = 0; i < services.size(); i++) {
            currentLine.addTrainService(new TrainService(currentLine));
        }
    }

    /**
     * Adds the TrainLine to the station
     */
    private void addStationsToLine(String name, TrainLine currentLine) throws IOException {
        List<String> stations = Files.readAllLines(Path.of("data\\" + name + "-stations.data"));
        for (String station : stations) {
            currentLine.addStation(allStations.get(station));
        }
    }

    /**
     * Prints out all the TrainLines
     */
    private void listAllTrainLines() {
        UI.clearText();
        UI.println("All Train Lines in region\n-------------------------------\n");
        for (TrainLine line : trainLines.values()) {
            UI.println(line);
        }
    }

    /**
     * Creates and adds all the station objects to the allStation map
     */
    private void loadStationData() {
        try {
            allStations.clear();
            List<String> stations = Files.readAllLines(Path.of("data\\stations.data"));
            for (String line : stations) {
                Scanner scan = new Scanner(line);
                String name = scan.next();
                int zone = scan.nextInt();
                double x = scan.nextDouble();
                double y = scan.nextDouble();
                allStations.put(name, new Station(name, zone, x, y));

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints out all the stations
     */
    private void listAllStations() {
        UI.clearText();
        UI.println("All Stations in the region\n-------------------------------\n");
        for (Station station : allStations.values()) {
            UI.println(station);
        }
    }

    /**
     * Sorts the stations by name and prints it
     */
    private void listStationsByName() {
        UI.clearText();
        UI.println("All Stations in the region\n-------------------------------\n");
        Map<String, Station> treeMap = new TreeMap<>(allStations);
        for (Station station : treeMap.values()) {
            UI.println(station);
        }
    }

    /**
     * Reads and adds the service data to the TrainService objects
     */
    private void loadTrainServicesData() {
        try {
            for (String line : trainLines.keySet()) {
                TrainLine trainLine = trainLines.get(line);
                List<String> services = Files.readAllLines(Path.of("data\\" + line + "-services.data"));

                for (int i = 0; i < services.size(); i++) {
                    TrainService tService = trainLine.getTrainServices().get(i);
                    String times = services.get(i);
                    Scanner sc1 = new Scanner(times);
                    while (sc1.hasNextInt()) {
                        tService.addTime(sc1.nextInt());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * List the train lines associated with the given name
     */
    private void listLinesOfStation(String stationName) {
        UI.clearText();
        // Capitalizes the stationName
        String capitalizedName = capName(stationName);

        // Retrieves the station name
        Station station = allStations.get(capitalizedName);
        if (station == null) {
            UI.println("Station could not be found");
            return;
        }

        // Displays train lines for the specified station
        UI.printf("Train lines for %s station:\n---------------------------------\n\n", capitalizedName);
        for (TrainLine line : station.getTrainLines()) {
            UI.println(line);
        }

    }

    /**
     * Lists the stations associated with a given train line
     */
    private void listStationsOnLine(String lineName) {
        UI.clearText();

        // Retrieves the train line object from the map
        TrainLine line = trainLines.get(lineName);
        if (line == null) {
            UI.println("Line could not be found");
            return;
        }

        // Displays stations for the specified train line
        UI.printf("Stations for %s Line:\n---------------------------------\n\n", lineName);
        for (Station station : line.getStations()) {
            UI.println(station);
        }

    }

    /**
     * Checks if two stations are connected by any train line and displays the information
     */
    private boolean checkConnected(String stationName, String destinationName) {
        String capStationName = capName(stationName);
        String capDestinationName = capName(destinationName);

        UI.clearText();
        Station station = allStations.get(capStationName);
        Station desStation = allStations.get(capDestinationName);

        boolean found = false;

        if (station == null || desStation == null) {
            UI.println("Stations could not be found!!");
            return found;
        }

        // Iterated through train lines associated with the starting station
        UI.println("---------------");
        for (TrainLine line : station.getTrainLines()) {
            int index = line.getStations().indexOf(station);
            List<Station> subList = new ArrayList<>(line.getStations().subList(index, (line.getStations().size() - 1)));

            // Checks if the destination station is in the sublist
            if (subList.contains(desStation)) {
                UI.printf("The %s line goes from %s to %s.\n", line.getName(), capStationName, capDestinationName);
                found = true;
            }
        }

        // Displays a message if no connection is found
        if (!found) {
            UI.printf("No train line found from %s to %s.", capStationName, capDestinationName);

        }

        return found;
    }

    /**
     * Capitalized the first letter of each work in a station name
     */
    private String capName(String stationName) {
        // Determines the index of the letter after the hyphen or space
        int index = 0;
        if(stationName.contains("-")){
            index = stationName.indexOf("-") + 1; // Index of the letter after the hyphen
        }else if(stationName.contains(" ")){
            index = stationName.indexOf(" ") + 1; // Index of the letter after the space
        }

        if(index > 0){
            // Capitalizes the first letter of each work
            return stationName.substring(0, 1).toUpperCase() + stationName.substring(1, index-1).toLowerCase() + "-" +
                    stationName.substring(index, index+1).toUpperCase() + stationName.substring(index+1).toLowerCase();
        }

        // Capitalizes the first letter of the entire word
        return stationName.substring(0, 1).toUpperCase() + stationName.substring(1).toLowerCase();
    }

    /**
     * Finds the next train service departing from a given station after a specified start time.
     */
    private void findNextServices(String stationName, int startTime) {
        String capStationName = capName(stationName);
        UI.clearText();
        Station station = allStations.get(capStationName);

        // Displays information about the next service for each train line at the specified station
        UI.printf("The next service from %s is\n----------------------\n", capStationName);
        for (TrainLine trainLine : station.getTrainLines()) {
            if(trainLine == null){
                UI.println("Station could not be found!!");
                return;
            }
            List<Station> stations = trainLine.getStations();
            int i = stations.indexOf(station);
            for (TrainService tService : trainLine.getTrainServices()) {
                if (tService.getTimes().get(i) >= startTime) {
                    UI.printf("%s line at %d\n", trainLine.getName(), tService.getTimes().get(i));
                    break;
                }
            }
        }
    }

    /**
     * Finds the trip between two stations and displays the station involved
     */
    private void findTrip(String stationName, String destinationName, int startTime) {
        // Check if the station are connected
        if(checkConnected(stationName, destinationName)){
            String capStationName = capName(stationName);
            String capDestinationName = capName(destinationName);

            UI.clearText();
            Station station = allStations.get(capStationName);
            Station desStation = allStations.get(capDestinationName);

            // Iterates through train lines associated with the starting station
            for (TrainLine line : station.getTrainLines()) {
                int index = line.getStations().indexOf(station);
                List<Station> subList = new ArrayList<>(line.getStations().subList(index, (line.getStations().size() - 1)));

                // Checks if the destination station is in the sublist
                if (subList.contains(desStation)) {
                    System.out.println(subList);
                }
            }
        }
    }

}
