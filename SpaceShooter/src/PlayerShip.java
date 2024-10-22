import java.awt.*;

public class PlayerShip {

    private int x;
    private int y;
    private int width;
    private int height;
    private int lives;

    //costruttore
    PlayerShip(int startX, int startY) {}

    //per il movimento
    void move(int dx, int dy){}

    //per istanziare un nuovo proiettile
    Bullet shoot(){}

    //riduce di 1 il numero di vite
    void loseLife(){}

    //ritorna il numero di vite
    int getLives(){
        return lives;
    }

    //disegna la navicella
    void draw(Graphics g){}

}
