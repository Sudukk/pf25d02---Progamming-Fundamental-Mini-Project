import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameBase extends JPanel {
    public Board board;
    public State currentState;
    public Seed currentPlayer;

    public JLabel statusBar;
    public JLabel scoreLabel;

    public String playerXName;
    public String playerOName;
    public int playerXScore = 0;
    public int playerOScore = 0;

    public BoardPanel boardPanel;
    private JPanel pauseOverlay;

    public GameBase(String playerXName, String playerOName) {
        this.playerXName = playerXName;
        this.playerOName = playerOName;
    }

    /** Initialize the game (run once) */
    public void initGame() {
        this.board = new Board();
    }

    /** Reset the game-board contents and the current-state, ready for new game */
    public void newGame() {
        for (int row = 0; row < Board.ROWS; ++row)
            for (int col = 0; col < Board.COLS; ++col)
                board.cells[row][col].content = Seed.NO_SEED;

        currentPlayer = Seed.CROSS;
        currentState = State.PLAYING;
    }

    public void setupUI() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + 70));
        setBackground(GameConstants.COLOR_BG);
        setBorder(BorderFactory.createLineBorder(new Color(100, 100, 130), 3));

        // bikin score label
        scoreLabel = new JLabel();
        scoreLabel.setFont(GameConstants.FONT_SCORE);
        scoreLabel.setForeground(new Color(240, 240, 255));
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton pauseButton = new JButton("Pause");
        pauseButton.setMargin(new Insets(2, 10, 2, 10));
        pauseButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        pauseButton.setAlignmentY(10);
        pauseButton.setFont(GameConstants.FONT_STATUS);
        pauseButton.setMaximumSize(new Dimension(240, 50));
        pauseButton.setBackground(GameConstants.COLOR_BG);
        pauseButton.setForeground(Color.WHITE);
        pauseButton.setFocusPainted(false);
        pauseButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        pauseButton.addActionListener(e -> showPauseMenu());


        JPanel topPanel = new JPanel(new BorderLayout()) {
            @Override public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, new Color(40, 40, 70), getWidth(), getHeight(), new Color(30, 30, 50)));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        topPanel.setPreferredSize(new Dimension(Board.CANVAS_WIDTH, 45));
        topPanel.add(scoreLabel, BorderLayout.CENTER);

        // bikin statusbar
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(GameConstants.COLOR_BG_STATUS);
        statusPanel.setPreferredSize(new Dimension(Board.CANVAS_WIDTH, 30));

        statusBar = new JLabel();
        statusBar.setFont(GameConstants.FONT_STATUS);
        statusBar.setForeground(Color.DARK_GRAY);
        statusBar.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        statusBar.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel.add(statusBar, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(pauseButton);
        statusPanel.add(buttonPanel, BorderLayout.EAST);


        // bikin panel board
        boardPanel = new BoardPanel(board);
        boardPanel.setBackground(GameConstants.COLOR_BG);
        boardPanel.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                handleClick(e.getX(), e.getY());
            }
        });

        add(topPanel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);
    }

    public void updateScoreLabel() {
        scoreLabel.setText(playerXName + ": " + playerXScore + "   |   " + playerOName + ": " + playerOScore);
    }

    public void updateStatusBar() {
        if (statusBar == null) return;
        switch (currentState) {
            case PLAYING -> statusBar.setText((currentPlayer == Seed.CROSS ? playerXName : playerOName) + "'s Turn");
            case DRAW -> statusBar.setText("It's a Draw! Click anywhere to play again.");
            case CROSS_WON -> statusBar.setText(playerXName + " Won! Click anywhere to play again.");
            case NOUGHT_WON -> statusBar.setText(playerOName + " Won! Click anywhere to play again.");
        }
    }

    public void handleClick(int x, int y){
        //buat nanti di override sama childrennya
    };


    public void showPauseMenu() {
        //bikin pauseOverlay
        this.pauseOverlay = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Optional: dark overlay
                g.setColor(new Color(0, 0, 0, 150));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        this.pauseOverlay.setLayout(new BoxLayout(pauseOverlay, BoxLayout.Y_AXIS));
        this.pauseOverlay.setOpaque(false);
        this.pauseOverlay.setFocusable(true);
        this.pauseOverlay.requestFocusInWindow();

        // supaya tidak bisa klik boardnya saat sedang di pause\
        this.pauseOverlay.addMouseListener(new MouseAdapter() {});
        this.pauseOverlay.addMouseMotionListener(new MouseMotionAdapter() {});
        this.pauseOverlay.addMouseWheelListener(e -> {});

        // bikin box buat tombol continue dan exit
        JPanel buttonBox = new JPanel();
        buttonBox.setOpaque(false);
        buttonBox.setLayout(new BoxLayout(buttonBox, BoxLayout.Y_AXIS));

        JButton continueButton = new JButton("Continue");
        continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        continueButton.setAlignmentY(10);
        continueButton.setFont(GameConstants.FONT_STATUS);
        continueButton.setMaximumSize(new Dimension(240, 50));
        continueButton.setBackground(GameConstants.COLOR_BG);
        continueButton.setForeground(Color.WHITE);
        continueButton.setFocusPainted(false);
        continueButton.addActionListener(e -> hidePauseMenu());

        JButton exitButton = new JButton("Exit to Menu");
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentY(10);
        exitButton.setFont(GameConstants.FONT_STATUS);
        exitButton.setMaximumSize(new Dimension(240, 50));
        exitButton.setBackground(GameConstants.COLOR_BG);
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        exitButton.addActionListener(e -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            topFrame.getContentPane().removeAll();
            topFrame.setContentPane(new StartMenu(topFrame));
            topFrame.setSize(720, 800);
            topFrame.revalidate();
            topFrame.repaint();
        });

        buttonBox.add(Box.createVerticalGlue());
        buttonBox.add(continueButton);
        buttonBox.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonBox.add(exitButton);
        buttonBox.add(Box.createVerticalGlue());

        pauseOverlay.add(Box.createVerticalGlue());
        pauseOverlay.add(buttonBox);
        pauseOverlay.add(Box.createVerticalGlue());

        setLayout(null);
        add(pauseOverlay);
        SwingUtilities.invokeLater(() -> {
            pauseOverlay.setBounds(0, 0, getWidth(), getHeight());
            setComponentZOrder(pauseOverlay, 0);
            revalidate();
            repaint();
        });
    }


    public void hidePauseMenu() {
        if (pauseOverlay != null) {
            remove(pauseOverlay);
            pauseOverlay = null;
            setLayout(new BorderLayout());
            revalidate();
            repaint();
        }
    }
}

