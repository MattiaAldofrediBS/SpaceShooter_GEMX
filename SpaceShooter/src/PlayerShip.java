import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class PlayerShip {

    public static final int MIN_BOUND = 0;
    public static final int MAX_BOUND = Menu.SCREENSIZE_WIDTH;
    private int x;
    private int y;
    public static final int width = 150;
    public static final int height = 150;
    private int lives;

    private BufferedImage shipImage;  // Declare an Image variable

    // Costruttore PlayerShip - ok
    PlayerShip(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.lives = 3;


        try {
            this.shipImage = ImageIO.read(new File("images/spaceship_v2.png"));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }

    // Muove la navicella - ok
    public void move(int dx){
        if(x + dx >=  MIN_BOUND && x + dx <= MAX_BOUND - width) {
            x += dx;
        }
    }

    // Istanzia un nuovo oggetto Bullet al centro/nord della navicella - ok
    public Bullet shoot(){
        return new Bullet(this.x + width / 2, this.y);
    }

    // Riduce le vite - ok
    public void loseLife(){
        if(this.lives > 0){
            this.lives--;
        }else{
            System.out.println("Game Over!");
        }
    }

    // Disegna la navicella - ok
    public void draw(Graphics g) {
        if (shipImage != null) {
            // Scale the image to the desired width and height of the ship
            Image scaledImage = shipImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            g.drawImage(scaledImage, x, y, null);
        } else {
            System.out.println("Image not found!");
        }
    }

    // Ritorna il rettangolo della PlayerShip - ok
    public Rectangle getBounds(){
        return new Rectangle(x, y, width, height);
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public int getLives(){
        return lives;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public void setLives(int lives){
        this.lives = lives;
    }
}
