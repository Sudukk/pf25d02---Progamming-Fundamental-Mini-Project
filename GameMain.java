import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Tic-Tac-Toe: Two-player Graphic version with better OO design.
 */
public class GameMain extends JPanel {
    private static final long serialVersionUID = 1L;

    public static final String TITLE = "Tic Tac Toe";
    public static final Color COLOR_BG = Color.orange;
    public static final Color COLOR_BG_STATUS = new Color(0, 0, 0);
    public static final Color COLOR_CROSS = new Color(239, 105, 80);
    public static final Color COLOR_NOUGHT = new Color(64, 154, 225);

    public static final Font FONT_STATUS = new Font("Segoe UI", Font.BOLD, 14);

    private Board board;
    private State currentState;
    private Seed currentPlayer;
    private JLabel statusBar;

    private String playerXName;
    private String playerOName;
    private int playerXScore;
    private int playerOScore;
    private JLabel scoreLabel;

    /** Constructor to setup the UI and game components */
    public GameMain(String playerXName, String playerOName) {
        this.playerXName = playerXName;
        this.playerOName = playerOName;
        this.playerXScore = 0;
        this.playerOScore = 0;

        super.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();

                int row = mouseY / Cell.SIZE;
                int col = mouseX / Cell.SIZE;

                if (currentState == State.PLAYING) {
                    if (row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS
                            && board.cells[row][col].content == Seed.NO_SEED) {
                        currentState = board.stepGame(currentPlayer, row, col);
                        currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                    }
                } else {
                    newGame();  // restart gamenya
                }

                repaint();
            }
        });

        statusBar = new JLabel();
        statusBar.setFont(FONT_STATUS);
        statusBar.setBackground(COLOR_BG_STATUS);
        statusBar.setOpaque(true);
        statusBar.setPreferredSize(new Dimension(300, 30));
        statusBar.setHorizontalAlignment(JLabel.CENTER);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));

        setLayout(new BorderLayout());
        add(statusBar, BorderLayout.PAGE_END);
        setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + 60));
        setBorder(BorderFactory.createLineBorder(COLOR_BG_STATUS, 2, false));

        //panel atas menampilkan skor
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(COLOR_BG);

        scoreLabel = new JLabel();
        scoreLabel.setFont(FONT_STATUS);
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        updateScoreLabel();
        topPanel.add(scoreLabel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.PAGE_START);

        initGame();
        newGame();
    }

    public void initGame() {
        board = new Board();
    }

    public void newGame() {
        for (int row = 0; row < Board.ROWS; ++row) {
            for (int col = 0; col < Board.COLS; ++col) {
                board.cells[row][col].content = Seed.NO_SEED;
            }
        }
        currentPlayer = Seed.CROSS;
        currentState = State.PLAYING;
    }

    private void updateScoreLabel() {
        scoreLabel.setText(playerXName + ": " + playerXScore + "   |   " + playerOName + ": " + playerOScore);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(COLOR_BG);

        board.paint(g);

        if (currentState == State.PLAYING) {
            statusBar.setForeground(Color.WHITE);
            if (currentPlayer == Seed.CROSS) {
                statusBar.setText(playerXName + "'s Turn (X)");
            } else {
                statusBar.setText(playerOName + "'s Turn (O)");
            }
        } else if (currentState == State.DRAW) {
            statusBar.setForeground(Color.YELLOW);
            statusBar.setText("It's a Draw! Click to play again.");
        } else if (currentState == State.CROSS_WON) {
            statusBar.setForeground(Color.GREEN);
            statusBar.setText("'" + playerXName + "' Won! Click to play again.");
            if (currentState != State.PLAYING) {
                playerXScore++;
                updateScoreLabel();
            }
        } else if (currentState == State.NOUGHT_WON) {
            statusBar.setForeground(Color.GREEN);
            statusBar.setText("'" + playerOName + "' Won! Click to play again.");
            if (currentState != State.PLAYING) {
                playerOScore++;
                updateScoreLabel();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                String playerX = JOptionPane.showInputDialog(null, "Enter Player X's Name:", "Player X", JOptionPane.QUESTION_MESSAGE);
                if (playerX == null || playerX.trim().isEmpty()) {
                    playerX = "Player X";
                }

                String playerO = JOptionPane.showInputDialog(null, "Enter Player O's Name:", "Player O", JOptionPane.QUESTION_MESSAGE);
                if (playerO == null || playerO.trim().isEmpty()) {
                    playerO = "Player O";
                }

                JFrame frame = new JFrame(TITLE);
                frame.setContentPane(new GameMain(playerX, playerO));
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}
