import ecs100.*;
import java.awt.*;
import java.util.Random;
import javax.swing.*;

/**
 * A simple drawing program.
 * The user can select from a variety of tools and options using the buttons and
 *   elements down the left side, and can use the mouse to add elements to the drawing
 *   according to the current tool and options
 */


public class MiniPaint{
    private Tools tool = Tools.Line;   // the current tool, governing what the mouse will do.
                                    // Initial value is "Line";  changed by the buttons.

    // Enumeration of different tools
    enum Tools{Line, Rectangle, Oval, Caption, Eraser, Image, Draw, Flower}

    double x1, x2, y1, y2; // Start and End coordinates
    double top, left, width, height; // Size of the shape
    boolean filled = false;
    String caption;
    Color lineColor = this.randomColor();
    Color fillColor = this.randomColor();
    JButton filledButton;
    JButton lineColorButton;
    JButton fillColorButton;
    double brushSize = 11;
    String fileName; // Path of the image that will be drawn

    /**
     * Sets up the graphical user interface
     */
    public void setupGUI(){
        // UI buttons for various tools and actions
        UI.addButton("Clear", UI::clearGraphics);
        UI.addButton("Line", ()->{tool = Tools.Line;});
        UI.addButton("Rectangle", ()->{tool = Tools.Rectangle;});
        UI.addButton("Oval", ()->{tool = Tools.Oval;});
        UI.addTextField("Caption", (String s) ->{caption = s ; tool = Tools.Caption;});
        UI.addButton("Image", ()->{fileName = UIFileChooser.open(); tool = Tools.Image;});
        UI.addButton("Eraser", ()->{tool = Tools.Eraser;});
        UI.addButton("Flower", ()->{tool = Tools.Flower;});
        filledButton = UI.addButton("Not filled", this::setFilled);
        UI.addButton("Free Draw", ()->{tool = Tools.Draw;});

        // UI buttons for setting colors
        lineColorButton = UI.addButton("Line Color", this::setLineButtonColor);
        lineColorButton.setBackground(lineColor);
        lineColorButton.setSize(5, 3);
        fillColorButton = UI.addButton("Fill Color", this::setFillButtonColor);
        fillColorButton.setBackground(fillColor);

        // UI sliders for setting line width, text size, and brush size
        UI.addSlider("Line Width", 1, 19, 1, UI::setLineWidth);
        UI.addSlider("Text Size", 8, 40, 8, UI::setFontSize);
        UI.addSlider("Brush Size", 1, 40, brushSize, this::setDiam);

        // Mouse listeners
        UI.setMouseListener(this::doMouse);
        UI.setMouseMotionListener(this::doMouse);

        // Quit button and layout settings
        UI.addButton("Quit", UI::quit);
        UI.setDivider(0.0);  // Hide the text area.
    }
    
    /**
     * Respond to mouse events
     * When pressed, remember the position.
     * When released, draw what is specified by current tool
     */
    public void doMouse(String action, double x, double y) {
        if(action.equals("pressed")){
            x1 = x;
            y1 = y;
        }else if (action.equals("dragged") && tool == Tools.Eraser){
                erase(x, y,12);
        } else if(action.equals("dragged") && tool == Tools.Draw) {
            freeDraw(x, y, brushSize);
        }else if (action.equals("released")){
            x2 = x;
            y2 = y;

            switch(tool){
                case Line:
                    drawALine(x1, y1, x2, y2);
                    break;
                case Rectangle:
                    drawARectangle(x1, y1, x2, y2);
                    break;
                    case Oval:
                    drawAnOval(x1, y1, x2, y2);
                    break;
                case Caption:
                    drawACaption(x2, y2);
                    break;
                case Image:
                    drawAnImage(x1, y1, x2, y2);
                    break;
                case Flower:
                    drawAFlower(x1, y1, Math.hypot(x1 - x2, y1 - y2));
                    break;
            }
        }
    }

    /**
     * Generates a random color
     */
    public Color randomColor(){
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        return new Color(r, g, b);
    }

    /**
     * Sets the top-left coordinates, width, and height based on two points
     */
    public void setTopLeftWidthHeight(double x1, double y1, double x2, double y2){
        left = Math.min(x1, x2);
        top = Math.min(y1, y2);
        width = Math.abs(x1 - x2);
        height = Math.abs(y1 - y2);
    }

    /**
     * Draws a circle centered where the mouse was pressed
     */
    public void drawCircle(double xCenter, double yCenter, double radius){
        double top = yCenter - radius;
        double left = xCenter - radius;
        double diameter = radius * 2;
        UI.fillOval(left, top, diameter, diameter);
    }

    /**
     * Erases a circular area at the specified coordinates with a given diameter
     */
    public void erase(double x, double y, double diam){
        UI.setColor(Color.WHITE);
        drawCircle(x, y, diam);
    }

    /**
     * Follows the mouse pattern and draws with the selected color and the selected brush size
     */
    public void freeDraw(double x, double y, double diam){
        UI.setColor(lineColor);
        drawCircle(x, y, diam);
    }

    /**
     * Sets the diameter of the brush for the free drawing tool
     */
    public void setDiam(double diam){
        brushSize = diam;
    }

    /**
     * Toggles the filled status for shapes that support filling
     */
    public void setFilled(){
        if(filled){
            filledButton.setText("Not filled");
        }else{
            filledButton.setText("Filled");
        }
        filled = !filled;
    }

    /**
     * Lets the user choose a color and then changes the button color to the chosen color
     */
    public void setLineButtonColor(){
        lineColor = JColorChooser.showDialog(null, "Line Color", lineColor);
        lineColorButton.setBackground(lineColor);
    }

    /**
     * Lets the user choose a color and then changes the button color to the chosen color
     */
    public void setFillButtonColor(){
        fillColor = JColorChooser.showDialog(null, "Fill Color", fillColor);
        fillColorButton.setBackground(fillColor);
    }

    /**
     * Draws a line between the two points
     */
    public void drawALine(double x1, double y1, double x2, double y2){
        UI.setColor(lineColor);
        UI.drawLine(x1, y1, x2, y2);
    }

    /**
     * Draws a rectangle between two diagonal points. (Filled or just the outline)
     */
    public void drawARectangle(double x1, double y1, double x2, double y2){
        setTopLeftWidthHeight(x1, y1, x2, y2);
        if(!filled) {
            UI.setColor(lineColor);
            UI.drawRect(left, top, width, height);
        }else {
            UI.setColor(lineColor);
            UI.drawRect(left, top, width, height);
            UI.setColor(fillColor);
            UI.fillRect(left, top, width, height);
        }
    }

    /**
     * Draws an oval between two diagonal points. (Filler or just the outline)
     */
    public void drawAnOval(double x1, double y1, double x2, double y2){
        setTopLeftWidthHeight(x1, y1, x2, y2);
        if(!filled) {
            UI.setColor(lineColor);
            UI.drawOval(left, top, width, height);
        }else {
            UI.setColor(lineColor);
            UI.drawOval(left, top, width, height);
            UI.setColor(fillColor);
            UI.fillOval(left, top, width, height);
        }
    }

    /**
     * Draws a caption at the specified coordinates.
     */
    public void drawACaption(double x, double y){
        UI.setColor(lineColor);
        UI.drawString(caption, x, y);
    }

    /**
     * Draws the current image between the two diagonal corners, unless
     *  they are very close, and then just draws the image at its natural size
     */
    public void drawAnImage(double x1, double y1, double x2, double y2){
        setTopLeftWidthHeight(x1, y1, x2, y2);
        if(width <= 5 || height <= 5){
            UI.drawImage(fileName, left, top);
        }else{
            UI.drawImage(fileName, left, top, width, height);
        }
    }

    /**
     * Draws a flower with 6 petals at the specified coordinates with a given radius.
     */
    public void drawAFlower(double x, double y, double radius){
        double petalX, petalY;
        UI.setColor(Color.YELLOW);
        drawCircle(x, y, radius);
        UI.setColor(fillColor);

        for(int i = 0; i < 6; i++){
            petalX = x + Math.cos((Math.PI / 3) * i) * (radius * 2);
            petalY = y + Math.sin((Math.PI / 3) * i) * (radius * 2);
            drawCircle(petalX, petalY, radius);
        }
    }

    public static void main(String[] arguments){
        MiniPaint mp = new MiniPaint();
        mp.setupGUI();
    }
}
