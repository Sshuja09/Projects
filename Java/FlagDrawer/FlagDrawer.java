import ecs100.*;
import java.awt.Color;

/**
 * You can find lots of flag details (including the correct dimensions and colours)
 * from  http://www.crwflags.com/fotw/flags/    
 */

/**
 * FlagDrawer class allows the user to draw various flags using the UI class
 */
public class FlagDrawer{

    public static final double LEFT = 100;  // the left side of the flags
    public static final double TOP = 50;    // the top of the flags

    /**
     * Draws the flag of Belgium
     */
    public void drawBelgiumFlag(){
        UI.clearGraphics();
        UI.println("Belgium Flag");
        double width = UI.askDouble("How wide: ");
        double length = width * (13.0/15);
        double portionWidth = width / 3;

        // Draw the three vertical colored portions of the flag
        UI.setColor(Color.BLACK);
        UI.fillRect(LEFT, TOP, portionWidth, length);
        UI.setColor(Color.YELLOW);
        UI.fillRect((LEFT + portionWidth), TOP, portionWidth, length);
        UI.setColor(Color.RED);
        UI.fillRect((LEFT + (portionWidth * 2)), TOP, portionWidth, length);
    }

    /**
     * Draws the Red Cross flag
     */
    public void drawRedCrossFlag() {
        UI.println("Red Cross Flag: ");
        UI.clearGraphics();
        double size = UI.askDouble("How wide: ");
        double verticalX = LEFT + size * 0.4;
        double verticalY = TOP + size * 0.2;
        double horizontalX = LEFT + size * 0.2;
        double horizontalY = TOP + size * 0.4;

        double crossWidth = size * 0.2;
        double crossLength = size * 0.6;

        // Draws the background and the red cross
        UI.setColor(Color.BLACK);
        UI.drawRect(LEFT, TOP, size, size);
        UI.setColor(Color.RED);
        UI.fillRect(verticalX, verticalY, crossWidth, crossLength);
        UI.fillRect(horizontalX, horizontalY, crossLength, crossWidth);
    }

    /**
     * Draws the Pacman flag
     */
    public  void drawPacman() {
        UI.clearGraphics();        
        UI.println("Pacman Flag");
        double width = UI.askDouble("How wide: ");
        double length = width * 1.7;
        double diameter = width * 0.15;
        double middle = LEFT + ((width - diameter) / 2.0);
        double pacmanSize = diameter * 3;
        double pacmanMiddle = LEFT + ((width - pacmanSize) / 2.0);

        // Draw the background and three colored circles
        UI.setColor(Color.BLACK);
        UI.fillRect(LEFT, TOP, width, length);
        UI.setColor(Color.BLUE);
        UI.fillOval(middle, (length * 0.2), diameter, diameter);
        UI.setColor(Color.GREEN);
        UI.fillOval(middle, (length * 0.4), diameter, diameter);
        UI.setColor(Color.YELLOW);
        UI.fillOval(middle, (length * 0.6), diameter, diameter);
        // Draw PacMan
        UI.setColor(Color.RED);
        UI.fillArc(pacmanMiddle, (length * 0.76), pacmanSize, pacmanSize, 125, 290);
    }

    /**
     * Draw the flag of Greenland
     */
    public void drawGreenlandFlag() {
        UI.clearGraphics();
        UI.println("Greenland Flag");
        double width = UI.askDouble("How wide: ");
        double length = width * 0.8;
        double mid = length / 2;
        double diameter = width * 0.5;
        double circleX = LEFT + 30;
        double radius = diameter / 2;
        double circleY = (TOP + mid) - radius;

        UI.setLineWidth(4);
        //Draw the background, bottom half, and the two half circles
        UI.setColor(Color.BLACK);
        UI.drawRect(LEFT, TOP, width, length);
        UI.setColor(Color.RED);
        UI.fillRect(LEFT,TOP + mid, width, mid);
        UI.fillArc(circleX, circleY, diameter, diameter, 0, 180);
        UI.setColor(Color.WHITE);
        UI.fillArc(circleX, circleY, diameter, diameter, 180, 180);
    }

    /**
     * Sets up the GUI with buttons for each flag drawing method
     */
    public void setupGUI(){
        UI.addButton("Clear", UI::clearPanes);
        UI.addButton("Flag of Belgium", this::drawBelgiumFlag);
        UI.addButton("Red Cross Flag",  this::drawRedCrossFlag);
        UI.addButton("Pacman Flag", this::drawPacman);
        UI.addButton("Flag of Greenland", this::drawGreenlandFlag);
        UI.addButton("Quit", UI::quit);
        UI.setDivider(0.3);
    }

    public static void main(String[] arguments){
        FlagDrawer fd = new FlagDrawer();
        fd.setupGUI();
    }

}
