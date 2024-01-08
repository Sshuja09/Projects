import java.util.*;

/**
 * This class finds strongly connected components is a directed graph using Kosaraju algorithm
 */
public class Components{

    /**
     * Finds strongly connected components in the given graph.
     * @param graph The directed graph.
     */
    public static void findComponents(Graph graph) {
        System.out.println("calling findComponents");
        graph.resetSubGraphIds();

        Set<Stop> visited = new HashSet<>(); // To keep track of the visited nodes
        Stack<Stop> fringe = new Stack<>(); // To search in backward order

        // Forward search
        for (Stop stop : graph.getStops()) {
            if (!visited.contains(stop)) {
                forwardSearch(stop, visited, fringe);
            }
        }

        // Backward search
        visited.clear();
        int id = 0;
        while (!fringe.isEmpty()) {
            Stop stop = fringe.pop();
            if (!visited.contains(stop)) {
                backwardSearch(stop, visited, id);
                id++;
            }
        }

        graph.setSubGraphCount(id);
    }

    /**
     * Performs a depth-first search in the forward direction from the given stop.
     * @param stop The starting stop
     * @param visited Set to keep track of visited stops.
     * @param fringe Stack to store stops in the order of exploration.
     */
    private static void forwardSearch(Stop stop, Set<Stop> visited, Stack<Stop> fringe) {
        visited.add(stop);
        for (Edge edge : stop.getForwardEdges()) {
            if (!visited.contains(edge.toStop())) { // Checks if the neighbour has been visited or not
                forwardSearch(edge.toStop(), visited, fringe); // Search from the neighbour
            }
        }
        fringe.push(stop);
    }

    /**
     * Performs a depth-first search in the backward direction from the given stop.
     * @param stop The starting stop.
     * @param visited Set to keep track of visited stops.
     * @param id The identifier for the strongly connected component.
     */
    private static void backwardSearch(Stop stop, Set<Stop> visited, int id) {
        visited.add(stop);
        stop.setSubGraphId(id);
        for (Edge edge : stop.getBackwardEdges()) {
            if (!visited.contains(edge.fromStop())) { // Checks if the neighbour has been visited
                backwardSearch(edge.fromStop(), visited, id); // Search from the neighbour
            }
        }
    }

}