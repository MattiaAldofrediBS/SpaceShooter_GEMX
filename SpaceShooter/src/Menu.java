import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Menu extends JFrame {

    // Costruttore del menu
    public Menu() {
        setTitle("Game Menu"); // Titolo della finestra
        setSize(500, 600); // Dimensioni della finestra
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Chiudi il programma quando la finestra è chiusa
        setLocationRelativeTo(null); // Centra la finestra sullo schermo
        getContentPane().setBackground(Color.BLACK); // Colore di sfondo della finestra

        // Imposta il layout generale a BorderLayout
        setLayout(new BorderLayout());

        // Aggiunge i pannelli principali al frame
        add(createTopPanel(), BorderLayout.NORTH);  // Pannello superiore (titolo)
        add(createCenterPanel(), BorderLayout.CENTER); // Pannello centrale (immagine e istruzioni)
        add(createButtonPanel(), BorderLayout.SOUTH); // Pannello inferiore (pulsanti)
    }

    // Metodo per creare il pannello superiore con il titolo
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS)); // Layout verticale
        topPanel.setOpaque(false); // Pannello trasparente (usa lo sfondo nero della finestra)

        // Aggiunge le etichette con il titolo del gioco
        topPanel.add(createLabel("Game Title", new Font("SansSerif", Font.BOLD, 24))); // Titolo principale
        topPanel.add(createLabel("Subtitle", new Font("SansSerif", Font.BOLD, 18)));   // Sottotitolo

        return topPanel;
    }

    // Metodo per creare il pannello centrale con un'immagine e istruzioni
    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        // Placeholder per l'immagine
        JLabel imageLabel = createLabel("Place Image Here", new Font("Arial", Font.PLAIN, 14));
        imageLabel.setPreferredSize(new Dimension(300, 150)); // Dimensione dell'immagine fittizia
        centerPanel.add(imageLabel);

        // Aggiunge un'area di testo con le istruzioni
        JTextArea instructions = new JTextArea("Game Instructions:\n- Use arrow keys to move\n- Avoid obstacles\n- Collect points");
        instructions.setFont(new Font("Arial", Font.PLAIN, 14)); // Stile del testo
        instructions.setEditable(false); // Rende il testo non modificabile
        instructions.setLineWrap(true); // Avvolge il testo
        instructions.setWrapStyleWord(true); // Avvolge per parola
        instructions.setOpaque(false); // Sfondo trasparente
        instructions.setForeground(Color.WHITE); // Colore del testo
        instructions.setPreferredSize(new Dimension(300, 100)); // Dimensione del box

        // Aggiunge l'immagine e le istruzioni al pannello
        centerPanel.add(Box.createVerticalStrut(10)); // Spazio tra immagine e istruzioni
        centerPanel.add(instructions);

        return centerPanel;
    }

    // Metodo per creare il pannello inferiore con i pulsanti
    // Pannello inferiore con i pulsanti
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS)); // BoxLayout in verticale
        buttonPanel.setOpaque(false); // Sfondo trasparente

        // Aggiunge i pulsanti con le relative azioni
        buttonPanel.add(createButton("Start Game", e -> startGame()));
        buttonPanel.add(Box.createVerticalStrut(10)); // Spazio tra i pulsanti
        buttonPanel.add(createButton("Leaderboard", e -> openLeaderBoard()));
        buttonPanel.add(Box.createVerticalStrut(10)); // Spazio tra i pulsanti
        buttonPanel.add(createButton("Exit", e -> exitGame()));

        return buttonPanel;
    }

    // Metodo per creare un'etichetta con testo e font specifico
    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text, SwingConstants.CENTER); // Crea l'etichetta con il testo e la centra
        label.setFont(font); // Imposta il font (carattere) dell'etichetta
        label.setForeground(Color.WHITE); // Imposta il colore del testo a bianco
        label.setAlignmentX(Component.CENTER_ALIGNMENT); // Centra l'etichetta orizzontalmente
        return label; // Ritorna l'etichetta pronta per essere aggiunta
    }

    // Metodo per creare un pulsante con testo e azione specifica
    private JButton createButton(String text, ActionListener action) {
        JButton button = new JButton(text); // Crea un pulsante con il testo specificato
        button.addActionListener(action); // Assegna l'azione che il pulsante dovrà eseguire
        button.setAlignmentX(Component.CENTER_ALIGNMENT); // Centra il pulsante orizzontalmente
        return button; // Ritorna il pulsante pronto per essere aggiunto
    }

    // Azioni dei pulsanti
    private void startGame() {
        // Logica per avviare il gioco (es. creare una nuova finestra di gioco)
        System.out.println("Avvio del gioco...");
    }

    private void openLeaderBoard() {
        // Logica per aprire la leaderboard (es. mostrare una finestra con i punteggi)
        System.out.println("Mostra la leaderboard...");
    }

    private void exitGame() {
        System.exit(0); // Chiude l'applicazione
    }

    // Metodo main per avviare l'applicazione
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Menu menu = new Menu(); // Crea una nuova istanza del menu
            menu.setVisible(true); // Rende visibile la finestra
        });
    }
}
