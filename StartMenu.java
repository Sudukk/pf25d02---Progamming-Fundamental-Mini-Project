import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class StartMenu extends JPanel {
    private static final long serialVersionUID = 1L;

    public StartMenu(JFrame frame) {
        setLayout(new BorderLayout());
        setBackground(new Color(0x00BCD4)); // Warna biru toska terang

        // Panel tengah untuk tombol dan logo
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        // Tambahkan logo Tic Tac Toe
        URL logoURL = getClass().getResource("/assets/logo_tictactoe.png");
        if (logoURL != null) {
            JLabel logoLabel = new JLabel(new ImageIcon(logoURL));
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            centerPanel.add(Box.createVerticalStrut(30));
            centerPanel.add(logoLabel);
        } else {
            System.err.println("⚠ Logo image not found!");
            JLabel fallback = new JLabel("Tic Tac Toe");
            fallback.setFont(new Font("Arial", Font.BOLD, 36));
            fallback.setForeground(Color.WHITE);
            fallback.setAlignmentX(Component.CENTER_ALIGNMENT);
            centerPanel.add(Box.createVerticalStrut(30));
            centerPanel.add(fallback);
        }

        centerPanel.add(Box.createVerticalStrut(40));
        centerPanel.add(createButton("Play Now!", new Color(255, 193, 7), () -> startDuoGame(frame)));
        centerPanel.add(createButton("Play with AI", new Color(33, 150, 243), () -> startBotGame(frame)));
        centerPanel.add(createButton("Multiplayer", new Color(76, 175, 80), () -> startMultiplayerGame(frame)));

        add(centerPanel, BorderLayout.CENTER);

        // Panel bawah untuk tombol options dan exit
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 15));
        bottomPanel.setOpaque(false);

        // Tombol options (ikon roda gigi)
        URL gearURL = getClass().getResource("/assets/icon_settings.png");
        JButton btnOptions;
        if (gearURL != null) {
            btnOptions = new JButton(new ImageIcon(gearURL));
        } else {
            System.err.println("⚠ Gear icon not found!");
            btnOptions = new JButton("⚙");
        }

        btnOptions.setPreferredSize(new Dimension(48, 48));
        btnOptions.setContentAreaFilled(false);
        btnOptions.setBorderPainted(false);
        btnOptions.setFocusPainted(false);
        btnOptions.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnOptions.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Options menu coming soon!"));
        bottomPanel.add(btnOptions);

        // Tombol Exit
        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 16));
        exitButton.setBackground(Color.RED);
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.setPreferredSize(new Dimension(100, 40));
        exitButton.addActionListener(e -> System.exit(0));
        bottomPanel.add(exitButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JButton createButton(String text, Color color, Runnable action) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        button.setMaximumSize(new Dimension(240, 50));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        button.addActionListener(e -> action.run());
        return button;
    }

    private void startDuoGame(JFrame frame) {
        String playerX = JOptionPane.showInputDialog(frame, "Enter name for Player X:", "Player X", JOptionPane.QUESTION_MESSAGE);
        if (playerX == null || playerX.trim().isEmpty()) playerX = "Player X";

        String playerO = JOptionPane.showInputDialog(frame, "Enter name for Player O:", "Player O", JOptionPane.QUESTION_MESSAGE);
        if (playerO == null || playerO.trim().isEmpty()) playerO = "Player O";

        frame.setContentPane(new GameMain(playerX, playerO));
        frame.revalidate();
        frame.repaint();
    }

    private void startBotGame(JFrame frame) {
        frame.setContentPane(new GameBotMain("Player X"));
        frame.revalidate();
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
