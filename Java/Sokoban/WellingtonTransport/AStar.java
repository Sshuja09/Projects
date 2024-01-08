/**
 * Implements the A* search algorithm to find the shortest path
 * in a graph between a start node and a goal node.
 * It returns a Path consisting of a list of Edges that will
 * connect the start node to the goal node.
 */

import java.util.*;


public class AStar {


    private static String timeOrDistance = "distance";    // way of calculating cost: "time" or "distance"

    /**
     * Finds the shortest path between two stops using A* search algorithm.
     * @param start The starting stop.
     * @param goal The goal stop.
     * @param timeOrDistance The way of calculating cost: "time" or "distance".
     * @return A list of Edges representing the shortest path from start to goal.
     */
    public static List<Edge> findShortestPath(Stop start, Stop goal, String timeOrDistance) {
        if (start == null || goal == null) {return null;}
        AStar.timeOrDistance= (timeOrDistance.equals("time"))?"time":"distance";

        // Initialize the priority fringe and visited set
        PriorityQueue<PathItem> fringe = new PriorityQueue<>();
        HashSet<Stop> visited = new HashSet<>();

        // Initialize the back-pointer map
        Map<Stop, Edge> backPointer = new HashMap<>();

        // Initialize the starting node
        PathItem startItem = new PathItem(start, null, 0.0, heuristic(start, goal));

        // Add the starting node
        fringe.add(startItem);

        // Start the search
        while(!fringe.isEmpty()){
            //Get the node with smallest f value from the fringe
            PathItem currentPathItem = fringe.poll();
            Stop currentStop = currentPathItem.getStop();

            //If the node is not visited
            if(!visited.contains(currentStop)) {
                // Add the node to the visited set
                visited.add(currentStop);
                backPointer.put(currentStop, currentPathItem.getEdge()); // Map the stop to the edge from which it came from
            }

            // If the current node is the goal, return the path
            if(currentStop.equals(goal)){
                return reconstructPath(start, goal, backPointer);
            }

            // Otherwise, expand the current node and add its neighbors to the fringe
            for(Edge edge: currentStop.getForwardEdges()){
                Stop neighbor = edge.toStop();

                if(!visited.contains(neighbor)){
                    //Calculate the cost of reaching the neighbor form the current node
                    double total = currentPathItem.getCost() + edgeCost(edge); // Cost from start to neighbour
                    double estimate = total + heuristic(neighbor, goal); // Total estimated cost

                    // Create a new PathItem object for the neighbour
                    fringe.add(new PathItem(neighbor, edge, total, estimate));
                }
            }
        }

        // if we've exhausted all the possible paths and haven't found the goal, return null
        return null;
    }

    /**
     * Reconstructs the path from the goal to the start using back-pointers.
     * @param start The starting stop.
     * @param goal The goal stop.
     * @param backPointers The map of stop to edge back-pointers.
     * @return A list of Edges representing the reconstructed path.
     */
    static List<Edge> reconstructPath(Stop start, Stop goal, Map<Stop, Edge> backPointers) {
        List<Edge> path = new ArrayList<>();
        Stop current = goal;

        // Traverse back from goal to start using the back-pointers map
        while(!current.equals(start)){
            path.add(backPointers.get(current));
            current = backPointers.get(current).fromStop();
        }

        // Reverse the order of the edges in the path list to get the correct order
        Collections.reverse(path);
        return path;
    }

    /** Return the heuristic estimate of the cost to get from a stop to the goal.
     * @param current The current stop.
     * @param goal The goal stop.
     * @return The heuristic estimate of the cost.
     */
    public static double heuristic(Stop current, Stop goal) {
        if (timeOrDistance =="distance"){ return current.distanceTo(goal);}
        else if (timeOrDistance =="time"){return current.distanceTo(goal) / Transport.TRAIN_SPEED_MPS;}
        else {return 0;}
    }

    /**
     * Return the cost of traversing an edge in the graph
     * @param edge The edge to calculate the cost for.
     * @return The cost of traversing the edge.
     */
    public static double edgeCost(Edge edge){
        if (timeOrDistance =="distance"){ return edge.distance();}
        else if (timeOrDistance =="time"){return edge.time();}
        else {return 1;}
    }

}
