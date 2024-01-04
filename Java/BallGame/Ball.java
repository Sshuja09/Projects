import ecs100.*;
import java.awt.Color;
import java.util.Random;

/** Ball represents a ball that is launched by the mouse towards a direction.
 *    Each time the step() method is called, it will take one step.
 *    The ball also has a gravity effect that reduces its vertical speed
 */

public class Ball{

    // Constants for the ball's diameter, ground level, and right end limit
    public static final double DIAM = 16;
    public static final double GROUND = BallGame.GROUND;
    public static final double RIGHT_END = BallGame.RIGHT_END;


    double xPos; // x-coordinate of the ball's current position
    double yPos; // y-coordinate of the ball's current position
    double stepX; // Horizontal speed of the ball (Change in x-coordinate per time step)
    double stepY; // Vertical speed of the ball (Change in y-coordinate per time step)
    Color randomColor; // Color of the ball, randomly generated

    /**
     * Constructor for the Ball class.
     * @param x Initial x-coordinate of the ball
     * @param y Initial y-coordinate of the ball
     */
    public Ball(double x, double y){
        this.xPos = x;
        this.yPos = y;

        // Generate a random color for the ball
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        randomColor = new Color(r,g,b);

    }

    /**
     * Draws the ball on the UI.
     * The ball is only drawn if it is within the right end limit
     */
    public void draw(){
        if(xPos <= RIGHT_END) {
            UI.setColor(randomColor);
            UI.fillOval(xPos - 10, GROUND - yPos - DIAM, DIAM, DIAM);
        }
    }

    /**
     * Updates the position of the ball for each time step
     * Applies a gravity effect that reduces the vertical speed
     * Ensures that the ball does not go below the ground level
     */
    public void step(){
        // Apply gravity effect by reducing the vertical speed
       if(stepX != 0 || stepY != 0) {
           this.stepY = this.stepY - 0.2;
       }

       // Ensures the ball does not go below the ground level
        if(this.yPos < 0){
            this.yPos = 0;
            this.stepY = 0;
        }

        // Update the position of the ball based on its speed
        this.xPos += this.stepX;
        this.yPos += (this.stepY - 0.2);
    }

    /**
     * Sets the horizontal speed (xSpeed) of the ball
     * @param xSpeed The horizontal speed of the ball
     */
    public void setXSpeed(double xSpeed){
        this.stepX = xSpeed;
    }

    /**
     * Sets the vertical speed (ySpeed) of the ball
     * @param ySpeed The vertical speed of the ball
     */
    public void setYSpeed(double ySpeed){
        this.stepY = ySpeed;
    }

    /**
     * Gets the current y-coordinate of the ball
     * @return The current y-coordinate of the ball
     */
    public double getY(){
        return this.yPos;
    }

    /**
     * Gets the current x-coordinate of the ball
     * @return The current x-coordinate of the ball
     */
    public double getX(){
        return this.xPos;
    }
}
