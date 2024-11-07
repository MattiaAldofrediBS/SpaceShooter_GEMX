import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;

public class Game extends JPanel implements Runnable {
    private PlayerShip playerShip;
    private ArrayList<Bullet> bullets;         // Active Bullets
    private ArrayList<Asteroid> asteroids;     // Active Asteroids
    private Timer timer;                        // Timer for updates
    private Leaderboard leaderboard;            // Leaderbords ref
    private boolean gameRunning;                // True or False, status of the game
    private boolean gameOver;
    private Menu menu;
    private double score;


    private static final int WIDTH = 800;  // Window width
    private static final int HEIGHT = 600;  // Window height

    public Game() {
        playerShip = new PlayerShip(375, 500);
        asteroids = new ArrayList<>();
        score = 0.0;
        gameRunning = true;
        gameOver = false;

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    playerShip.move(-5); // Left
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    playerShip.move(5); // Right
                }
            }
        });
        setFocusable(true);

        // Thread separato
        new Thread(this).start();

        // Game Window
        JFrame gameFrame = new JFrame("Space Shooter Game");
        gameFrame.setSize(WIDTH, HEIGHT);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setLocationRelativeTo(null); // Centra la finestra sullo schermo
        gameFrame.add(this);
        gameFrame.setVisible(true);
    }

    @Override
    public void run() {
        while (gameRunning) {
            update(); // Logic
            checkCollisions(); // Collision
            repaint(); // Redraws the scene
            try {
                Thread.sleep(16); // 60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // New asteroids
    private void update() {
        if (Math.random() < 0.01) { // every cycle 1% possibility
            asteroids.add(new Asteroid((int)(Math.random() * (WIDTH - 30)), -1)); // add asteroid random
        }

        // moves the asteroids
        for (Asteroid asteroid : asteroids) {
            asteroid.move();
        }

        // removes them
        Iterator<Asteroid> iterator = asteroids.iterator();
        while (iterator.hasNext()) {
            Asteroid asteroid = iterator.next();
            if (asteroid.getY() > HEIGHT || !asteroid.isActive()) { // Out of screen or deactivated
                iterator.remove(); //remove
            }
        }

        score += 0.01; // Increments the score each cycle

    }

    // Spaceship and asteroid collisions | Bullet is missing!!
    private void checkCollisions() {
        Iterator<Asteroid> iterator = asteroids.iterator();
        while (iterator.hasNext()) {
            Asteroid asteroid = iterator.next();
            if (playerShip.getX() < asteroid.getX() + asteroid.getWidth() &&
                    playerShip.getX() + playerShip.getWidth() > asteroid.getX() &&
                    playerShip.getY() < asteroid.getY() + asteroid.getHeight() &&
                    playerShip.getY() + playerShip.getHeight() > asteroid.getY()) {
                // Collisione con un asteroide
                if(playerShip.getLives() == 1) {
                    gameOver = true; // Imposta il gioco come finito
                    gameRunning = false; // Termina il ciclo di gioco
                    break;
                }else{
                    playerShip.setLives(playerShip.getLives() - 1);
                    asteroid.deactivate();
                }
            }
        }
    }

    // Draws all the components
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Background
        setBackground(Color.BLACK);

        // Score
        g.setColor(Color.WHITE);
        g.drawString("Score: " + (int)score, 10, 20);

        // Lives
        g.setColor(Color.WHITE);
        g.drawString("Lives: " + playerShip.getLives(), 10, 40);

        // Spaceship
        playerShip.draw(g);

        // Asteroids
        for (Asteroid asteroid : asteroids) {
            asteroid.draw(g);  // Disegna ogni asteroide
        }

        // Gameover
        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("Game Over!", WIDTH / 2 - 100, HEIGHT / 2);
            g.drawString("Score: " + (int)score, WIDTH / 2 - 60, HEIGHT / 2 + 50);
        }
    }
}
