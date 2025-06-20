import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class StartMenu extends JPanel {
    private static final long serialVersionUID = 1L;

    public StartMenu(JFrame frame) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.DARK_GRAY);
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel title = new JLabel("Tic Tac Toe");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        add(title);
        add(Box.createRigidArea(new Dimension(0, 30)));

        // Create buttons
        addButton(frame, "Play Duo", () -> startDuoGame(frame));
        addButton(frame, "Play with Bot", () -> JOptionPane.showMessageDialog(frame, "Bot mode not implemented yet."));
        addButton(frame, "Play Multiplayer", () -> startMultiplayerGame(frame));
        addButton(frame, "Options", () -> startSettingsMenu(frame));
        addButton(frame, "Exit Game", () -> System.exit(0));

    }

    private JButton createButton(String text, Color color, Runnable action) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setAlignmentY(10);
        button.setFont(new Font("SegoeUI", Font.BOLD, 20));
        button.setMaximumSize(new Dimension(240, 50));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);

        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addActionListener(e -> action.run());
        return button;
    }

    private void startDuoGame(JFrame frame) {
        String playerX = JOptionPane.showInputDialog(frame, "Enter name for Player X:", "Player X",
                JOptionPane.QUESTION_MESSAGE);
        if (playerX == null || playerX.trim().isEmpty())
            playerX = "Player X";

        String playerO = JOptionPane.showInputDialog(frame, "Enter name for Player O:", "Player O",
                JOptionPane.QUESTION_MESSAGE);
        if (playerO == null || playerO.trim().isEmpty())
            playerO = "Player O";

        frame.setContentPane(new GameMain(playerX, playerO));

 // refresh UI

        frame.setSize(400,400);
        frame.setResizable(false);
        frame.revalidate();
        frame.repaint();
    }

    private void startBotGame(JFrame frame) {
        frame.setContentPane(new GameBotMain("Player X"));
        frame.setSize(400,400);
        frame.setResizable(false);
        frame.revalidate();
        frame.repaint();
    }

    private void startSettingsMenu(JFrame frame) {
        frame.setContentPane(new SettingsMenu());
        frame.revalidate(); // refresh UI
        frame.repaint();

    }

    private void startMultiplayerGame(JFrame frame) {
        String playerName = JOptionPane.showInputDialog(frame, "Enter your username:", "Player X", JOptionPane.QUESTION_MESSAGE);
        if (playerName == null || playerName.trim().isEmpty()) playerName = "Unnamed Player";

        MultiplayerGameMain gamePanel = new MultiplayerGameMain(playerName);
        frame.setContentPane(gamePanel);
        frame.setSize(400,400);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                gamePanel.deleteGame();
                System.exit(0);
            }
        });

        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
