import java.awt.*;

public class Asteroid {

    //Variabili di istanza
    private int x;
    private int y;
    private int speed;
    private int width;
    private int height;
    private boolean isActive;

    //Costruttore
    public Asteroid(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.speed =  (int)(Math.random() * 3) + 3;
        this.width = (int)(Math.random() * 20.0) + 20;
        this.height = (int)(Math.random() * 20.0) + 20;
        this.isActive = true;
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

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(x, y, width, height); // Disegna un cerchio per l'asteroide
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
