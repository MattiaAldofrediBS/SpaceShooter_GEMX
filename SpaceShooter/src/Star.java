import java.awt.*;

public class Star {
    private int x, y;  // Posizione della stella
    private int size;  // Dimensione della stella
    private int speed; // Velocità di movimento verso l'alto

    // Costruttore
    public Star(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.size = (int)(Math.random() * 5 + 1); // Dimensione casuale tra 1 e 5
        this.speed = (int)(Math.random() * 2 + 1); // Velocità casuale tra 1 e 2
    }

    // Metodo per muovere la stella verso l'alto
    public void move() {
        this.y -= speed;  // Spostamento verso l'alto (decrementa y)
    }

    // Metodo per disegnare la stella
    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval(x, y, size, size);  // Disegna un piccolo cerchio bianco
    }

    // Getter per la posizione y
    public int getY() {
        return y;
    }

    // Getter per la posizione x
    public int getX() {
        return x;
    }

    // Getter per la larghezza della stella
    public int getWidth() {
        return size;
    }

    // Getter per l'altezza della stella
    public int getHeight() {
        return size;
    }
}