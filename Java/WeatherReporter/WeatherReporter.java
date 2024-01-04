import ecs100.*;
import java.util.*;
import java.nio.file.*;
import java.io.*;
import java.awt.Color;

/**
 * WeatherReporter
 * Analyses weather data from files of weather-station measurements.
 *
 * The weather data files consist of a set of measurements from weather stations around
 * New Zealand at a series of date/time stamps.
 * For each date/time, the file has:
 *  A line with the date and time (four integers for day, month, year, and time)
 *   eg "24 01 2021 1900"  for 24 Jan 2021 at 19:00
 *  A line with the number of weather-stations for that date/time 
 *  Followed by a line of data for each weather station:
 *   - name: one token, eg "Cape-Reinga"
 *   - (x, y) coordinates on the map: two numbers, eg   186 38
 *   - four numbers for temperature, dew-point, suface-pressure, and sea-level-pressure
 *
 * Note, the data files were extracted from MetOffice weather data from 24-26 January 2021
 */

public class WeatherReporter{

    // Constant diameter for the temperature circles.
    public static final double DIAM = 10;

    // Constants for text placement
    public static final double LEFT_TEXT = 10;
    public static final double TOP_TEXT = 50;

    /**
     * Reads temperature data from a file and plots a snapshot
     */
    public void plotTemperatures(){
        try {
            UI.clearPanes(); // Clear the UI panes
            String filename = UIFileChooser.open(); // Open a file chooser dialog to get the filename
            Scanner sc = new Scanner (Path.of(filename)); // Create a scanner to read from the file
            plotSnapshot(sc); // Plot the temperature snapshot
        } catch(IOException e){UI.println("File reading failed");} // Handle file reading failure
    }

    /**
     * Reads date and time information from the scanner and draws it on the UI.
     * The input format is expected to be: day month year time
     * @param sc Scanner object to read input
     */
    public void drawDate(Scanner sc){
        // Read date and time components
        int day = sc.nextInt();
        int month = sc.nextInt();
        int year = sc.nextInt();
        String time = formatTime(sc.next());

        // Format the date and time string
        String dateTime = String.format("%d/%d/%d at %s", day, month, year, time);

        // Draw the formatted date and time on the UI
        UI.drawString(dateTime, LEFT_TEXT, TOP_TEXT);
    }

    /**
     * Formats the time string depending on if the hour is single-digit or double-digit
     * @param time Unformatted time string
     * @return Formatted time string
     */
    private String formatTime(String time) {
        if (time.length() > 3) {
            // If the hour is double-digit
            return time.substring(0, 2) + ":" + time.substring(2);
        } else {
            // If the hour is single-digit
            return time.charAt(0) + ":" + time.substring(1);
        }
    }

    /**
     * Plot a snapshot of temperature data on a map of New Zealand
     * @param sc A scanner object containing temperature data
     */
    public void plotSnapshot(Scanner sc){
        // Draw the New Zealand map as the background
        UI.drawImage("map-new-zealand.gif", 0, 0);

        // Extract and display the date and time from the scanner
        drawDate(sc);

        // Consume the first integer (Not used in the loop)
        sc.nextInt();

        // Loop through each line of temperature data
        while(sc.hasNextLine()){
            // Extract station name, x and y coordinates, and temperature from the scanner
            String station = sc.next();
            double x = sc.nextDouble();
            double y = sc.nextDouble();
            double temp = sc.nextDouble();

            // Set the color based on the temperature and fill an oval at the specified coordinates
            UI.setColor(this.getTemperatureColor(temp));
            UI.fillOval(x, y, DIAM, DIAM);

            // Consume the rest of the line
            sc.nextLine();
        }
    }

    /** Returns a color representing that temperature
     *  The colors are increasingly blue below 15 degrees, and
     *  increasingly red above 15 degrees.
     */
    public Color getTemperatureColor(double temp){
        double max = 37, min = -5, mid = (max+min)/2;
        if (temp < min || temp > max){
            return Color.white;
        }
        else if (temp <= mid){ //blue range: hues from .7 to .5
            double tempFracOfRange = (temp-min)/(mid-min);
            double hue = 0.7 -  tempFracOfRange*(0.7-0.5);
            return Color.getHSBColor((float)hue, 1.0F, 1.0F);
        }
        else { //red range: .15 to 0.0
            double tempFracOfRange = (temp-mid)/(max-mid);
            double hue = 0.15 -  tempFracOfRange*(0.15-0.0);
            return Color.getHSBColor((float)hue, 1.0F, 1.0F);
        }
    }

    public void setupGUI(){
        UI.initialise();
        UI.addButton("Plot temperature", this::plotTemperatures);
        UI.addButton("Quit", UI::quit);
        UI.setWindowSize(800,750);
        UI.setFontSize(18);
    }

    public static void main(String[] arguments){
        WeatherReporter obj = new WeatherReporter();
        obj.setupGUI();
    }

}
