import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;

public class Menu extends JFrame {

    // Costruttore del menu
    public Menu() {
        setTitle("Game Menu");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.BLACK);

        // BorderLayout come layout principale
        setLayout(new BorderLayout());

        // Sezione superiore: Titolo
        add(createTopPanel(), BorderLayout.NORTH);

        // Sezione centrale: Immagine e Istruzioni
        add(createCenterPanel(), BorderLayout.CENTER);

        // Sezione inferiore: Pulsanti
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setOpaque(false);

        // Titoli centrati
        topPanel.add(Box.createVerticalStrut(150));
        topPanel.add(createLabel("GEMX", "fonts/SpaceX.ttf", Font.ITALIC, 32, Color.GRAY));
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(createLabel("SPACESHIP", "fonts/PressStart2P-vaV7.ttf", Font.PLAIN, 70, Color.WHITE));
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(createLabel("THE GAME", "fonts/PressStart2P-vaV7.ttf", Font.PLAIN, 24, Color.WHITE));
        topPanel.add(Box.createVerticalStrut(80));

        return topPanel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        // Istruzioni
        centerPanel.add(createLabel("ISTRUZIONI","fonts/PressStart2P-vaV7.ttf", Font.BOLD, 14, Color.WHITE));
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(createLabel("Guida la navicella","fonts/PressStart2P-vaV7.ttf", Font.PLAIN, 10, Color.GRAY));
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(createLabel("evita o distruggi gli asteroidi.","fonts/PressStart2P-vaV7.ttf", Font.PLAIN, 10, Color.GRAY));
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(createLabel("più sopravvivi, più punti guadagni.","fonts/PressStart2P-vaV7.ttf", Font.PLAIN, 10, Color.GRAY));
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(createLabel("Buona fortuna, pilota!","fonts/PressStart2P-vaV7.ttf", Font.PLAIN, 10, Color.GRAY));

        centerPanel.add(Box.createVerticalStrut(50));

        // Immagine
        ImageIcon icon = new ImageIcon("images/keyboard.png");
        Image img = icon.getImage().getScaledInstance(250, 150, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(img));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(imageLabel);

        return centerPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

        // Pulsanti
        String buttonImagePath = "images/button.png";
        buttonPanel.add(createButton("PLAY", buttonImagePath, e -> startGame()));
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(createButton("LEADERBOARD", buttonImagePath, e -> openLeaderBoard()));
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(createButton("EXIT", buttonImagePath, e -> exitGame()));
        buttonPanel.add(Box.createVerticalStrut(150));

        return buttonPanel;
    }

    private JLabel createLabel(String text, String fontPath, int style, int size, Color color) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath)).deriveFont(style, size);
            label.setFont(customFont);
        } catch (Exception e) {
            e.printStackTrace();
            label.setFont(new Font("SansSerif", style, size)); // Fallback al font di default
        }

        label.setForeground(color);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

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

    private void startGame() {
        Game game = new Game();
    }

    private void openLeaderBoard() {
        System.out.println("Mostra la leaderboard...");
    }

    private void exitGame() {
        System.exit(0); // Chiude l'applicazione
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Menu menu = new Menu();
            menu.setVisible(true);
        });
    }
}
