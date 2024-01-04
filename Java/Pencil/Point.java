import java.awt.*;

/**
 * This class represents a point in a drawing with specified coordinates, width, and color.
 */
public class Point {

    double x, y, width; // X-coordinate, Y-coordinate, and the width of the point
    Color lineColor; // Color of the line representing the point

    /**
     * Constructs a Point with the specified coordinates, width, and color.
     * @param x The X-coordinate of the point
     * @param y The Y-coordinate of the point
     * @param width The width of the point (used for drawing)
     * @param color The color of the line representing the point
     */
    public Point(double x, double y, double width, Color color){
        this.x = x;
        this.y = y;
        this.width = width;
        this.lineColor = color;
    }

    /**
     * Gets the X-coordinate of the point.
     * @return The X-coordinate of the point.
     */
    public double getX() {
        return x;
    }

    /**
     * Gets the Y-coordinate of the point.
     * @return The Y-coordinate of the point.
     */
    public double getY() {
        return y;
    }

    /**
     * Gets the width of the point
     * @return The width of the point
     */
    public double getWidth() { return width; }

    /**
     * Gets the color of the line representing the point.
     * @return The color of the line representing the point.
     */
    public Color getColor() { return lineColor; }

}
