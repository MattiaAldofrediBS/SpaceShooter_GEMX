import java.awt.*;

public class PlayerShip {

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
        x += dx;

        if(x < 0){
            x = 0;
        }else if(x + width > bound){
            x = bound - width;
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

    // Ritorna il numero di vite - ok
    public int getLives(){
        return lives;
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

}
