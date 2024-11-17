import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Game extends JPanel implements Runnable {
    private PlayerShip playerShip;
    private ArrayList<Bullet> bullets;         // Bullets
    private ArrayList<Asteroid> asteroids;     // Asteroids
    private ArrayList<Star> stars;            // Stars
    private boolean gameRunning;               // Running
    private boolean gameOver;                 // Game over
    private boolean gamePaused;               // Paused
    private double score;                     // Score

    public static final int WIDTH = 1920;      // Width
    public static final int HEIGHT = 1080;     // Height

    private int movement_dx = 10;

    private long lastFireTime; // Last shot
    private static final long BASE_FIRE_DELAY = 1000;  // Initial delay
    private static final double MIN_FIRE_DELAY = 0.6;
    private static final double FIRE_SCALE_FACTOR = 40.0;  // speed-up
    private final int ADDITIONAL_BULLET_LEVEL_1 = 300;
    private final int ADDITIONAL_BULLET_LEVEL_2 = 600;

    private boolean[] keyStates = new boolean[1024]; // Key states

    private static final double SCALE_FACTOR = 200;  // spawn  increase
    private static final double MAX_SPAWN_PROBABILITY = 0.8;  // ca

    private static final double SCORE_INCREASE_CYCLE = 0.01;

    private static Circle earth;

    private String playerName = "";       // Store the player's name
    private String currentInput = "";     // Temporary input from the user while typing
    private boolean scoreSaved = false;

    // Pause Menu Components
    private JPanel pausePanel;
    private JButton restartButton;
    private JButton exitButton;


    // STRINGS
    private final String BTN_RESTART = "RICOMINCIA";
    private final String BTN_EXIT = "RITORNA AL MENU";

    private final String GUI_SCORE = "Punteggio: ";
    private final String GUI_LIVES = "Vite: ";
    private final String GUI_LAST_LIVE = "!! ULTIMA VITA !!";
    private final String GUI_GAMEOVER = "Game Over!";
    private final String GUI_INSERTSCORE = "Inserisci il nome (Premi ENTER per confermare): ";
    private final String GUI_NAME = "Nome: ";
    private final String GUI_PAUSED = "IN PAUSA";


    // Images
    private final String buttonImagePath = "images/button.png";

    public Game() {
        playerShip = new PlayerShip(WIDTH / 2 - playerShip.width / 2, 900);
        asteroids = new ArrayList<>();
        bullets = new ArrayList<>();
        stars = new ArrayList<>();
        score = 0.0;
        gameRunning = true;
        gameOver = false;
        gamePaused = false;

        earth = new Circle(WIDTH / 2, HEIGHT); // Create circle at spaceship position

        setFocusable(true);  // Ensure this is called before the key listener
        requestFocusInWindow();  // Make sure the panel has focus

        // Initialize pause panel and buttons
        pausePanel = new JPanel();
        pausePanel.setLayout(new BoxLayout(pausePanel, BoxLayout.Y_AXIS));  // Align components vertically
        pausePanel.setOpaque(false);  // Make the panel transparent

        // Center the pausePanel within its container (add this code below the setOpaque)
        pausePanel.setAlignmentX(Component.CENTER_ALIGNMENT);  // Horizontally center the panel

        // Set up restart button
        pausePanel.add(Box.createVerticalStrut(600));
        pausePanel.add(createButton(BTN_RESTART, buttonImagePath, e -> resetGame()));
        pausePanel.add(Box.createVerticalStrut(50));

        // Set up exit button
        pausePanel.add(createButton(BTN_EXIT, buttonImagePath, e -> exitFrame()));
        pausePanel.add(Box.createVerticalStrut(150));


        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                keyStates[e.getKeyCode()] = true;  // Key pressed

                // Handle input during game over (name input)
                if (gameOver && playerName.isEmpty()) {
                    int keyCode = e.getKeyCode();

                    // Allow backspace to delete the last character
                    if (keyCode == KeyEvent.VK_BACK_SPACE && currentInput.length() > 0) {
                        currentInput = currentInput.substring(0, currentInput.length() - 1);
                    }
                    // Handle alphanumeric input for name
                    else if (Character.isLetterOrDigit(e.getKeyChar()) || e.getKeyCode() == KeyEvent.VK_SPACE) {
                        currentInput += e.getKeyChar();
                    }

                    // Enter key to finalize the name
                    if (keyCode == KeyEvent.VK_ENTER && !currentInput.isEmpty()) {
                        playerName = currentInput;  // Save the name
                        currentInput = "";          // Clear temporary input
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                keyStates[e.getKeyCode()] = false;  // Key released
            }
        });

        new Thread(this).start(); // Start the game loop
    }

    @Override
    public void run() {
        while (gameRunning) {
            if (!gamePaused) {
                update(); // Update game state
                checkCollisions(); // Check collisions
            }


            if (!gameOver) {
                handlePauseKeys(); // Handle pause/unpause functionality
            }

            repaint(); // Redraw the screen
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

        if (keyStates[KeyEvent.VK_LEFT] || keyStates[KeyEvent.VK_A]) {
            playerShip.move(-movement_dx); // Move left
        }

        if (keyStates[KeyEvent.VK_RIGHT] || keyStates[KeyEvent.VK_D]) {
            playerShip.move(movement_dx);  // Move right
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
        double fireDelay =  BASE_FIRE_DELAY / (1 + ((score / 100) / FIRE_SCALE_FACTOR)); //BASE_FIRE_DELAY;
        double minFireDelay = MIN_FIRE_DELAY; // Your desired minimum fire delay

        fireDelay = Math.max(fireDelay, minFireDelay);
        if (currentTime - lastFireTime >= fireDelay) {
            bullets.add(new Bullet(playerShip.getX() + playerShip.getWidth() / 2 - 4, playerShip.getY())); // Middle
            if (score > ADDITIONAL_BULLET_LEVEL_1) {
                bullets.add(new Bullet(playerShip.getX() + playerShip.getWidth(), playerShip.getY() + playerShip.getHeight() / 2 - 4)); // Right
            }
            if (score > ADDITIONAL_BULLET_LEVEL_2) {
                bullets.add(new Bullet(playerShip.getX(), playerShip.getY() + playerShip.getHeight() / 2 - 4)); // Left
            }
            lastFireTime = currentTime;  // Update the last fire time
        }
    }

    private void checkCollisions() {
        for (Asteroid asteroid : asteroids) {
            if (checkCollision(playerShip, asteroid)) {
                if (playerShip.getLives() == 1) {
                    gameOver = true; // Game over
                    gameRunning = false; // Stop game
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

        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/PressStart2P-vaV7.ttf")).deriveFont(Font.PLAIN, 32);
            g.setFont(customFont);
        } catch (Exception e) {
            e.printStackTrace();
            g.setFont(new Font("SansSerif", Font.PLAIN, 12));
        }

        // Draw the start circle (if it exists)
        if (earth != null) {
            earth.draw(g); // Draw the circle as it moves up
        }

        setBackground(Color.BLACK); // Background

        g.setColor(new Color(123, 0, 0, 230));  // Semi-transparent black background for contrast

        // Points of the parallelogram (define the four corners)
        int[] xPoints = {0, 700, 600, 0}; // X coordinates of the parallelogram
        int[] yPoints = {0, 0, 100, 100};  // Y coordinates of the parallelogram

        // Draw the parallelogram
        g.fillPolygon(xPoints, yPoints, 4);

        g.setColor(Color.WHITE);
        g.drawString(GUI_SCORE + (int) score, 10, 50); // Score display

        g.drawString(GUI_LIVES + playerShip.getLives(), 10, 90); // Lives display
        if(playerShip.getLives() == 1 && !this.gameOver){
            g.setColor(Color.RED);
            g.drawString(GUI_LAST_LIVE, WIDTH / 2 - 280, 240); // Lives display
            g.setColor(Color.WHITE);
        }

        playerShip.draw(g); // Draw player ship

        for (Asteroid asteroid : asteroids) {
            asteroid.draw(g);
        }

        for (Bullet bullet : bullets) {
            bullet.draw(g);
        }

        for (Star star : stars) {
            star.draw(g);
        }

        // Game over screen
        if (gameOver) {
            g.setColor(Color.RED);
            g.drawString(GUI_GAMEOVER, WIDTH / 2 - 150, HEIGHT / 2 - 350); // Game over screen
            g.drawString(GUI_SCORE + (int) score, WIDTH / 2 - 170, HEIGHT / 2 - 300); // Score
            gamePaused = true;

            if (playerName.isEmpty()) {
                // Ask the user to enter a name
                g.setColor(Color.WHITE);
                g.drawString(GUI_INSERTSCORE, 160, HEIGHT / 2);

                // Draw the user's input name (if they are typing)
                g.drawString(currentInput, WIDTH / 2 - 150, HEIGHT / 2 + 50); // Show typed name
            } else {
                // After the user has typed their name, display it along with the score
                g.drawString(GUI_NAME + playerName, WIDTH / 2 - 250, HEIGHT / 2);
                g.drawString(GUI_SCORE + (int) score, WIDTH / 2 - 150, HEIGHT / 2 + 50);
                // Score
                if (!scoreSaved) {
                    Leaderboard leaderboard = new Leaderboard();
                    leaderboard.saveScore(playerName, (int) score);
                    scoreSaved = true; // Mark score as saved
                }
            }

            showPauseMenu(); // Show the pause menu with restart/exit buttons
        }

        // Paused state
        if (gamePaused) {
            if(!gameOver){
                g.setColor(Color.YELLOW);
                g.drawString(GUI_PAUSED, WIDTH / 2 - 120, HEIGHT / 2); // Pause message
            }
            showPauseMenu(); // Show the pause menu with restart/exit buttons
        }else{
            remove(pausePanel);
        }
    }


    private void showPauseMenu() {
        // Ensure the buttons are only shown when the game is paused
        if (!pausePanel.isAncestorOf(this)) {
            add(pausePanel, BorderLayout.CENTER);  // Add the pause panel to the center
            revalidate();                         // Revalidate the layout after adding
            repaint();                            // Repaint to show the updated screen
        }else{
            remove(pausePanel);
        }
    }


    public void handlePauseKeys() {
        // Check if the user presses 'Escape' or 'P' to toggle the pause state
        if (keyStates[KeyEvent.VK_ESCAPE] || keyStates[KeyEvent.VK_P]) {
            // Toggle pause state only if the key was just pressed
            if (!gamePaused) {
                gamePaused = true;  // Pause the game
            } else {
                gamePaused = false; // Unpause the game
            }

            // Ensure we don't toggle the pause state every frame
            keyStates[KeyEvent.VK_ESCAPE] = false;  // Reset the key state after the pause toggle
            keyStates[KeyEvent.VK_P] = false;      // Reset the key state after the pause toggle
        }
    }

    private void exitFrame() {
        Menu menu = new Menu();  // Assuming you have a Menu class
        this.setVisible(false);  // Hide the current game panel
        menu.setVisible(true);   // Show the menu
        menu.setFocusable(true); // Ensure this is called before the key listener
        menu.requestFocusInWindow(); // Make sure the panel has focus

        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);  // Get the parent JFrame
        frame.dispose();  // Close the current frame and release resources
    }

    public void resetGame() {
        playerShip = new PlayerShip(WIDTH / 2 - playerShip.width / 2, 900);
        asteroids.clear();
        bullets.clear();
        stars.clear();
        score = 0.0;
        gameRunning = true;
        gameOver = false;
        gamePaused = false;
        earth = new Circle(WIDTH / 2, HEIGHT); // Create circle at spaceship position
        scoreSaved = false;  // Reset the flag so the score can be saved again
    }

    private JButton createButton(String text, String imagePath, ActionListener action) {
        JButton button = new JButton(text);

        ImageIcon icon = new ImageIcon(imagePath);
        Image img = icon.getImage().getScaledInstance(200, 60, Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(img));

        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);

        button.setVerticalTextPosition(SwingConstants.CENTER);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/PressStart2P-vaV7.ttf")).deriveFont(Font.PLAIN, 12);
            button.setFont(customFont);
        } catch (Exception e) {
            e.printStackTrace();
            button.setFont(new Font("SansSerif", Font.PLAIN, 12));
        }
        button.setForeground(Color.WHITE);

        button.addActionListener(action);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
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
