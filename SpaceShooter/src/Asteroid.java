public class Asteroid {

    //Variabili di istanza
    private int x;
    private int y;
    private int speed;
    private int size;
    private boolean isActive;

    //Costruttore
    public Asteroid(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.speed = 10;
        this.size = 5;
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

    public int setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public int setY(int y) {
        this.y = y;
    }

    public int getSpeed() {
        return speed;
    }

    public int setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSize() {
        return size;
    }

    public int setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Asteroid{" +
                "x=" + x +
                ", y=" + y +
                ", speed=" + speed +
                ", size=" + size +
                ", isActive=" + isActive +
                "}";
    }
}
