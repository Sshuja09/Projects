import ecs100.*;
import java.awt.Color;


/**
 * The Robot class represents a circular vacuum cleaner that moves around a floor,
 * erasing "dirt" as it goes. It has methods for stepping, changing direction,
 * reporting its position, erasing itself, and drawing itself
 */

public class Robot{

    // Fields
    double diam; // Diameter of the robot
    double xPos; // X-coordinate of the robot's position
    double yPos; // Y-coordinate of the robot's position
    double direction; // Current direction of the robot in degrees
    Color color; // Color of the robot

    /**
     * Constructor for the Robot class
     * Initializes the robot with a given diameter, position, and color
     * The initial direction is set to a random angle between 0 and 360 degrees
     */
    public Robot(double diam, double xPos, double yPos, Color color){
        this.diam = diam;
        this.xPos = xPos;
        this.yPos = yPos;
        this.direction = Math.random()*360;
        this.color = color;
    }

    /**
     * Gets the X-coordinate of the robot's position
     */
    public double getX(){
        return this.xPos;
    }

    /**
     * Gets the Y-coordinate of the robot's position
     */
    public double getY(){
        return this.yPos;
    }

    /**
     * Moves the robot forward one step in its current direction
     */
    public void step(){
        this.xPos = this.getX() + Math.cos(this.direction * Math.PI/180);
        this.yPos = this.getY() + Math.sin(this.direction * Math.PI/180);
    }

    /**
     * Change the direction of the robot to a new (random) direction
     * The robot first erases itself, then updates its position and direction
     */
    public void changeDirection(){
        this.erase();
        this.xPos = this.getX() - Math.cos(this.direction * Math.PI/180);
        this.yPos = this.getY() - Math.sin(this.direction * Math.PI/180);
        this.direction = Math.random()*360;
    }

    /**
     * Erases the robot and the "dirt" under it by filling its position with white color
     */
    public void erase(){
        UI.setColor(Color.WHITE);
        UI.fillOval(this.getX(), this.getY(), this.diam, this.diam);

    }

    /**
     * Draws the robot at its current position with its color
     * Also draws a small dot indicating the direction of the robot
     */
    public void draw(){
        UI.setColor(this.color);
        UI.fillOval(this.xPos, this.yPos, this.diam, this.diam);

        // Draw a dot indication the direction of the robot
        UI.setColor(Color.black);
        double xCenter = (this.xPos + this.diam / 2);
        double yCenter = (this.yPos + this.diam / 2);
        double dotX, dotY, dotRadius;
        dotRadius = (this.diam / 5); // Radius of the dot
        dotX = Math.cos(this.direction * Math.PI / 180) * dotRadius;
        dotY = Math.sin(this.direction * Math.PI / 180) * dotRadius;

        UI.fillOval(xCenter + dotX, yCenter + dotY, 4, 4);
    }
}
