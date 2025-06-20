import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.List;

public class GameBotMain extends JPanel {
    private static final long serialVersionUID = 1L;

    public static final String TITLE = "Tic Tac Toe vs AI";
    public static final Color COLOR_BG = Color.BLUE;
    public static final Color COLOR_BG_STATUS = new Color(216, 216, 216);
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

    private boolean vsBot = true;

    public GameBotMain(String playerXName) {
        this.playerXName = playerXName;
        this.playerOName = playerOName;
        this.playerXScore = 0;
        this.playerOScore = 0;

        addMouseListener(new MouseAdapter() {
            @Override
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
                        repaint();

                        if (vsBot && currentPlayer == Seed.NOUGHT && currentState == State.PLAYING) {
                            makeAIMove();
                        }
                    }
                } else {
                    newGame();
                    repaint();
                }
            }
        });

        statusBar = new JLabel();
        statusBar.setFont(FONT_STATUS);
        statusBar.setBackground(COLOR_BG_STATUS);
        statusBar.setOpaque(true);
        statusBar.setPreferredSize(new Dimension(300, 30));
        statusBar.setHorizontalAlignment(JLabel.LEFT);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));

        setLayout(new BorderLayout());
        add(statusBar, BorderLayout.PAGE_END);
        setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + 30));
        setBorder(BorderFactory.createLineBorder(COLOR_BG_STATUS, 2, false));

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

    private void updateScoreLabel() {
        scoreLabel.setText(playerXName + ": " + playerXScore + "   |   " + playerOName + ": " + playerOScore);
    }

    public void initGame() {
        board = new Board();
    }

    public void newGame() {
        board.newGame();
        currentPlayer = Seed.CROSS;
        currentState = State.PLAYING;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(COLOR_BG);
        board.paint(g);

        if (currentState == State.PLAYING) {
            statusBar.setForeground(Color.BLACK);
            statusBar.setText((currentPlayer == Seed.CROSS) ? "Your Turn (X)" : "Bot's Turn (O)");
        } else if (currentState == State.DRAW) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("Draw! Click to restart.");
        } else if (currentState == State.CROSS_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText(playerXName + " Wins! Click to restart.");
            playerXScore++;
            updateScoreLabel();
        } else if (currentState == State.NOUGHT_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText(playerOName + " Wins! Click to restart.");
            playerOScore++;
            updateScoreLabel();
        }
    }

    private void makeAIMove() {
        try {
            Thread.sleep(400); // Delay AI move
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        List<int[]> emptyCells = new ArrayList<>();
        for (int row = 0; row < Board.ROWS; row++) {
            for (int col = 0; col < Board.COLS; col++) {
                if (board.cells[row][col].content == Seed.NO_SEED) {
                    emptyCells.add(new int[]{row, col});
                }
            }
        }

        if (!emptyCells.isEmpty()) {
            int[] move = emptyCells.get(new Random().nextInt(emptyCells.size()));
            int row = move[0];
            int col = move[1];
            currentState = board.stepGame(currentPlayer, row, col);
            currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
            repaint();
        }
    }
}
