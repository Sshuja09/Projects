import ecs100.*;
import java.util.*;
import java.awt.Color;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;


/**
 * The ImageProcessor class performs various operations on images,
 * such as lightening, fading, flipping, shifting, and rotating.
 */
public class ImageProcessor{
    private int[][] image = null;
    private int selectedRow = 0;
    private int selectedCol = 0;
    private final int pixelSize = 1;

    /**
     * Lightens the image by increasing pixel values by 20, capped at 255
     */
    public void lightenImage(){
        for(int row = 0; row < image.length; row++){
            for(int col = 0; col < image[row].length; col++){
                if(image[row][col] < 255 - 20){
                    image[row][col] += 20;
                }else{
                    image[row][col] = 255;
                }
            }
        }
        this.redisplayImage();
    }

    /**
     * Fades the image by adjusting pixel values based on their intensity
     */
    public void fadeImage() {
        for (int row = 0; row < image.length; row++) {
            for (int col = 0; col < image[row].length; col++) {
                int pixelValue = image[row][col];

                if (pixelValue >= 128) {
                    image[row][col] -= (pixelValue - 128) * 0.2;
                } else {
                    image[row][col] += (128 - pixelValue) * 0.2;
                }
            }
        }

        this.redisplayImage();
    }

    /**
     * Flips the image horizontally
     */
    public void flipImageHorizontally() {
        for (int row = 0; row < image.length; row++) {
            int currentColumn = 0;
            for (int col = image[row].length - 1; col > currentColumn; col--) {
                int temp = image[row][currentColumn];
                image[row][currentColumn] = image[row][col];
                image[row][col] = temp;
                currentColumn++;
            }

        }
        this.redisplayImage();
    }

    /**
     * Shifts the image vertically. (One row per click)
     */
    public void shiftImageVertically(){
        int rows = image.length;
        int cols = image[0].length;
        int lastRow = rows - 1;

        int[][] newImage = new int[rows][cols];
        for(int row = 0; row < rows; row++){
            if(row == lastRow){
                newImage[0] = image[lastRow];
            }else{
                newImage[row + 1] = image[row];
            }
        }
        image = newImage;
        this.redisplayImage();
    }

    /**
     * Rotates the image 90 degrees clockwise
     */
    public int[][] rotateImage90(int[][] image) {
        int rows = image.length;
        int cols = image[0].length;

        int[][] newImage = new int[cols][rows];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                newImage[cols - 1 - col][row] = image[row][col];
            }
        }

        return newImage;
    }

    /**
     * Rotates the image 180 degrees
     */
    public void rotateImage180(){
        for(int i = 0; i < 2; i++){
            image = rotateImage90(image);
        }

        this.redisplayImage();
    }

    /**
     * Rotates the image 90 degrees anti-clockwise
     */
    public void rotateImage90(){
        image = rotateImage90(image);
        this.redisplayImage();
    }

    /** field and helper methods to precompute and store all the possible gray colours,
     *  so the redisplay method does not have to constantly construct new color objects
     */
    private Color[] grayColors = computeGrayColors();

    /** Display the image on the screen with each pixel as a square of size pixelSize.
     *  To speed it up, all the possible colours from 0 - 255 have been precalculated.
     */
    public void redisplayImage(){
        UI.clearGraphics();
        UI.setImmediateRepaint(false);
        for(int row=0; row<this.image.length; row++){
            int y = row * this.pixelSize;
            for(int col=0; col<this.image[0].length; col++){
                int x = col * this.pixelSize;
                UI.setColor(this.grayColor(this.image[row][col]));
                UI.fillRect(x, y, this.pixelSize, this.pixelSize);
            }
        }
        UI.setColor(Color.red);
        UI.drawRect(this.selectedCol*this.pixelSize,this.selectedRow*this.pixelSize,
            this.pixelSize,this.pixelSize);
        UI.repaintGraphics();
    }

    /** Get and return an image as a two-dimensional gray-scale image (from 0-255).
     *  This method will cause the image to be returned as a gray-scale image,
     *  regardless of the original colouration.
     */
    public int[][] loadAnImage(String imageName) {
        int[][] ans = null;
        if (imageName==null) return null;
        try {
            BufferedImage img = ImageIO.read(new File(imageName));
            UI.printMessage("loaded image height(rows)= " + img.getHeight() +
                "  width(cols)= " + img.getWidth());
            ans = new int[img.getHeight()][img.getWidth()];
            for (int row = 0; row < img.getHeight(); row++){
                for (int col = 0; col < img.getWidth(); col++){
                    Color c = new Color(img.getRGB(col, row), true);
                    // Use a common algorithm to move to grayscale
                    ans[row][col] = (int)Math.round((0.3 * c.getRed()) + (0.59 * c.getGreen())
                        + (0.11 * c.getBlue()));
                }
            }
        } catch(IOException e){UI.println("Image reading failed: "+e);}
        return ans;
    }

    /** Ask user for an image file, and load it into the current image */
    public void loadImage(){
        this.image = this.loadAnImage(UIFileChooser.open());
        this.redisplayImage();
    }

    /** Write the current grayscale image to the specified filename */
    public  void saveImage() {
        // For speed, we'll assume every row of the image is the same length!
        int height = this.image.length;
        int width = this.image[0].length;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int grayscaleValue = this.image[row][col];
                Color c = new Color(grayscaleValue, grayscaleValue, grayscaleValue);
                img.setRGB(col, row, c.getRGB());
            }
        }
        try {
            String fname = UIFileChooser.save("save to png image file");
            if (fname==null) return;
            ImageIO.write(img, "png", new File(fname));
        } catch(IOException e){UI.println("Image reading failed: "+e);}
    }

    private Color[] computeGrayColors(){
        Color[] ans = new Color[256];
        for (int i=0; i<256; i++){
            ans[i] = new Color(i, i, i);
        }
        return ans;
    }

    private Color grayColor(int gray){
        if (gray < 0){
            return Color.blue;
        }
        else if (gray > 255){
            return Color.red;
        }
        else {
            return this.grayColors[gray];
        }
    }


    public void doMouse(String a, double x, double y){
        if (a.equals("released")) {
            this.setPos(x, y);}
    }

    /** Set the selected Row and Col to the pixel on the mouse position x, y */
    public void setPos(double x, double y){
        int row = (int)(y/this.pixelSize);
        int col = (int)(x/this.pixelSize);
        if (this.image != null && row < this.image.length && col < this.image[0].length){
            this.selectedRow = row;
            this.selectedCol = col;
            this.redisplayImage();
        }
    }

    public void setupGUI(){
        UI.initialise();
        UI.setMouseListener(this::doMouse);
        UI.addButton("Load",       this::loadImage );
        UI.addButton("Save",       this::saveImage );       
        UI.addButton("Lighten",    this::lightenImage );
        UI.addButton("Fade",       this::fadeImage );    
        UI.addButton("Flip Horiz", this::flipImageHorizontally );
        UI.addButton("Shift Vert", this::shiftImageVertically );
        UI.addButton("Rotate 180", this::rotateImage180 );     
        UI.addButton("Rotate 90",  this::rotateImage90 );
        UI.addButton("Quit", UI::quit );

        if (image != null){
            this.redisplayImage();
        }
        else{
            UI.setFontSize(36);
            UI.drawString("Start the program from main", 2, 36);
            UI.drawString("in order to load an image", 2, 80);
            UI.sleep(2000);
            UI.quit();
        }
    }   

    public static void main(String[] arguments){
        ImageProcessor obj = new ImageProcessor();
        obj.image = obj.loadAnImage("/Users/pakii_boii/Desktop/Temp/COMP-102/Assignment 10/ImageProcessor/img-rose.jpg");
        obj.setupGUI();
    }
}
