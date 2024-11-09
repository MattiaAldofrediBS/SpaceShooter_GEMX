import java.awt.*;

public class PlayerShip {

    public static final int MIN_BOUND = 0;
    public static final int MAX_BOUND = 800;
    private int x;
    private int y;
    private int width;
    private int height;
    private int lives;

    // Costruttore PlayerShip - ok
    PlayerShip(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.width = 50;
        this.height = 50;
        this.lives = 3;
    }

    // Muove la navicella - ok
    public void move(int dx, int bound){
        if(x + dx >=  MIN_BOUND && dx <= MAX_BOUND - width) {
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
    public void draw(Graphics g){
        g.setColor(Color.BLACK); // Importo colore
        g.fillRect(x, y, width, height); // Disegno rettangolo (x e y sono in alto a sx)
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

    public void setWidth(int width){
        this.width = width;
    }

    public void setHeight(int height){
        this.height = height;
    }

    public void setLives(int lives){
        this.lives = lives;
    }
}
