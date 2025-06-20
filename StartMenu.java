import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class StartMenu extends JPanel {
    private static final long serialVersionUID = 1L;

    public StartMenu(JFrame frame) {

        setLayout(new BorderLayout());
        setBackground(new Color(2,21,38)); // Warna biru toska terang

        // Panel tengah untuk tombol dan logo
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        // Tambahkan logo Tic Tac Toe

        URL logoURL = getClass().getClassLoader().getResource("images/tictactoe_logo.png");
        if (logoURL != null) {
            ImageIcon originalLogo = new ImageIcon(logoURL);
            Image scaledImage = originalLogo.getImage().getScaledInstance(640, 360, Image.SCALE_SMOOTH);
            ImageIcon scaledLogoIcon = new ImageIcon(scaledImage);
            JLabel logoLabel = new JLabel(scaledLogoIcon);
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            centerPanel.add(Box.createVerticalStrut(30));
            centerPanel.add(logoLabel);

        } else {
            System.err.println("Logo image not found!");
            JLabel fallback = new JLabel("Tic Tac Toe");
            fallback.setFont(new Font("SegoeUI", Font.BOLD, 36));
            fallback.setForeground(Color.WHITE);
            fallback.setAlignmentX(Component.CENTER_ALIGNMENT);
            centerPanel.add(Box.createVerticalStrut(30));
            centerPanel.add(fallback);
        }

//        URL logoURL = getClass().getResource("/images/tictactoe_logo.png");
//        if (logoURL != null) {
//            JLabel logoLabel = new JLabel(new ImageIcon(logoURL));
//            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//            centerPanel.add(Box.createVerticalStrut(30));
//            centerPanel.add(logoLabel);
//        } else {
//            System.err.println("Logo image not found!");
//            JLabel fallback = new JLabel("Tic Tac Toe");
//            fallback.setFont(new Font("SegoeUI", Font.BOLD, 36));
//            fallback.setForeground(Color.WHITE);
//            fallback.setAlignmentX(Component.CENTER_ALIGNMENT);
//            centerPanel.add(Box.createVerticalStrut(30));
//            centerPanel.add(fallback);
//        }

        centerPanel.add(Box.createVerticalStrut(40));
        centerPanel.add(createButton("Play Now!", new Color(2,21,38), () -> startDuoGame(frame)));
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(createButton("Play with AI", new Color(2,21,38), () -> startBotGame(frame)));
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(createButton("Multiplayer", new Color(2,21,38), () -> startMultiplayerGame(frame)));

        add(centerPanel, BorderLayout.CENTER);

        // Panel bawah terpisah kiri dan kanan
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);

        // Panel kiri untuk tombol options
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        leftPanel.setOpaque(false);

        // Tombol options
        URL gearURL = getClass().getClassLoader().getResource("images/gear_icon.png");
        JButton btnOptions;
        if (gearURL != null) {
            ImageIcon originalIcon = new ImageIcon(gearURL);
            Image scaledImage = originalIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            btnOptions = new JButton(scaledIcon);
        } else {
            System.err.println("Icon not found!!");
            btnOptions = new JButton("Options");
        }
        btnOptions.setPreferredSize(new Dimension(60, 40));
        btnOptions.setContentAreaFilled(false);
        btnOptions.setBorderPainted(false);
        btnOptions.setFocusPainted(false);
        btnOptions.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnOptions.addActionListener(e -> startSettingsMenu(frame));
        leftPanel.add(btnOptions);

        // Panel kanan untuk tombol exit
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        rightPanel.setOpaque(false);

        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("SegoeUI", Font.BOLD, 16));
        exitButton.setBackground(Color.RED);
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.setPreferredSize(new Dimension(100, 40));
        exitButton.addActionListener(e -> System.exit(0));
        rightPanel.add(exitButton);

// Tambahkan ke bottomPanel
        bottomPanel.add(leftPanel, BorderLayout.WEST);
        bottomPanel.add(rightPanel, BorderLayout.EAST);

// Tambahkan bottomPanel ke layout utama
        add(bottomPanel, BorderLayout.SOUTH);

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
        String playerX = JOptionPane.showInputDialog(frame, "Enter name for Player X:", "Player X", JOptionPane.QUESTION_MESSAGE);
        if (playerX == null || playerX.trim().isEmpty()) playerX = "Player X";

        String playerO = JOptionPane.showInputDialog(frame, "Enter name for Player O:", "Player O", JOptionPane.QUESTION_MESSAGE);
        if (playerO == null || playerO.trim().isEmpty()) playerO = "Player O";

        frame.setContentPane(new GameMain(playerX, playerO));
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