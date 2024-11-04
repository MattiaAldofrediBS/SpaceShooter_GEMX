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
                openSettings();
            }
        });

        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exitGame();
            }
        });
    }

    private void startGame(){}
    private void openSettings(){}
    private void exitGame(){}

    // Menu di gioco
    void draw(Graphics g) {}

    // Gestisce gli input utente
    void handleInput(){}
}
