import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

/**
 * This class provides a utility for finding articulation points in an undirected graph.
 * An articulation point is a node in a graph, removal of which increases the number of connected components.
 */
public class ArticulationPoints{

    /**
     * Finds all the sub-graphs in the graph.
     * @param graph The input graph.
     * @return A set of sets, each representing a sub-graph in the graph.
     */
    private static Set<Set<Stop>> findSubGraphs(Graph graph) {
        Set<Stop> visited = new HashSet<>();
        Set<Set<Stop>> subGraphs = new HashSet<>();

        // Use depth-first search to find all the stops in each subgraph
        for (Stop stop : graph.getStops()) {
            if (!visited.contains(stop)) {
                Set<Stop> subGraph = new HashSet<>();
                findSubGraph(stop, visited, subGraph);
                subGraphs.add(subGraph);
            }
        }
        return subGraphs;
    }

    /**
     * Helper method using depth-first search to find all stops in the sub-graph.
     * @param stop The current stop-
     * @param visited A set of visited stops.
     * @param subGraph A set representing the current sub-graph.
     */
    private static void findSubGraph(Stop stop, Set<Stop> visited, Set<Stop> subGraph) {
        visited.add(stop);
        subGraph.add(stop);

        for (Stop neighbour : stop.getNeighbours()) {
            if (!visited.contains(neighbour)) {
                findSubGraph(neighbour, visited, subGraph);
            }
        }
    }

    /**
     * Finds articulation points in the given graph.
     * @param graph The input graph.
     * @return A collection of articulation points.
     */
    public static Collection<Stop> findArticulationPoints(Graph graph) {
        System.out.println("calling findArticulationPoints");
        graph.computeNeighbours();   // To ensure that all stops have a set of (undirected) neighbour stops

        Map<Stop, Integer> depths = new HashMap<>(); // A map to store the depth of each visited stop
        Set<Stop> articulationPoints = new HashSet<>(); // A set to store the articulation points found

        // Find all the sub graphs
        Set<Set<Stop>> subGraphs = findSubGraphs(graph);

        // Set all depths to infinity (i.e., unvisited)
        for (Stop n : graph.getStops()) {
            depths.put(n, Integer.MAX_VALUE);
        }

        // Apply the articulation points algorithm to each subgraph
        for (Set<Stop> subGraph : subGraphs) {
            // Choose the first stop in the subgraph as the starting node
            Stop startStop = subGraph.iterator().next();

            // Run the depth-first search algorithm and find articulation points
            int subtreeID = 0;
            depths.put(startStop, 0); // Set the depth of the starting node to 0
            for (Stop neighbour : startStop.getNeighbours()) {
                if (depths.get(neighbour) == Integer.MAX_VALUE) { // If the neighbour has not been visited yet
                    // Recursively find articulation points in the subtree rooted at the neighbour
                    recordAPoints(neighbour, 1, startStop, depths, articulationPoints);
                    subtreeID++;
                }
            }

            // If starting node has more than 1 subtree, it is an articulation point
            if (subtreeID > 1) {
                articulationPoints.add(startStop);
            }
        }

        return articulationPoints;
    }

    /**
     * Recursively find articulation points in the subtree rooted at the node.
     * @param stop The current stop.
     * @param depth The depth of the current stop.
     * @param fromNode The parent node.
     * @param depths A map of depths for each visited stop.
     * @param articulationPoints A set to store the articulation points found.
     * @return The reach-back value for the current stop.
     */
    private static int recordAPoints(Stop stop, int depth, Stop fromNode, Map<Stop, Integer> depths, Set<Stop> articulationPoints){
        // Set the depth of the node and the reach back value to the current depth
        depths.put(stop, depth);
        int reachBack = depth;

        // Check all the neighbours of the node
        for(Stop neighbour: stop.getNeighbours()){

            // Skip the parent node
            if (neighbour != fromNode) {
                int d = depths.get(neighbour); // Depth of the node
                if (d < Integer.MAX_VALUE) {
                    // If the neighbor has already been visited
                    reachBack = Math.min(reachBack, d);
                } else {
                    // If the neighbor hasn't been visited yet
                    // Recursively find articulation points in the subtree rooted at the neighbor
                    int childReach = recordAPoints(neighbour, depth + 1, stop, depths, articulationPoints);
                    reachBack = Math.min(childReach, reachBack);
                    // If the subtree rooted at neighbor has no connection to ancestors of node (except fromNode), then node is an articulation point
                    if (childReach >= depth && stop != fromNode) {
                        articulationPoints.add(stop);
                    }
                }
            }
        }

        return reachBack;
    }

}
