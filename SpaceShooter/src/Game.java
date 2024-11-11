import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;

public class Game extends JPanel implements Runnable {
    private PlayerShip playerShip;
    private ArrayList<Bullet> bullets;         // Bullets
    private ArrayList<Asteroid> asteroids;     // Asteroids
    private ArrayList<Star> stars;            // Stars
    private boolean gameRunning;               // Running
    private boolean gameOver;                 // Game over
    private boolean gamePaused;               // Paused
    private double score;                     // Score

    private static final int WIDTH = 900;      // Width
    private static final int HEIGHT = 1080;     // Height

    private long lastFireTime; // Last shot
    private static final long BASE_FIRE_DELAY = 1000;  // Initial delay
    private static final double FIRE_SCALE_FACTOR = 40.0;  // speed-up

    private boolean[] keyStates = new boolean[1024]; // Key states

    private static final double SCALE_FACTOR = 200;  // spawn  increase
    private static final double MAX_SPAWN_PROBABILITY = 0.8;  // ca

    private static final double SCORE_INCREASE_CYCLE = 0.01;

    private static Circle earth;

    public Game() {
        playerShip = new PlayerShip(425, 900);
        asteroids = new ArrayList<>();
        bullets = new ArrayList<>();
        stars = new ArrayList<>();
        score = 0.0;
        gameRunning = true;
        gameOver = false;
        gamePaused = false;

        earth = new Circle(WIDTH / 2, HEIGHT); // Create circle at spaceship position

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                keyStates[e.getKeyCode()] = true;  // Key pressed
            }

            @Override
            public void keyReleased(KeyEvent e) {
                keyStates[e.getKeyCode()] = false;  // Key released
            }
        });
        setFocusable(true);

        new Thread(this).start(); // Start thread

        JFrame gameFrame = new JFrame("Space Shooter");
        gameFrame.setSize(WIDTH, HEIGHT); // Window size
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setLocationRelativeTo(null); // Center window
        gameFrame.add(this);
        gameFrame.setVisible(true);
    }

    @Override
    public void run() {
        while (gameRunning) {
            if (!gamePaused) {
                update(); // Update
                checkCollisions(); // Check collisions
            }
            repaint(); // Redraw
            try {
                Thread.sleep(16); // 60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void update() {
        // Update the circle's position if it's still active
        if (earth != null) {
            earth.update(); // Move the circle upwards

            // Remove the circle when it goes off the screen (Y < 0)
            if (earth.isExpired()) {
                earth = null; // Remove the circle when it's no longer visible
            }
        }

        if (Math.random() < 0.6) {
            stars.add(new Star((int)(Math.random() * WIDTH), 0)); // Add star
        }

        for (Star star : stars) {
            star.move(); // Move star
        }

        if (keyStates[KeyEvent.VK_LEFT]) {
            playerShip.move(-5); // Move left
        }

        if (keyStates[KeyEvent.VK_RIGHT]) {
            playerShip.move(5);  // Move right
        }

        if (keyStates[KeyEvent.VK_SPACE]) {
            fireBullet();        // Fire bullet
        }

        // Asteroids
        double spawnProbability = Math.log(score + 1) / (Math.log(score + 1) + SCALE_FACTOR);
        spawnProbability = Math.min(spawnProbability, MAX_SPAWN_PROBABILITY);  // Cap

        // Random
        if (Math.random() < spawnProbability) {
            asteroids.add(new Asteroid((int)(Math.random() * (WIDTH - 30)), -1));  // Spawn asteroid
        }

        for (Asteroid asteroid : asteroids) {
            asteroid.move(); // Move asteroid
        }

        for (Bullet bullet : bullets) {
            bullet.move(); // Move bullet
        }

        Iterator<Asteroid> asteroidIterator = asteroids.iterator();
        while (asteroidIterator.hasNext()) {
            Asteroid asteroid = asteroidIterator.next();
            if (asteroid.getY() > HEIGHT || !asteroid.isActive()) {
                asteroidIterator.remove(); // Remove asteroid
            }
        }

        Iterator<Bullet> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            if (bullet.getY() < 0 || !bullet.isActive()) {
                bulletIterator.remove(); // Remove bullet
            }
        }

        score += SCORE_INCREASE_CYCLE; // Increment score
    }

    private void fireBullet() {
        long currentTime = System.currentTimeMillis();

        // Calculate fire delay based on score
        double fireDelay = BASE_FIRE_DELAY; // / (1 + (score / FIRE_SCALE_FACTOR));

        if (currentTime - lastFireTime >= fireDelay) {
            Bullet bullet = new Bullet(playerShip.getX() + playerShip.getWidth() / 2 - 2, playerShip.getY());
            bullets.add(bullet);
            lastFireTime = currentTime;  // Update the last fire time
        }
    }

    private void checkCollisions() {
        for (Asteroid asteroid : asteroids) {
            if (checkCollision(playerShip, asteroid)) {
                if (playerShip.getLives() == 1) {
                    gameOver = true; // Game over
                    gameRunning = false; // Stop game

                    Leaderboard lb = new Leaderboard();
                    lb.saveScore((int) score);
                    break;
                } else {
                    playerShip.setLives(playerShip.getLives() - 1); // Decrease lives
                    asteroid.deactivate(); // Deactivate asteroid
                }
            }

            for (Bullet bullet : bullets) {
                if (checkCollision(bullet, asteroid)) {
                    bullet.deactivate();  // Deactivate bullet
                    asteroid.deactivate();  // Deactivate asteroid
                    score += 10;  // Increase score
                }
            }
        }
    }

    private boolean checkCollision(PlayerShip playerShip, Asteroid asteroid) {
        return playerShip.getX() < asteroid.getX() + asteroid.getWidth() &&
                playerShip.getX() + playerShip.getWidth() > asteroid.getX() &&
                playerShip.getY() < asteroid.getY() + asteroid.getHeight() &&
                playerShip.getY() + playerShip.getHeight() > asteroid.getY();
    }

    private boolean checkCollision(Bullet bullet, Asteroid asteroid) {
        return bullet.getX() < asteroid.getX() + asteroid.getWidth() &&
                bullet.getX() + bullet.getWidth() > asteroid.getX() &&
                bullet.getY() < asteroid.getY() + asteroid.getHeight() &&
                bullet.getY() + bullet.getHeight() > asteroid.getY();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the start circle (if it exists)
        if (earth != null) {
            earth.draw(g); // Draw the circle as it moves up
        }

        setBackground(Color.BLACK); // Background

        g.setColor(new Color(123, 0, 0, 230));  // Semi-transparent black background for contrast

        // Points of the parallelogram (define the four corners)
        int[] xPoints = {0, 150, 200, 0}; // X coordinates of the parallelogram
        int[] yPoints = {0, 0, 50, 50};  // Y coordinates of the parallelogram

        // Draw the parallelogram
        g.fillPolygon(xPoints, yPoints, 4);

        g.setColor(Color.WHITE);
        g.drawString("Score: " + (int) score, 10, 20); // Score display

        g.drawString("Lives: " + playerShip.getLives(), 10, 40); // Lives display

        playerShip.draw(g); // Draw player ship

        for (Asteroid asteroid : asteroids) {
            asteroid.draw(g); // Draw asteroid
        }

        for (Bullet bullet : bullets) {
            bullet.draw(g); // Draw bullet
        }

        for (Star star : stars) {
            star.draw(g); // Draw star
        }

        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("Game Over!", WIDTH / 2 - 100, HEIGHT / 2); // Game over screen
            g.drawString("Score: " + (int) score, WIDTH / 2 - 60, HEIGHT / 2 + 50); // Score
        }

        if (gamePaused) {
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("PAUSE", WIDTH / 2 - 100, HEIGHT / 2); // Pause screen
        }
    }

    // Method to handle key press for Restart or Exit
    public void handleGameOverKeys() {
        if (keyStates[KeyEvent.VK_R]) {
            resetGame(); // Restart the game
        }

        if (keyStates[KeyEvent.VK_ESCAPE]) {
            System.exit(0); // Exit the game
        }
    }

    // Method to reset the game when R is pressed
    public void resetGame() {
        playerShip = new PlayerShip(375, 500);
        asteroids.clear();
        bullets.clear();
        stars.clear();
        score = 0.0;
        gameRunning = true;
        gameOver = false;
        gamePaused = false;
    }

    class Circle {
        private int x, y; // Position of the circle
        private final int RADIUS = 500; // Fixed size for the circle

        // Constructor to initialize the circle at a given position
        public Circle(int x, int y) {
            this.x = x;
            this.y = y;
        }

        // Method to update the circle's position (move it upwards)
        public void update() {
            y += 1;  // Move the circle up by 2 pixels each update
        }

        // Method to check if the circle has gone off the screen (out of bounds)
        public boolean isExpired() {
            return false; // y + RADIUS < 0;  // If the circle is above the screen, it's "expired"
        }

        // Method to draw the circle
        public void draw(Graphics g) {
            g.setColor(new Color(70, 130, 180));
            g.fillOval(x - RADIUS / 2, y - RADIUS / 2, RADIUS, RADIUS);  // Draw the circle

            g.setColor(new Color(34, 139, 34)); // LAND
            g.fillOval(x - RADIUS / 4, y - RADIUS / 4, RADIUS / 2, RADIUS / 2);
        }
    }
}
