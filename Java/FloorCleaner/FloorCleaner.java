import ecs100.*;
import java.awt.Color;
import java.util.Random;

/** Runs a simulation of a robot vacuum cleaner that moves around a floor area,
 * changing to a new random direction every time it hits the edge of the
 * floor area.
 */
public class FloorCleaner{

    // Constants for drawing the robot and the floor.
    public static final double DIAM = 60;  //diameter of the robot.
    public static final double LEFT = 50;  // borders of the floor area.
    public static final double TOP = 50;
    public static final double RIGHT = 550;
    public static final double BOT = 420;

    /* Simulation loop.
     * The method should draw a dirty floor (a gray rectangle), and then
     * create two robots and make them run around for ever.
     * Each time step, each robot will erase the "dirt" under it, and then
     *  move forward a small amount.
     * After it has moved, the program should ask for the robot's
     *  position and check the position against the edges of the floor area.
     * If it has gone over the edge, it will make the robot step back onto the floor
     *  and change its direction, it will also check if the robots have hit each other, and
     *  if so, make them both back off and change direction
     * Make sure that starting positions of the robots are not on
     * top of each other (otherwise they get "stuck" to each other!)
     */

    /**
     * Checks if the robot is at the boarders of the floor area
     */
    public boolean checkBorders(Robot rob){
        return rob.getX() <= LEFT || rob.getX() +10>= RIGHT || rob.getY() <= TOP || rob.getY() +10 >= BOT;
    }

    /**
     * Generate a random X coordinate within the floor area
     */
    public double randomX(Random rand, double radius){
       return (LEFT + radius) + ((RIGHT - radius) - (LEFT + radius)) * rand.nextDouble();
    }

    /**
     * Generate a random Y coordinate within the floor area
     */
    public double randomY(Random rand, double radius){
        return (TOP + radius) + ((BOT - radius) - (TOP + radius)) * rand.nextDouble();
    }

    /**
     * Calculate the distance between two robots
     */
    public double getDistance(Robot rob1, Robot rob2){
        double deltaX = rob1.getX() - rob2.getX();
        double deltaY = rob1.getY() - rob2.getY();
        double distance = Math.hypot(deltaX, deltaY);
        return distance;
    }

    /**
     * Check if two robots are colliding
     */
    public boolean isColliding(Robot rob1, Robot rob2){
        return getDistance(rob1, rob2) <= DIAM;
    }

    /**
     * Generate a random color
     */
    public Color randomColor(){
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        return new Color(r, g, b);
    }

    /**
     * Clean the floor using two robots
     */
    public void cleanFloor(){
        Random rand = new Random();
        double radius = DIAM / 2;
        UI.setColor(Color.gray);
        UI.fillRect(LEFT, TOP, RIGHT, BOT);

        // Generate random colors for two robots
        Color color1 = randomColor();
        Color color2 = randomColor();

        // Generate random starting position for two robots, ensuring they are not colliding
        double rob1X = randomX(rand, radius);
        double rob1Y = randomY(rand, radius);
        double rob2X = randomX(rand, radius);
        double rob2Y = randomY(rand, radius);

        while(Math.hypot((rob1X - rob2X), (rob1Y - rob2Y)) <= DIAM){
            rob2X = randomX(rand, radius);
            rob2Y = randomY(rand, radius);
        }

        // Create two robots with random position and colors
        Robot rob1 = new Robot(DIAM, rob1X, rob1Y, color1);
        Robot rob2 = new Robot(DIAM, rob2X, rob2Y, color2);

        Robot[] robots = {rob1, rob2};
        for(Robot r : robots){ r.draw(); }

        // Move the robots, change direction if they hit the borders, and handle collisions
        while(true){
            for(Robot rob: robots) {
                rob.erase();
                rob.step();
                if (checkBorders(rob)) {
                    rob.changeDirection();
                }
                rob.draw();
            }

            if(isColliding(rob1, rob2)){
                for(Robot rob : robots){ rob.changeDirection(); }
            }

            UI.sleep(6);
        }

    }

    /**
     * Set up the GUI with buttons and window size
     */
    public void setupGUI(){
        UI.addButton("start", this::cleanFloor);
        UI.addButton("Quit", UI::quit);
        UI.setWindowSize(650,500);
        UI.setDivider(0);
    }    


    public static void main(String[] arguments){
        FloorCleaner fc = new FloorCleaner();
        fc.setupGUI();
    }	

}
