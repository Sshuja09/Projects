import ecs100.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * The Pencil class represents a simple drawing application with features like drawing,
 * undo, redo, changing line color, and line width
 */
public class Pencil{
    private double lastX;
    private double lastY;
    private double lineWidth = 3;
    Color lineColor = Color.BLACK;
    Stack<Stroke> strokeHistory = new Stack<>();
    Stack<Stroke> redoStack = new Stack<>();
    Stroke currentStroke;

    /**
     * Sets up the graphical user interface, including mouse event listeners and buttons
     */
    public void setupGUI(){
        UI.setMouseMotionListener(this::doMouse);
        UI.addButton("Quit", UI::quit);
        UI.addButton("Undo", this::undo);
        UI.addButton("Color", this::setColor);
        UI.addButton("Redo", this::redo);
        UI.addSlider("Width", 4, 10, this::setLineWidth);
        UI.setLineWidth(3);
        UI.setDivider(0.0);
    }

    /**
     * Handles mouse events for drawing, including pressed, dragged, and released actions
     * @param action The type of mouse action (pressed, dragged, released)
     * @param x The X-coordinate of the mouse
     * @param y The Y-coordinate of the mouse
     */
    public void doMouse(String action, double x, double y) {
        switch (action) {
            // Initialize a new stroke on mouse press
            case "pressed" -> {
                lastX = x;
                lastY = y;
                currentStroke = new Stroke();
                currentStroke.addPoint(x, y, lineWidth, lineColor);
            }
            case "dragged" -> {
                // Draw a line and update current stroke on mouse drag
                UI.drawLine(lastX, lastY, x, y);
                lastX = x;
                lastY = y;
                currentStroke.addPoint(x, y, lineWidth, lineColor);
            }
            case "released" -> {
                // Draw a line, update current stroke, and add it to history on mouse release
                UI.drawLine(lastX, lastY, x, y);
                currentStroke.addPoint(x, y, lineWidth, lineColor);
                strokeHistory.push(currentStroke);
                redoStack.clear();
            }
        }
    }

    /**
     * Undoes the last drawing action by removing the last stroke from the history
     */
    public void undo(){
        if(strokeHistory.empty()){return;}
        // Clear the canvas, undo the last stroke, and redraw remaining strokes
        UI.clearGraphics();
        redoStack.push(strokeHistory.pop());

        drawStrokes(strokeHistory);
    }

    /**
     * Redoes the last undone drawing action by retrieving the last stroke from the redo stack
     */
    private void redo() {
        if(redoStack.empty()){return;}
        // Redo the last undone stroke and push in back to the history
        Stroke stroke = redoStack.pop();
        drawStroke(stroke);
        strokeHistory.push(stroke);
    }

    /**
     * Opens a color chooser dialog ad sets the line color to the selected color
     */
    private void setColor() {
        lineColor = JColorChooser.showDialog(null, "Select a color", lineColor);
        UI.setColor(lineColor);
    }

    /**
     * Sets the line width to the specified width
     * @param width The new line width
     */
    private void setLineWidth(double width) {
        lineWidth = width;
        UI.setLineWidth(width);
    }

    /**
     * Draws a list of strokes on the canvas.
     * @param stack The stack of strokes to be drawn
     */
    public void drawStrokes(Stack<Stroke> stack){
        for(Stroke st: stack){
            drawStroke(st);
        }
    }

    /**
     * Draws a stroke on the canvas.
     * @param stroke The stroke to be drawn
     */
    public void drawStroke(Stroke stroke){
        List<Point> points = stroke.getPoints();
        Point p1, p2;
        for (int i = 0; i < points.size() - 1; i++){
            p1 = points.get(i);
            p2 = points.get(i+1);
            UI.setLineWidth(p1.getWidth());
            UI.setColor(p1.getColor());
            UI.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
        }
    }

    public static void main(String[] arguments){
        new Pencil().setupGUI();
    }

}