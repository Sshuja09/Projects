/**
 * This class represents an edge between two stops in the transportation network.
 */
public class Edge {

    private final Stop fromStop; // The starting stop of the edge
    private final Stop toStop; // The destination stop of the edge
    private final String transpType; // The type of transportation (as defined in the Transport class)
    private final Line line; // The line that this edge is part of (null if it is a WALKING edge)
    private final double time; // The time, in seconds, required to travel between the two stops of the edge
    private final double distance; // The distance between the two stops of the edge
    private final String toString;   // The computed string representation of the string

    /**
     * Constructs an edge between two stops in a transportation network.
     * @param fromStop The starting stop of the edge.
     * @param toStop The destination stop of the edge.
     * @param transpType The type of transportation for this edge.
     * @param line The line associated with this edge (Null if it is a WALKING edge).
     * @param time The time, in seconds, required to travel between the two stops of the edge.
     * @param distance The distance between the two stops of the edge.
     */
    public Edge(Stop fromStop, Stop toStop, String transpType, Line line, double time, double distance){
        this.fromStop = fromStop;
        this.toStop = toStop;
        this.transpType = transpType;
        this.line = line;
        this.time = time;
        this.distance = distance;
        this.toString = "FROM " +
            fromStop.getName() + "(" + fromStop.getId()+")  TO "+
            toStop.getName() + "(" + toStop.getId()+")  BY "+transpType+
            ((line!=null)?("(" + line.getId()+")"):"")+
            "  " + ((int)time) + "s/" + ((int)distance)+"m";
    }

    /**
     * Gets the starting stop of the edge
     * @return The starting edge
     */
    public Stop fromStop() {return fromStop;}

    /**
     * Gets the destination stop of the edge
     * @return The destination edge
     */
    public Stop toStop() {return toStop;}

    /**
     * Gets the type of transportation for this edge.
     * @return The transportation type.
     */
    public String transpType() {return transpType;}

    /**
     * Gets the line associated with this edge.
     * @return The line or null if not applicable.
     */
    public Line line() {return line;}

    /**
     * Gets the time required to travel between the two stops of the edge.
     * @return The travel time in seconds.
     */
    public double time() {return time;}

    /**
     * Gets the distance between the two stops of the edge.
     * @return The distance in meters.
     */
    public double distance() {return distance;}

    /**
     * Gets the string representation of the edge.
     * @return The string representation.
     */
    public String toString() {return this.toString;}

}
