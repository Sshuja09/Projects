import ecs100.*;
import java.awt.Color;

public class PatternsDrawer{

    // Constants for the pattern display
    public static final double PATTERN_LEFT = 50.0;
    public static final double PATTERN_TOP = 50.0;
    public static final double PATTERN_SIZE = 300.0;

    /**
     * Draw a row of alternation colored rectangles
     */
    public void drawRow(){
        UI.clearGraphics();
        double num = UI.askInt("How many rectangles:");
        double newX = PATTERN_LEFT;
        double boxSize = PATTERN_SIZE / num;

        // Loop through each rectangle in the row
        for(int i = 0; i < num; i++){
            // Alternate colors based on even or odd index
            if(i % 2 == 0){
             UI.setColor(Color.blue);
            }else{
             UI.setColor(Color.orange);
            }
            // Draw the rectangle
            UI.fillRect(newX, PATTERN_TOP, boxSize, boxSize);
            newX += boxSize;
        }
    }

    /**
     * Draw a checkered draughts board
     */
    public void drawDraughtsBoard(){
        UI.clearGraphics();
        UI.setColor(Color.black);
        int num = UI.askInt("How many rows:");
        double boxSize = PATTERN_SIZE / num;
        double x = PATTERN_LEFT;

        // Loop through each row
        for(int row = 0; row < num; row++){
            double y = PATTERN_TOP;
            // Loop through each column in the row
            for(int column = 0; column < num; column++){
                // Alternate colors based on row and column indices
                if(row % 2 == 0 && column % 2 == 0 || row % 2 != 0 && column % 2 != 0){
                    UI.drawRect(x, y, boxSize, boxSize);
                }else{
                    UI.fillRect(x, y, boxSize, boxSize);
                }
                y += boxSize;
            }
            x += boxSize;
        }
    }

    /**
     * Draw a triangular grid
     */
    public void drawTriGrid(){
        UI.clearGraphics();
        UI.setColor(Color.black);
        int num = UI.askInt("How many rows:");
        double boxSize = PATTERN_SIZE / num;
        double rows = num;
         double y = PATTERN_TOP;

         // Loop through each column
        for(int column = 0; column < num; column++){
            double x = PATTERN_LEFT;
            // Loop through each row in the column
            for(int row = 0; row < rows; row++){
                // Draw a filled rectangle
                UI.fillRect(x, y, boxSize, boxSize);
                x += boxSize * 1.2;
            }
            rows -= 1;
            y += boxSize * 1.2;
        }
    }

    /**
     * Draw a circle from its center
     * @param xCenter X coordinate of the center
     * @param yCenter Y coordinate of the center
     * @param radius Radius of the circle
     */
    public void drawFromCenter(double xCenter, double yCenter, double radius){
        UI.drawOval(xCenter - radius, yCenter - radius, radius*2, radius*2);
    }

    /**
     * Draw a pattern of concentric circles
     */
    public void drawCircle(){
        UI.clearGraphics();
        UI.setColor(Color.black);
        int num = UI.askInt("How many rows:");
        double y = PATTERN_TOP;

        // Loop through each column
        for(int column = 0; column < num; column++){
            double circleSize = PATTERN_SIZE / num;
            double x = PATTERN_LEFT;
            // Loop through each row in the column
            for(int row = 0; row < num; row++){
                // Draw circles with decreasing size
                while(circleSize > 0) {
                    drawFromCenter(x, y, circleSize/ 2);
                    circleSize -= 3;
                }
                // Reset circle size for the next row
                circleSize = PATTERN_SIZE / num;
                x += circleSize;
            }
            y += circleSize;
        }
    }


    /**
     * Sets up the graphical user interface with buttons.
     */
    public void setupGUI(){
        UI.initialise();
        UI.addButton("Clear",UI::clearPanes);
        UI.addButton("Draw Row", this::drawRow);
        UI.addButton("Draw Draughts", this::drawDraughtsBoard);
        UI.addButton("Draw TriGrid", this::drawTriGrid);
        UI.addButton("Draw Circles", this::drawCircle);
        UI.addButton("Quit",UI::quit);
    }   

    public static void main(String[] arguments){
        PatternsDrawer pd = new PatternsDrawer();
        pd.setupGUI();
    }

}

