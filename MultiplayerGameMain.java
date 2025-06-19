import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;
import javax.swing.Timer;

public class MultiplayerGameMain extends JPanel {
    private static final long serialVersionUID = 1L;

    public static final String TITLE = "Tic Tac Toe";
    public static final Color COLOR_BG = Color.BLUE;
    public static final Color COLOR_BG_STATUS = new Color(216, 216, 216);
    public static final Color COLOR_CROSS = new Color(239, 105, 80);
    public static final Color COLOR_NOUGHT = new Color(64, 154, 225);
    public static final Font FONT_STATUS = new Font("Segoe UI", Font.BOLD, 14);

    private Board board;
    private JLabel statusBar;
    private JLabel scoreLabel;
    private DBManager dbManager;
    private MultiplayerGameContext gameContext;
    private int lastSyncedMoveNumber = 0;
    private String localPlayer;

    public MultiplayerGameMain() {
        this.dbManager = new DBManager();

        int gameId;
        if (dbManager.isGamesTableEmpty()) {
            gameId = dbManager.createNewGame("Player X", "Player O");
            System.out.println("Created new game with ID: " + gameId);
            localPlayer = "Player X";
        } else {
            gameId = this.dbManager.getLatestGameId();
            localPlayer = "Player O";
        }
        this.gameContext = new MultiplayerGameContext(gameId);

        super.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Sync dengan DB
                syncBoardFromDB();

                int mouseX = e.getX();
                int mouseY = e.getY();
                int row = mouseY / Cell.SIZE;
                int col = mouseX / Cell.SIZE;

                if (row < 0 || row >= Board.ROWS || col < 0 || col >= Board.COLS) return;

                if (gameContext.getCurrentState() != State.PLAYING) {
                    newGame();
                    dbManager.clearMoves(gameContext.getGameId());
                    repaint();
                    return;
                }

                // cuma player lokal yg bisa gerak kalo itu giliranya
                String local = getLocalPlayer();
                Seed currentTurn = gameContext.getCurrentPlayer();
                boolean isLocalPlayersTurn = (currentTurn == Seed.CROSS && "Player X".equals(local)) ||
                        (currentTurn == Seed.NOUGHT && "Player O".equals(local));

                if (!isLocalPlayersTurn) {
                    statusBar.setText("Wait for your turn!");
                    return;
                }

                if (board.cells[row][col].content == Seed.NO_SEED) {
                    State newState = board.stepGame(currentTurn, row, col);
                    gameContext.setCurrentState(newState);

                    int moveNumber = lastSyncedMoveNumber + 1;
                    dbManager.insertMove(gameContext.getGameId(), currentTurn == Seed.CROSS ? 'X' : 'O', row, col, moveNumber);
                    lastSyncedMoveNumber = moveNumber;

                    if (newState == State.CROSS_WON) gameContext.incrementPlayerXScore();
                    else if (newState == State.NOUGHT_WON) gameContext.incrementPlayerOScore();

                    gameContext.switchPlayer();
                    setCurrentPlayer(gameContext.getCurrentPlayer() == Seed.CROSS ? "Player X" : "Player O");
                    updateScoreLabel();
                    repaint();
                }
            }
        });

        // UI setup
        statusBar = new JLabel();
        statusBar.setFont(FONT_STATUS);
        statusBar.setBackground(COLOR_BG_STATUS);
        statusBar.setOpaque(true);
        statusBar.setPreferredSize(new Dimension(300, 30));
        statusBar.setHorizontalAlignment(JLabel.LEFT);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));

        scoreLabel = new JLabel();
        scoreLabel.setFont(FONT_STATUS);
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(COLOR_BG);
        topPanel.add(scoreLabel, BorderLayout.CENTER);

        super.setLayout(new BorderLayout());
        super.add(topPanel, BorderLayout.PAGE_START);
        super.add(statusBar, BorderLayout.PAGE_END);
        super.setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + 30));
        super.setBorder(BorderFactory.createLineBorder(COLOR_BG_STATUS, 2, false));

        initGame();
        newGame();
        updateScoreLabel();

        // sync dengan DB per 200ms
        new Timer(200, e -> syncBoardFromDB()).start();
    }

    private void updateScoreLabel() {
        scoreLabel.setText("Player X: " + gameContext.getPlayerXScore() + "   |   Player O: " + gameContext.getPlayerOScore());
    }

    public void initGame() {
        board = new Board();

        // sync dengan DB pas inisiasi
        List<Move> moves = dbManager.getMoves(gameContext.getGameId());
        lastSyncedMoveNumber = moves.size();
        for (Move move : moves) {
            Seed seed = (move.player == 'X') ? Seed.CROSS : Seed.NOUGHT;
            board.cells[move.row][move.col].content = seed;
        }
    }

    public void newGame() {
        board.newGame();
        gameContext.resetGameState();
        setCurrentPlayer("Player X");
        lastSyncedMoveNumber = 0;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(COLOR_BG);
        board.paint(g);

        switch (gameContext.getCurrentState()) {
            case PLAYING:
                statusBar.setForeground(Color.BLACK);
                if ((gameContext.getCurrentPlayer() == Seed.CROSS && localPlayer == "Player X") ||
                        (gameContext.getCurrentPlayer() == Seed.NOUGHT && localPlayer == "Player O")) {
                    statusBar.setText("Your Turn");
                } else {
                    statusBar.setText("Opponent's Turn");
                }
                break;

            case DRAW:
                statusBar.setForeground(Color.RED);
                statusBar.setText("It's a Draw! Click to play again.");
                SoundEffect.EXPLODE.play();
                break;

            case CROSS_WON:
                statusBar.setForeground(Color.RED);
                if (localPlayer == "Player X") {
                    statusBar.setText("You Won! Click to play again.");
                } else {
                    statusBar.setText("You Lost. Click to play again.");
                }
                SoundEffect.EXPLODE.play();
                break;

            case NOUGHT_WON:
                statusBar.setForeground(Color.RED);
                if (localPlayer == "Player O") {
                    statusBar.setText("You Won! Click to play again.");
                } else {
                    statusBar.setText("You Lost. Click to play again.");
                }
                SoundEffect.EXPLODE.play();
                break;
        }

    }

    private void syncBoardFromDB() {
        List<Move> moves = dbManager.getMoves(gameContext.getGameId());

        if (moves.isEmpty() && lastSyncedMoveNumber > 0) {
            System.out.println("Detected game reset from DB. Clearing local board.");
            board.newGame();
            gameContext.resetGameState();
            setCurrentPlayer("Player X");
            lastSyncedMoveNumber = 0;
        }

        for (Move move : moves) {
            if (move.moveNumber <= lastSyncedMoveNumber) continue;

            Seed seed = (move.player == 'X') ? Seed.CROSS : Seed.NOUGHT;
            if (board.cells[move.row][move.col].content == Seed.NO_SEED) {
                board.cells[move.row][move.col].content = seed;
                State newState = board.checkGameState(seed, move.row, move.col);
                gameContext.setCurrentState(newState);
                gameContext.setCurrentPlayer((seed == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS);
                lastSyncedMoveNumber = move.moveNumber;

                if (newState == State.CROSS_WON) gameContext.incrementPlayerXScore();
                else if (newState == State.NOUGHT_WON) gameContext.incrementPlayerOScore();

                updateScoreLabel();
            }
        }

        repaint();
    }

    public void deleteGame(){
        this.dbManager.deleteGame(this.gameContext.getGameId());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(MultiplayerGameMain.TITLE);
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
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private String getLocalPlayer() {
        return localPlayer;
    }

    private void setCurrentPlayer(String player) {
        gameContext.setCurrentPlayer("Player X".equals(player) ? Seed.CROSS : Seed.NOUGHT);
    }
}
