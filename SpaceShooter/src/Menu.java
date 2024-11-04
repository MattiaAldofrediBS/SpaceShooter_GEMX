import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JFrame {

    // Costruttore Menu
    public Menu(){
        setTitle("Game Menu");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 10));

        JButton button1 = new JButton("Start Game");
        JButton button2 = new JButton("Settings");
        JButton button3 = new JButton("Exit");

        panel.add(button1);
        panel.add(button2);
        panel.add(button3);

        add(panel);

        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });

        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openLeaderBoard();
            }
        });

        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exitGame();
            }
        });
    }

    private void startGame(){
        Game game = new Game();
    }

    private void openLeaderBoard(){
        Leaderboard lb = new Leaderboard();
        //lb.displayScores();
    }

    private void exitGame(){
        System.exit(0);
    }

    // Menu di gioco
    void draw(Graphics g) {}

    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.setVisible(true);
    }
}
