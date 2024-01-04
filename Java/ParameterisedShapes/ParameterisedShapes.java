import ecs100.*;
import java.awt.Color;
import javax.swing.JColorChooser;

/**
 * The ParameterisedShapes class allows the user to draw simple and fancy flags with
 * customizable colors and sized using the UI class.
 */
public class ParameterisedShapes{

    // Constants for width and height
    public static final double FLAG_WIDTH = 200; // The width of the flag
    public static final double FLAG_HEIGHT = 133; // The height of the flag

    /**
     * Prompts the user to input the position and color for a simple three-stripe flag,
     * then calls the drawThreeStripesFlag method to draw the flag
     */
    public void doSimpleFlag(){
        double left = UI.askDouble("Left of flag");
        double top = UI.askDouble("Top of flag");
        UI.println("Now choose the colours");
        Color stripe1 = JColorChooser.showDialog(null, "First Stripe", Color.white);
        Color stripe2 = JColorChooser.showDialog(null, "Second Stripe", Color.white);
        Color stripe3 = JColorChooser.showDialog(null, "Third Stripe", Color.white);
        this.drawThreeStripesFlag(left, top, stripe1, stripe2, stripe3);
    }

    /**
     * Draws a simple three-stripe flag with specified colors.
     * @param left    The left coordinate of the flag
     * @param top     The right coordinate of the flag
     * @param stripe1 Color of the first stripe
     * @param stripe2 Color of the second stripe
     * @param stripe3 Color of the third stripe
     */
    public void drawThreeStripesFlag(double left, double top, Color stripe1, Color stripe2, Color stripe3){
        UI.clearGraphics();
        double stripeLength = FLAG_HEIGHT / 3.0;
        double[] stripeYCoordinates = {top, top + stripeLength, top + stripeLength * 2};
        Color [] stripeColors = {stripe1, stripe2, stripe3};
        for(int i = 0; i<stripeColors.length; i++){
            UI.setColor(stripeColors[i]);
            UI.fillRect(left, stripeYCoordinates[i], FLAG_WIDTH, stripeLength);
        }
    }

    /**
     * Prompts the user to input the position, color, and sizes for a fancy flag,
     * then calls the drawFancyFlag method to draw the flag
     */
    public void doFancyFlag(){
        double left = UI.askDouble("Left of flag");
        double top = UI.askDouble("Top of flag");
        UI.println("Now choose the colours");
        Color col1 = JColorChooser.showDialog(null, "First Stripe", Color.white);
        Color col2 = JColorChooser.showDialog(null, "Second Stripe", Color.white);
        Color col3 = JColorChooser.showDialog(null, "Third Stripe", Color.white);
        UI.println("Now choose the sizes");
        drawFancyFlag(left, top, col1, col2, col3);

    }

    /**
     * Draws a fancy flag with specified colors and sizes.
     * @param left The left coordinate of the flag
     * @param top  The top coordinate of the flag
     * @param col1 Color of the first stripe
     * @param col2 Color of the second stripe
     * @param col3 Color of the third stripe
     */
    public void drawFancyFlag(double left, double top, Color col1, Color col2, Color col3){
        UI.clearGraphics();
        double stripe1Height = UI.askDouble("Enter the height of the 1st stripe: ");
        double stripe2Height = UI.askDouble("Enter the height of the 2nd stripe: ");
        double stripe3Height = UI.askDouble("Enter the height of the 3rd stripe: ");

        boolean filled = UI.askBoolean("Are the circles filled? ");

        // Calculate position and dimensions for circles and stripes
        double stripe2Y = top + stripe1Height;
        double stripe3Y = top + stripe2Height + stripe1Height;
        double width = (stripe1Height + stripe2Height + stripe3Height) * 1.5;

        double portionOfFlag = width / 3;

        double circle1Diameter = stripe1Height * 0.2;
        double circle1X = left + (portionOfFlag - circle1Diameter) / 2;
        double circle1Y = top + (stripe1Height - circle1Diameter) / 2;

        double circle2Diameter = stripe2Height * 0.2;
        double circle2Radius = circle1Diameter * 0.5;
        double circle2X =circle1X + portionOfFlag - circle2Diameter / 2;
        double circle2Y = stripe2Y + (stripe2Height / 2) - circle2Radius;

        double circle3Diameter = stripe3Height * 0.2;
        double circle3Radius = circle3Diameter * 0.5;
        double circle3X = (left + width - (portionOfFlag  / 2 )- circle3Radius) ;
        double circle3Y = stripe3Y + (stripe3Height / 2) - circle3Radius;

        // Draw the flag
        drawStripe(col1, left, top, width, stripe1Height, circle1X, circle1Y, circle1Diameter, filled);
        drawStripe(col2, left, stripe2Y, width, stripe2Height, circle2X, circle2Y, circle2Diameter, filled);
        drawStripe(col3, left, stripe3Y, width, stripe3Height, circle3X, circle3Y, circle3Diameter, filled);
    }

    /**
     * Draws a stripe with a circle on it
     * @param col          Color of the stripe.
     * @param left         The left coordinate of the stripe.
     * @param top          The top coordinate of the stripe.
     * @param width        The width of the stripe.
     * @param stripeHeight The height of the stripe.
     * @param circleX      The x-coordinate of the circle.
     * @param circleY      The y-coordinate of the circle.
     * @param diameter     The diameter of the circle.
     * @param filled      Whether the circle should be filled.
     */
    public void drawStripe(Color col, double left, double top, double width, double stripeHeight, double circleX,
                           double circleY, double diameter, boolean filled){
        UI.setColor(col);
        UI.fillRect(left, top, width, stripeHeight);

        UI.setColor(Color.BLACK);

        if (filled){
            UI.fillOval(circleX, circleY, diameter, diameter);
        }else{
            UI.drawOval(circleX, circleY, diameter, diameter);
        }
    }

    /**
     * Sets up the graphical user interface with buttons for different actions.
     */
    public void setupGUI(){
        UI.initialise();
        UI.addButton("Clear", UI::clearPanes );
        UI.addButton("Simple Flag", this::doSimpleFlag );
        UI.addButton("Fancy Flag", this::doFancyFlag );
        UI.addButton("Quit", UI::quit );
    }

    public static void main(String[] args){
        ParameterisedShapes ps = new ParameterisedShapes ();
        ps.setupGUI();
    }

}
