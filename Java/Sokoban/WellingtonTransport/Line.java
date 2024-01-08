import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Structure for holding information about a bus/train/ferry/cable-car line.
 */

public class Line {
    
    private final String lineId; // Unique identifier for the line
    private final String transpType; // Type of transportation associated with the line ("bus", "train", "cablecar", "ferry").

    // Paired lists with stop id and stop times. Need to make sure they remain in order
    private List<Stop> stops;
    private List<Integer> times;

    /**
     * Constructor used to create and then add stops to the line.
     * @param lineId Unique identifier for the line.
     */
    public Line(String lineId) {
        this.lineId = lineId;
        this.transpType = Transport.transpType(lineId);
        this.stops = new ArrayList<Stop>();
        this.times = new ArrayList<Integer>();
    }

    /**
     * Add a stop to the end of the current line.
     * @param stop The stop to be added.
     * @param time The time from the start of the line to the current stop.
     */
    public void addStop(Stop stop, int time) {
        this.stops.add(stop);
        this.times.add(time);
    }

    /**
     * Gets the unique identifier of the line.
     * @return The line identifier.
     */
    public String getId() {
        return lineId;
    }

    /**
     * Get the type of transportation associated with the line.
     * @return The transportation type ("bus", "train", "cablecar", "ferry").
     */
    public String getType() {
        return transpType;
    }

    /**
     * Return a string representation of the Line object.
     * @return A string containing line details.
     */
    public String toString() {
        String s = "";
        s += "Line: " + lineId + " ("+transpType+")\t stops: " + stops.toString() + "\t times: " + times.toString();
        return s;
    }

    /**
     * Return an unmodifiable list of stops for each stop in the line.
     * @return The list of stops in the line.
     */
    public List<Stop> getStops() {
        return Collections.unmodifiableList(stops);
    }

    /**
     * Return an unmodifiable list of times for each stop in the line.
     * @return The list of times in seconds.
     */
    public List<Integer> getTimes() {
        return  Collections.unmodifiableList(times);
    }

}
