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
        addButton(frame, "Play with Bot", () -> JOptionPane.showMessageDialog(frame, "Bot mode not implemented yet."));
        addButton(frame, "Play Multiplayer", () -> JOptionPane.showMessageDialog(frame, "Multiplayer not implemented yet."));
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
}
