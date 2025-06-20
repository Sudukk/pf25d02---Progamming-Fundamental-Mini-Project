import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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
        addButton(frame, "Play with Bot", () -> startBotGame(frame));
        addButton(frame, "Play Multiplayer", () -> startMultiplayerGame(frame));
        addButton(frame, "Options", () -> JOptionPane.showMessageDialog(frame, "Options menu not implemented yet."));
        addButton(frame, "Exit Game", () -> System.exit(0));
    }

    private void addButton(JFrame frame, String text, Runnable action) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 40));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        button.addActionListener(e -> action.run());
        add(button);
        add(Box.createRigidArea(new Dimension(0, 15)));
    }

    private void startDuoGame(JFrame frame) {
        String playerX = JOptionPane.showInputDialog(frame, "Enter name for Player X:", "Player X", JOptionPane.QUESTION_MESSAGE);
        if (playerX == null || playerX.trim().isEmpty()) playerX = "Player X";

        String playerO = JOptionPane.showInputDialog(frame, "Enter name for Player O:", "Player O", JOptionPane.QUESTION_MESSAGE);
        if (playerO == null || playerO.trim().isEmpty()) playerO = "Player O";

        frame.setContentPane(new GameMain(playerX, playerO));
        frame.revalidate();  // refresh UI
        frame.repaint();
    }

    private void startBotGame(JFrame frame) {

        frame.setContentPane(new GameBotMain("Player X"));
        frame.revalidate();  // refresh UI
        frame.repaint();
    }

    private void startMultiplayerGame(JFrame frame) {

        MultiplayerGameMain gamePanel = new MultiplayerGameMain();
        frame.setContentPane(gamePanel);
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
