import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Asteroid {

    //Variabili di istanza
    private int x;
    private int y;
    private int speed;
    private int width;
    private int height;
    private boolean isActive;
    private boolean asteroidSpecial;
    private BufferedImage asteroidImage;  // Declare an Image variable
    private BufferedImage asteroidImageDeactivate;
    private double rotationAngle = 0;
    private Game game;  // Reference to the Game instance

    //Costruttore
    public Asteroid(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.speed =  (int)(Math.random() * 3) + 3;
        this.width = (int)(Math.random() * 20.0) + 90;
        this.height = (int)(Math.random() * 20.0) + 70;
        this.isActive = true;

        try {
            this.asteroidImage = ImageIO.read(new File("images/asteroide.png"));
            this.asteroidImageDeactivate = ImageIO.read(new File("images/explosion.png"));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    //isActive() che restituisce isActive
    public boolean isActive() {
        return isActive;
    }

    //deactivate() che setta isActive a false
    public void deactivate() {
        this.isActive = false;
    }

    //Getter e Setter
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    // Disegna l'asteroide - ok
    public void draw(Graphics g) {
        if (asteroidImage != null) {
            Graphics2D g2d = (Graphics2D) g.create();  // Create a Graphics2D object

            // Translate the graphics context to the asteroid's center
            int centerX = x + width / 2;
            int centerY = y + height / 2;
            g2d.translate(centerX, centerY);

            // Rotate the graphics context
            g2d.rotate(Math.toRadians(rotationAngle));

            // Draw the rotated image
            g2d.drawImage(asteroidImage, -width / 2, -height / 2, width, height, null);

            // Update the rotation angle
            rotationAngle += 2;  // Adjust for desired rotation speed

            g2d.dispose();  // Dispose of the created Graphics2D object
        } else {
            System.out.println("Image not found!");
        }
    }

    // Muove l'asteroide verso il basso
    public void move() {
        y += speed;
    }

    @Override
    public String toString() {
        return "Asteroid{" +
                "x=" + x +
                ", y=" + this.y +
                ", speed=" + this.speed +
                ", width=" + this.width +
                ", height=" + this.height +
                ", isActive=" + this.isActive +
                "}";
    }

}
