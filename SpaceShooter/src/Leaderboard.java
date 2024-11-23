import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class Leaderboard extends JPanel {
    private ArrayList<ScoreEntry> scores; // List of saved scores and names
    private static final String SCORE_FILE = "scores.txt"; // File name for saving scores
    private JPanel exitPanel;

    // Strings for error messages
    final String ERROR_LOAD_FILE = "Error loading scores: ";
    final String ERROR_WRITE_FILE = "Error saving score: ";

    // Constructor
    public Leaderboard() {
        scores = new ArrayList<>();
        loadScores(); // Load scores on start

        setFocusable(true);  // Ensure this is called before the key listener
        requestFocusInWindow();  // Make sure the panel has focus

        // Set the background to black
        setBackground(Color.BLACK);

        // Initialize exit panel and buttons
        exitPanel = new JPanel();
        exitPanel.setLayout(new BoxLayout(exitPanel, BoxLayout.Y_AXIS));  // Align components vertically
        exitPanel.setOpaque(false);  // Make the panel transparent

        // Center the exitPanel within its container
        exitPanel.setAlignmentX(Component.CENTER_ALIGNMENT);  // Horizontally center the panel
        String buttonImagePath = "images/button.png";

        // Set up exit button
        exitPanel.add(createButton("MENU", buttonImagePath, e -> exitFrame()));
        exitPanel.add(Box.createVerticalStrut(150));

        // Add the exit panel to the leaderboard panel
        setLayout(new BorderLayout());
        add(exitPanel, BorderLayout.SOUTH); // Always visible at the bottom
    }

    // Loads high scores from the file
    public void loadScores() {
        try (BufferedReader br = new BufferedReader(new FileReader(SCORE_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.trim().split(",");
                if (parts.length == 2) {
                    String name = parts[0];
                    int score = Integer.parseInt(parts[1]);
                    scores.add(new ScoreEntry(name, score)); // Add score entry
                }
            }
        } catch (IOException e) {
            System.out.println(ERROR_LOAD_FILE + e.getMessage());
        }
    }

    // Writes a new score and name to the file
    public void saveScore(String name, int score) {
        scores.add(new ScoreEntry(name, score)); // Add score entry to the list
        Collections.sort(scores); // Sort by score in descending order
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SCORE_FILE))) {
            for (ScoreEntry entry : scores) {
                bw.write(entry.getName() + "," + entry.getScore()); // Write name and score
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println(ERROR_WRITE_FILE + e.getMessage());
        }
    }

    // Displays the scores centered on the screen
    public void displayScores(Graphics g) throws IOException, FontFormatException {
        g.setColor(Color.WHITE); // Set text color to white
        String title = "Classifica:";

        Font titleFont = null;
        try {
            titleFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/PressStart2P-vaV7.ttf")).deriveFont(Font.PLAIN, 35);
        } catch (Exception e) {
            e.printStackTrace();
            titleFont = new Font("SansSerif", Font.PLAIN, 35);
        }

        Font scoreFont = null;
        try {
            scoreFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/PressStart2P-vaV7.ttf")).deriveFont(Font.PLAIN, 14);
        } catch (Exception e) {
            e.printStackTrace();
            scoreFont = new Font("SansSerif", Font.PLAIN, 20);
        }

        // Set fonts for title and scores
        g.setFont(titleFont);
        g.drawString(title, getWidth() / 2 - g.getFontMetrics().stringWidth(title) / 2, 150); // Center title

        // Draw the scores below the title
        g.setFont(scoreFont);
        int y = 250; // Starting Y position for scores
        for (int i = 0; i < scores.size() && i <= 19; i++) {
            ScoreEntry entry = scores.get(i);
            String scoreText = (i + 1) + ". " + entry.getName() + ": " + entry.getScore();
            g.drawString(scoreText, getWidth() / 2 - g.getFontMetrics().stringWidth(scoreText) / 2, y);
            y += 30; // Line height
        }
    }

    // Getter for the scores
    public ArrayList<ScoreEntry> getScores() {
        return scores;
    }

    // Create a button with an image and an action listener
    private JButton createButton(String text, String imagePath, ActionListener action) {
        JButton button = new JButton(text);

        ImageIcon icon = new ImageIcon(imagePath);
        Image img = icon.getImage().getScaledInstance(180, 40, Image.SCALE_SMOOTH);
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

    // Exit game action
    private void exitFrame() {
        Menu menu = new Menu();  // Assuming you have a Menu class
        this.setVisible(false);  // Hide the current leaderboard panel
        menu.setVisible(true);   // Show the menu
        menu.setFocusable(true); // Ensure this is called before the key listener
        menu.requestFocusInWindow(); // Ensure the panel has focus

        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);  // Get the parent JFrame
        frame.dispose();  // Close the current frame and release resources
    }

    // Paint component method for displaying the scores
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        try {
            displayScores(g); // Draw leaderboard scores
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        }
    }

    // ScoreEntry class to hold the name and score
    public static class ScoreEntry implements Comparable<ScoreEntry> {
        private String name;
        private int score;

        public ScoreEntry(String name, int score) {
            this.name = name;
            this.score = score;
        }

        public String getName() {
            return name;
        }

        public int getScore() {
            return score;
        }

        @Override
        public int compareTo(ScoreEntry other) {
            return Integer.compare(other.score, this.score); // Sort by score descending
        }
    }
}
