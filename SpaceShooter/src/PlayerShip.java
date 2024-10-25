import java.awt.*;

public class PlayerShip {

    private int x;
    private int y;
    private int width;
    private int height;
    private int lives;

    // Costruttore PlayerShip
    PlayerShip(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.width = 50;
        this.height = 50;
        this.lives = 3;
    }

    // Muove la navicella
    void move(int dx, int dy){
    }

    /*

    // Istanzia un nuovo oggetto Bullet al centro/nord della navicella
    Bullet shoot(){
        return new Bullet(this.x, this.y);
    }

     */

    // Riduce le vite
    void loseLife(){
        if(this.lives > 0){
            this.lives --;
        }else{
            System.out.println("Game Over!");
        }
    }

    // Ritorna il numero di vite
    int getLives(){
        return lives;
    }

    // Disegna la navicella
    void draw(Graphics g){}

}
