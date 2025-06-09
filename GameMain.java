import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Tic-Tac-Toe: Two-player Graphic version with better OO design.
 * The Board and Cell classes are separated in their own classes.
 * This version prompts for player names and includes a score counter.
 */
public class GameMain extends JPanel {
    private static final long serialVersionUID = 1L; // to prevent serializable warning

    // Define named constants for the drawing graphics
    public static final String TITLE = "Tic Tac Toe";
    public static final Color COLOR_BG = Color.orange;
    public static final Color COLOR_BG_STATUS = new Color(0, 0, 0);
    public static final Color COLOR_CROSS = new Color(239, 105, 80); // Red #EF6950
    public static final Color COLOR_NOUGHT = new Color(64, 154, 225); // Blue #409AE1

    public static final Font FONT_STATUS = new Font("Segoe UI", Font.BOLD, 14);

    // Define game objects
    private Board board; // the game board
    private State currentState; // the current state of the game
    private Seed currentPlayer; // the current player
    private JLabel statusBar; // for displaying status message
    private JButton restartButton; // button to restart the game

    // Player names and scores
    private String playerXName;
    private String playerOName;
    private int playerXScore;
    private int playerOScore;
    private JLabel scoreLabel; // Label to display the scores

    /** Constructor to setup the UI and game components */
    public GameMain(String playerXName, String playerOName) {
        this.playerXName = playerXName;
        this.playerOName = playerOName;
        this.playerXScore = 0;
        this.playerOScore = 0;

        // This JPanel fires MouseEvent
        super.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { // mouse-clicked handler
                int mouseX = e.getX();
                int mouseY = e.getY();
                // Get the row and column clicked
                int row = mouseY / Cell.SIZE;
                int col = mouseX / Cell.SIZE;

                if (currentState == State.PLAYING) {
                    if (row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS
                            && board.cells[row][col].content == Seed.NO_SEED) {
                        // Update cells[][] and return the new game state after the move
                        currentState = board.stepGame(currentPlayer, row, col);
                        // Switch player
                        currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                    }
                }
                // Refresh the drawing canvas
                repaint(); // Callback paintComponent().
            }
        });

        // Setup the status bar (JLabel) to display status message
        statusBar = new JLabel();
        statusBar.setFont(FONT_STATUS);
        statusBar.setBackground(COLOR_BG_STATUS);
        statusBar.setOpaque(true);
        statusBar.setPreferredSize(new Dimension(300, 30));
        statusBar.setHorizontalAlignment(JLabel.CENTER);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));

        super.setLayout(new BorderLayout());
        super.add(statusBar, BorderLayout.PAGE_END);
        // Adjust preferred size to account for the new top panel
        super.setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + 60));
        super.setBorder(BorderFactory.createLineBorder(COLOR_BG_STATUS, 2, false));

        // --- UI for Score and Restart Button ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(COLOR_BG);

        // Score Label Setup
        scoreLabel = new JLabel();
        scoreLabel.setFont(FONT_STATUS);
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        updateScoreLabel(); // Set initial score text
        topPanel.add(scoreLabel, BorderLayout.CENTER);

        // Initialize restartButton
        restartButton = new JButton("Play Again?");
        restartButton.setVisible(false);
        restartButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        topPanel.add(restartButton, BorderLayout.EAST); // Add button to the side

        // Add ActionListener to the restartButton
        restartButton.addActionListener(_ -> {
            newGame();
            repaint();
        });

        // Add the whole top panel to the main frame
        super.add(topPanel, BorderLayout.PAGE_START);

        // Set up Game
        initGame();
        newGame();
    }

    /** Initialize the game (run once) */
    public void initGame() {
        board = new Board(); // allocate the game-board
    }

    /** Reset the game-board contents and the current-state, ready for new game */
    public void newGame() {
        restartButton.setVisible(false);
        for (int row = 0; row < Board.ROWS; ++row) {
            for (int col = 0; col < Board.COLS; ++col) {
                board.cells[row][col].content = Seed.NO_SEED; // all cells empty
            }
        }
        currentPlayer = Seed.CROSS; // cross plays first
        currentState = State.PLAYING; // ready to play
    }

    /** Helper method to update the score label text */
    private void updateScoreLabel() {
        scoreLabel.setText(playerXName + ": " + playerXScore + "   |   " + playerOName + ": " + playerOScore);
    }

    /** Custom painting codes on this JPanel */
    @Override
    public void paintComponent(Graphics g) { // Callback via repaint()
        super.paintComponent(g);
        setBackground(COLOR_BG); // set its background color

        board.paint(g); // ask the game board to paint itself

        // Print status-bar message
        if (currentState == State.PLAYING) {
            statusBar.setForeground(Color.WHITE);
            if (currentPlayer == Seed.CROSS) {
                statusBar.setText(playerXName + "'s Turn (X)");
            } else {
                statusBar.setText(playerOName + "'s Turn (O)");
            }
        } else if (currentState == State.DRAW) {
            statusBar.setForeground(Color.YELLOW);
            statusBar.setText("It's a Draw! Click 'Play Again'.");
            restartButton.setVisible(true);
        } else if (currentState == State.CROSS_WON) {
            statusBar.setForeground(Color.GREEN);
            statusBar.setText("'" + playerXName + "' Won! Click 'Play Again'.");
            // Check if button is not visible, which means we just entered this state.
            // This prevents the score from incrementing on every repaint.
            if (!restartButton.isVisible()) {
                playerXScore++;
                updateScoreLabel();
            }
            restartButton.setVisible(true);
        } else if (currentState == State.NOUGHT_WON) {
            statusBar.setForeground(Color.GREEN);
            statusBar.setText("'" + playerOName + "' Won! Click 'Play Again'.");
            // Same check to prevent multiple score increments.
            if (!restartButton.isVisible()) {
                playerOScore++;
                updateScoreLabel();
            }
            restartButton.setVisible(true);
        }
    }

    /** The entry "main" method */
    public static void main(String[] args) {
        // Run GUI construction codes in Event-Dispatching thread for thread safety
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Prompt for player names
                String playerX = JOptionPane.showInputDialog(null, "Enter Player X's Name:", "Player X", JOptionPane.QUESTION_MESSAGE);
                if (playerX == null || playerX.trim().isEmpty()) {
                    playerX = "Player X"; // Default name if empty or cancelled
                }

                String playerO = JOptionPane.showInputDialog(null, "Enter Player O's Name:", "Player O", JOptionPane.QUESTION_MESSAGE);
                if (playerO == null || playerO.trim().isEmpty()) {
                    playerO = "Player O"; // Default name if empty or cancelled
                }

                JFrame frame = new JFrame(TITLE);
                // Pass player names to the GameMain constructor
                frame.setContentPane(new GameMain(playerX, playerO));
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setLocationRelativeTo(null); // center the application window
                frame.setVisible(true); // show it
            }
        });
    }
}
