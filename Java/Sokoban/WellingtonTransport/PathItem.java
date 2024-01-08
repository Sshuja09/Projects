/**
 * This class represents a partial path in A* search or Dijkstra search algorithms.
 * Each instance of PathItem contains information about a specific stop in the path,
 * including the associated edge, and total cost to reach this stop so far,
 * and the estimated total cost from this stop to the goal.
 */
public class PathItem implements Comparable<PathItem> {
    private Stop node; // The stop/node associated with this partial path.
    private Edge edge; // The edge connecting the previous stop to this stop.
    private double totalCost; // The total cost to reach this stop from the start.
    private double estimateCost; // The estimated total cost from this stop to the goal.

    /**
     * Constructs a PathItem with the specified stop, edge, total cost, and estimated cost.
     * @param stop The stop/node associated with this partial path.
     * @param edge The edge connection the previous stop to this stop.
     * @param totalCost The total cost to reach this stop from the start.
     * @param estimatedCost The estimated total cost from this stop to the goal.
     */
    public PathItem(Stop stop, Edge edge, Double totalCost, Double estimatedCost) {
        this.node = stop;
        this.edge = edge;
        this.totalCost = totalCost;
        this.estimateCost = estimatedCost;
    }

    /**
     * Gets the stop associated with this partial path.
     * @return The stop/node associated with this partial path.
     */
    public Stop getStop() {
        return node;
    }

    /**
     * Gets the edge connecting the previous stop to this stop.
     * @return The edge connecting the previous stop to this stop.
     */
    public Edge getEdge() {
        return edge;
    }

    /**
     * Gets the total cost to reach this stop from the start.
     * @return The total cost to reach this stop from the start.
     */
    public double getCost() {
        return totalCost;
    }

    /**
     * Gets the estimated total cost from this stop to the goal.
     * @return The estimated total cost from this stop to the goal.
     */
    public double geEstimate() {
        return estimateCost;
    }

    /**
     * Compares this PathItem to another based on their estimated total costs.
     * @param other The other PathItem to compare to.
     * @return 1 if this has a greater estimated cost, -1 if less, and 0 if equal.
     */
    @Override
    public int compareTo(PathItem other) {
        return Double.compare(this.estimateCost, other.estimateCost);
    }

}
