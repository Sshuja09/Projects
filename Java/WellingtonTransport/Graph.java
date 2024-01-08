import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.HashSet;


/**
 * Graph is the data structure that stores the collection of stops, lines and connections. 
 * The Graph constructor is passed a Map of the stops, indexed by stopId and
 *  a Map of the Lines, indexed by lineId.
 * The Stops in the map have their id, name and GIS location.
 * The Lines in the map have their id, and lists of the stopIDs an times (in order)
 *
 * To build the actual graph structure, it is necessary to
 *  build the list of Edges out of each stop and the list of Edges into each stop
 * Each pair of adjacent stops in a Line is an edge.
 * We also need to create walking edges between every pair of stops in the whole
 *  network that are closer than walkingDistance.
 */
public class Graph {

    private Collection<Stop> stops; // Collection of stops in the graph.
    private Collection<Line> lines; // Collection of lines in the graph.
    private Collection<Edge> edges = new HashSet<Edge>(); // Collection of edges connecting stops.
    private int numComponents = 0;     // Number of connected sub-graphs (graph components).

    /**
     * Constructs a new graph with a given collection of stops and lines.
     * @param stops Collection of stops.
     * @param lines Collection of lines.
     */
    public Graph(Collection<Stop> stops, Collection<Line> lines) {
        this.stops = new TreeSet<Stop>(stops);
        this.lines = lines;

        createAndConnectEdges();
        computeNeighbours();

        // printGraphData();   // you could uncomment this to help in debugging
    }


    /**
     * Print out the lines and stops in the graph to System.out
     */
    public void printGraphData(){
        System.out.println("============================\nLines:");
        for (Line line : lines){
            System.out.println(line.getId()+ "("+line.getStops().size()+" stops)");
        }
        System.out.println("\n=============================\nStops:");
        for (Stop stop : stops){
            System.out.println(stop+((stop.getSubGraphId()<0)?"":" subG:"+stop.getSubGraphId()));
            System.out.println("  "+stop.getForwardEdges().size()+" out edges; "+
                               stop.getBackwardEdges().size() +" in edges; " +
                               stop.getNeighbours().size() +" neighbours");
        }
        System.out.println("===============");
    }

    /** 
     * Creates and connects edges between stops based on lines.
     */
    private void createAndConnectEdges() {
        //Create edges between stops for each line
        for(Line line: lines){
            List<Stop> stops = line.getStops(); // Gets line stops
            List<Integer> times = line.getTimes(); // Gets line timing
            for(int i = 0; i < stops.size() - 1; i++){
                Stop fromStop = stops.get(i); // Current stop
                Stop toStop = stops.get(i + 1); // Next stop
                Edge edge = new Edge(fromStop, toStop, line.getType(), line, times.get(i + 1) - times.get(i), fromStop.distanceTo(toStop)); // Creates the edge object
                edges.add(edge); // Adds the edge object to edges set
                fromStop.addForwardEdge(edge); // Adds an edge from current stop to the next stop
                toStop.addBackwardEdge(edge); // Adds an edge from next stop to current stop
            }
        }

        System.out.println("NUM OF STOPS: " + stops.size());
    }

    /** 
     * Computes the undirected graph of neighbours for each stop.
     */
    public void computeNeighbours(){
        Map<Stop, Set<Stop>> neighbours = new HashMap<>(); // Map of stops and theirs neighbours

        for(Edge edge: edges){
            Stop fromStop = edge.fromStop(); // Stop where the edge starts from
            Stop toStop = edge.toStop(); // Stop where the edge ends

            // Add fromStop as a neighbour of toStop
            Set<Stop> toStopNeighbours = neighbours.getOrDefault(toStop, new HashSet<>()); // Gets the stop's neighbours from the map
            toStopNeighbours.add(fromStop); // Adds current stop as a neighbour of the next stop
            neighbours.put(toStop, toStopNeighbours); // Updates the map

            // Add toStop as a neighbour of fromStop
            Set<Stop> fromStopNeighbours = neighbours.getOrDefault(fromStop, new HashSet<>()); // Gets the stop's neighbours from the map
            fromStopNeighbours.add(toStop); // Adds next stop as a neighbour of the current stop
            neighbours.put(fromStop, fromStopNeighbours); // Updates the map
        }

        for(Stop stop: stops){ // Goes through each stop
            Set<Stop> stopNeighbours = neighbours.getOrDefault(stop, new HashSet<>()); // Gets the stop's neighbours from the map
            if(!neighbours.isEmpty()) {
                for (Stop n : stopNeighbours) {
                    stop.addNeighbour(n);
                }
            }
        }
    }

    /** 
     * Recomputes walking edges and adds them to the graph
     */
    public void recomputeWalkingEdges(double walkingDistance) {
        int count = 0;
        this.removeWalkingEdges(); // Removes previous walking edges

        for(Stop stop1: stops){
            for(Stop stop2: stops){
                if(stop1 != stop2 && stop1.distanceTo(stop2) <= walkingDistance){
                    double time = stop1.distanceTo(stop2) / Transport.WALKING_SPEED_MPS; // Calculates the time
                    Edge edge1 = new Edge(stop1, stop2, Transport.WALKING, null, time, stop1.distanceTo(stop2)); // Edge from stop1 to stop2
                    edges.add(edge1);
                    stop1.addForwardEdge(edge1); // Adds an edge from current stop to the next stop
                    stop2.addBackwardEdge(edge1); // Adds an edge from next stop to the current stop
                    count++;
                }
            }
        }

       // computeNeighbours();

        System.out.println("Number of walking edges added: " + count); // Should equal to 25956
    }

    /** 
     * Remove all the current walking edges in the graph
     */
    public void removeWalkingEdges() {
        resetSubGraphIds();
        for (Stop stop : stops) {
            stop.deleteEdgesOfType(Transport.WALKING);// remove all edges of type walking
        }
        edges.removeIf((Edge e)->Transport.WALKING.equals(e.transpType()));
    }

    /**
     * Return a collection of all the stops in the network.
     * @return Unmodifiable collection of stops.
     */
    public Collection<Stop> getStops() {
        return Collections.unmodifiableCollection(stops);
    }
    /**
     * Return a collection of all the edges in the network
     * @return Unmodifiable collection of edges.
     */        
    public Collection<Edge> getEdges() {
        return Collections.unmodifiableCollection(edges);
    }

    /**
     * Return the first stop that starts with the specified prefix.
     * (first by alphabetic order of name)
     * @param prefix Prefix to match.
     * @return First matching stop or null if not found.
     */
    public Stop getFirstMatchingStop(String prefix) {
        for (Stop stop : stops) {
            if (stop.getName().startsWith(prefix)) {
                return stop;
            }
        }
        return null;
    }

    /** 
     * Return all the stops that start with the specified prefix in alphabetic order.
     * @param prefix Prefix to match.
     * @return List of matching stops.
     */
    public List<Stop> getAllMatchingStops(String prefix) {
        List<Stop> ans = new ArrayList<Stop>();
        for (Stop stop : stops) {
            if (stop.getName().startsWith(prefix)) {
                ans.add(stop);
            }
        }
        return ans;
    }

    /**
     * Returns the number of connected sub-graphs (graph components).
     * @return Number of sub-graphs.
     */
    public int getSubGraphCount() {
        return numComponents;
    }

    /**
     * Sets the number of connected sub-graphs.
     * @param num Number of sub-graphs.
     */
    public void setSubGraphCount(int num) {
        numComponents = num;
        if (num==0){ resetSubGraphIds(); }
    }

    /**
     * Reset the subgraph ID of all stops
     */
    public void resetSubGraphIds() {
        for (Stop stop : stops) {
            stop.setSubGraphId(-1);
        }
        numComponents = 0;
    }

}
