import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Timer;

public class Game {
    private PlayerShip player;
    private ArrayList<Bullet> bullets;         // Active Bullets
    private ArrayList<Asteroid> asteroids;     // Active Asteroids
    private Timer timer;                        // Timer for updates
    private Leaderboard leaderboard;            // Leaderbords ref
    private boolean gameRunning;                // True or False, status of the game

    //Window size
    private final int WIDTH = 800;             // Window width
    private final int HEIGHT = 600;            // Window Height

    // Costruttore
    public Game() {
        // player = new PlayerShip(WIDTH / 2, HEIGHT - 50);
        bullets = new ArrayList<>();
        asteroids = new ArrayList<>();
        leaderboard = new Leaderboard();
        gameRunning = true;
    }
}
