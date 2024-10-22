import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class Leaderboard {
    private ArrayList<Integer> scores; // Lista dei punteggi salvati
    private static final String SCORE_FILE = "scores.txt"; // Nome del file per il salvataggio

    // Strings
    final String ERROR_LOAD_FILE = "Errore nel caricamento dei punteggi: ";
    final String ERROR_WRITE_FILE ="Errore nel salvataggio del punteggio: ";
    // Constructor
    public Leaderboard() {
        scores = new ArrayList<>();
        loadScores(); // Carica i punteggi all'avvio
    }

    // Loads highscores file
    public void loadScores() {
        try (BufferedReader br = new BufferedReader(new FileReader(SCORE_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                scores.add(Integer.parseInt(line.trim())); // Add a score
            }
        } catch (IOException e) {
            System.out.println(ERROR_LOAD_FILE + e.getMessage());
        }
    }

    // Writes a new score in the file
    public void saveScore(int score) {
        scores.add(score); // Adds score to the list
        Collections.sort(scores, Collections.reverseOrder()); // Dec order
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SCORE_FILE))) {
            for (Integer s : scores) {
                bw.write(s.toString()); // Writes the score into the file
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println(ERROR_WRITE_FILE + e.getMessage());
        }
    }

    // Shows the scores
    public void displayScores(Graphics g) {
        g.drawString("Leaderboard:", 10, 20); // Title
        int y = 40; // Starting Y
        for (int i = 0; i < scores.size(); i++) {
            g.drawString((i + 1) + ". " + scores.get(i), 10, y); // Shows
            y += 20; // Interline
        }
    }

    // Getter
    public ArrayList<Integer> getScores() {
        return scores;
    }
}
