import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Tic-Tac-Toe: Two-player Graphic version with better OO design.
 * The Board and Cell classes are separated in their own classes.
 */
public class GameMain extends JPanel {
    private static final long serialVersionUID = 1L;

    public static final String TITLE = "Tic Tac Toe";
    public static final Color COLOR_BG = new Color(25, 25, 45);
    public static final Color COLOR_BG_STATUS = new Color(245, 245, 250);
    public static final Color COLOR_CROSS = new Color(239, 105, 80);
    public static final Color COLOR_NOUGHT = new Color(64, 154, 225);
    public static final Font FONT_STATUS = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SCORE = new Font("Segoe UI Semibold", Font.BOLD, 16);

    private Board board;
    private State currentState;
    private Seed currentPlayer;
    private JLabel statusBar;
    private JLabel scoreLabel;

    private String playerXName;
    private String playerOName;
    private int playerXScore = 0;
    private int playerOScore = 0;

    private BoardPanel boardPanel;

    public GameMain(String playerXName, String playerOName) {
        this.playerXName = playerXName;
        this.playerOName = playerOName;


        initGame();
        setupUI();
        newGame();
        updateScoreLabel();
        updateStatusBar();
    }

    private void setupUI() {
        setLayout(new BorderLayout(0, 0));

        setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + 70));
        setBackground(COLOR_BG);
        setBorder(BorderFactory.createLineBorder(new Color(100, 100, 130), 3));

        // Top Score Panel
        scoreLabel = new JLabel();
        scoreLabel.setFont(FONT_SCORE);
        scoreLabel.setForeground(new Color(240, 240, 255));
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scoreLabel.setOpaque(false);

        JPanel topPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, new Color(40, 40, 70), getWidth(), getHeight(), new Color(30, 30, 50)));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        topPanel.setPreferredSize(new Dimension(Board.CANVAS_WIDTH, 45));
        topPanel.add(scoreLabel, BorderLayout.CENTER);

        // Status Bar
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(COLOR_BG_STATUS);
        statusPanel.setPreferredSize(new Dimension(Board.CANVAS_WIDTH, 30));

        statusBar = new JLabel();
        statusBar.setFont(FONT_STATUS);
        statusBar.setForeground(Color.DARK_GRAY);
        statusBar.setOpaque(false);
        statusBar.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        statusBar.setHorizontalAlignment(SwingConstants.LEFT);

        statusPanel.add(statusBar, BorderLayout.CENTER);

        // Game board
        boardPanel = new BoardPanel(this.board);
        boardPanel.setBackground(COLOR_BG);
        boardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();
                int row = mouseY / Cell.SIZE;
                int col = mouseX / Cell.SIZE;

                if (row < 0 || row >= Board.ROWS || col < 0 || col >= Board.COLS) return;

                if (currentState != State.PLAYING) {
                    newGame();
                    updateStatusBar();
                    boardPanel.repaint();
                    return;
                }

                if (board.cells[row][col].content == Seed.NO_SEED) {
                    currentState = board.stepGame(currentPlayer, row, col);
                    if (currentState == State.CROSS_WON) playerXScore++;
                    else if (currentState == State.NOUGHT_WON) playerOScore++;

                    currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                    updateScoreLabel();
                    updateStatusBar();
                    boardPanel.repaint();
                }
            }
        });

        add(topPanel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);
    }

    private void updateScoreLabel() {
        scoreLabel.setText(playerXName + ": " + playerXScore + "   |   " + playerOName + ": " + playerOScore);
    }

    private void updateStatusBar() {
        if (statusBar == null) return;

        switch (currentState) {
            case PLAYING:
                statusBar.setForeground(Color.DARK_GRAY);
                statusBar.setText((currentPlayer == Seed.CROSS ? playerXName : playerOName) + "'s Turn");
                break;
            case DRAW:
                statusBar.setForeground(new Color(200, 50, 50));
                statusBar.setText("It's a Draw! Click anywhere to play again.");
                break;
            case CROSS_WON:
                statusBar.setForeground(new Color(200, 50, 50));
                statusBar.setText(playerXName + " Won! Click anywhere to play again.");
                break;
            case NOUGHT_WON:
                statusBar.setForeground(new Color(200, 50, 50));
                statusBar.setText(playerOName + " Won! Click anywhere to play again.");
                break;
        }
    }

    public void initGame() {
        this.board = new Board();
    }

    public void newGame() {
        for (int row = 0; row < Board.ROWS; ++row) {
            for (int col = 0; col < Board.COLS; ++col) {
                this.board.cells[row][col].content = Seed.NO_SEED;
            }
        }
        currentPlayer = Seed.CROSS;
        currentState = State.PLAYING;
    }
//
//    private class BoardPanel extends JPanel {
//        @Override
//        protected void paintComponent(Graphics g) {
//            super.paintComponent(g);
//            board.paint(g);
//        }
//
//        @Override
//        public Dimension getPreferredSize() {
//            return new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT);
//        }
//    }
}




