import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

public class Game extends JPanel implements ActionListener {
    private PlayerShip playerShip;
    private ArrayList<Bullet> bullets;         // Active Bullets
    private ArrayList<Asteroid> asteroids;     // Active Asteroids
    private Timer timer;                        // Timer for updates
    private Leaderboard leaderboard;            // Leaderbords ref
    private boolean gameRunning;                // True or False, status of the game
    private Menu menu;

    //Window size
    private final int WIDTH = 800;             // Window width
    private final int HEIGHT = 600;            // Window Height

    // Costruttore
    public Game() {
        playerShip = new PlayerShip();
        asteroids = new ArrayList<>();
        bullets = new ArrayList<>();
        leaderboard = new Leaderboard();
        menu = new Menu();
        gameRunning = true;

        timer = new Timer(16, this); // Aggiornamento ogni 16 ms (~60 FPS)
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameRunning) {
            update();
            checkCollisions();
            repaint(); // Chiama paintComponent
        }
    }

    private void update() {
        // Aggiornare la posizione degli asteroidi
        for (Asteroid asteroid : asteroids) {
            //asteroid.move();
        }
        // Aggiornare la posizione dei proiettili
        for (Bullet bullet : bullets) {
            //bullet.move();
        }
    }

    private void checkCollisions() {
        Iterator<Bullet> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            Iterator<Asteroid> asteroidIterator = asteroids.iterator();
            while (asteroidIterator.hasNext()) {
                Asteroid asteroid = asteroidIterator.next();
//                if (bullet.collidesWith(asteroid)) {
//                    bulletIterator.remove();
//                    asteroidIterator.remove();
//                    playerShip.incrementScore();
//                    break;
//                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawMain(g);
    }

    private void drawMain(Graphics g) {
        // Disegna il giocatore
        playerShip.draw(g);
        // Disegna gli asteroidi
//        for (Asteroid asteroid : asteroids) {
//            asteroid.draw(g);
//        }
//        // Disegna i proiettili
//        for (Bullet bullet : bullets) {
//            bullet.draw(g);
//        }
    }

    private void spawnAsteroids() {
        // Logica per generare asteroidi iniziali
        for (int i = 0; i < 5; i++) {
            asteroids.add(new Asteroid()); // Aggiunge un nuovo asteroide alla lista
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Space Shooter");
        Game game = new Game();
        frame.add(game);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
