import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The class represents a stroke of points with specific coordinates, width, and color.
 * Each stroke is a collection of points that together form a line or a curve
 */
public class Stroke {

    // List to store points in the stroke
    List<Point> points;

    /**
     * Constructor a new Stroke object with an empty list of points
     */
    public Stroke(){
        this.points = new ArrayList<Point>();
    }

    /**
     * Adds a point to the stroke with specified x and y coordinates, width, and color.
     *
     * @param x      The x-coordinate of the point.
     * @param y      The y-coordinate of the point.
     * @param width  The width of the point (or line).
     * @param color  The color of the point.
     */
    public void addPoint(double x, double y, double width, Color color){
        this.points.add(new Point(x, y, width, color));
    }

    /**
     * Gets the list of points in the stroke
     * @return A list of points forming the stroke
     */
    public List<Point> getPoints(){
        return this.points;
    }

}



