import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;
import javax.swing.Timer;

public class MultiplayerGameMain extends JPanel {
    private static final long serialVersionUID = 1L;

    public static final String TITLE = "Tic Tac Toe";
    public static final Color COLOR_BG = new Color(25, 25, 45);
    public static final Color COLOR_BG_STATUS = new Color(245, 245, 250);
    public static final Color COLOR_CROSS = new Color(255, 95, 85);
    public static final Color COLOR_NOUGHT = new Color(85, 170, 255);
    public static final Font FONT_STATUS = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SCORE = new Font("Segoe UI Semibold", Font.BOLD, 16);

    private Board board;
    private JLabel statusBar;
    private JLabel scoreLabel;
    private DBManager dbManager;
    private MultiplayerGameContext gameContext;
    private int lastSyncedMoveNumber = 0;
    private String localPlayer;
    private BoardPanel boardPanel;
    private Timer syncTimer;
    private String playerName;
    private boolean isHost;
    private String oppName;

    public MultiplayerGameMain(String playerName) {
        this.dbManager = new DBManager();
        this.oppName = "";

        int gameId;

        if (dbManager.isGamesTableEmpty()) {
            this.localPlayer = "Player X";
            this.isHost = true;
            this.playerName = playerName;
            gameId = dbManager.createNewGame(this.playerName, this.isHost);
        } else {
            gameId = this.dbManager.getLatestGameId();
            this.localPlayer = "Player O";
            this.isHost = false;
            this.playerName = playerName;
            dbManager.insertOppName(gameId, this.playerName);
        }
        this.gameContext = new MultiplayerGameContext(gameId);

        initGame();
        setupUI();
        newGame();
        updateScoreLabel();

        this.syncTimer = new Timer(100, e -> syncBoardFromDB());
        this.syncTimer.start();

    }

    private void setupUI() {
        setLayout(new BorderLayout(0,0));

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

        //tambah tombol back
        JButton exitButton = new JButton("Exit");
        exitButton.setFocusPainted(false);
        exitButton.setFont(FONT_STATUS);
        exitButton.setMargin(new Insets(2, 10, 2, 10));
        exitButton.setBackground(new Color(230, 230, 230));
        exitButton.addActionListener(e -> {
            deleteGame();

            if (this.syncTimer != null) {
                this.syncTimer.stop();
            }

            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            topFrame.getContentPane().removeAll();
            topFrame.getContentPane().add(new StartMenu(topFrame));
            topFrame.setSize(720,800);
            topFrame.revalidate();
            topFrame.repaint();
        });

        statusPanel.add(statusBar, BorderLayout.CENTER);
        statusPanel.add(exitButton, BorderLayout.EAST);

        boardPanel = new BoardPanel(this.board);
        boardPanel.setBackground(COLOR_BG);
        boardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();
                int row = mouseY / Cell.SIZE;
                int col = mouseX / Cell.SIZE;

                //kalau diluar batas tidak melakukan apa-apa
                if (row < 0 || row >= Board.ROWS || col < 0 || col >= Board.COLS) return;

                //hanya bisa klik kalo giliran kita
                Seed currentTurn = gameContext.getCurrentPlayer();
                if ((isHost && currentTurn != Seed.CROSS) || (!isHost && currentTurn != Seed.NOUGHT)) {
                    return;
                }

                // kalau statenya tidak playing maka reset semua
                if (gameContext.getCurrentState() != State.PLAYING) {
                    newGame();
                    dbManager.clearMoves(gameContext.getGameId());
                    boardPanel.repaint();
                    return;
                }

                //kalau board bagian row dan col kosong, maka player dapat mengisinya
                if (board.cells[row][col].content == Seed.NO_SEED) {
                    State newState = board.stepGame(gameContext.getCurrentPlayer(), row, col);
                    gameContext.setCurrentState(newState);

                    int moveNumber = ++lastSyncedMoveNumber;
                    dbManager.insertMove(gameContext.getGameId(),
                            gameContext.getCurrentPlayer() == Seed.CROSS ? 'X' : 'O',
                            row, col, moveNumber);

                    if (newState == State.CROSS_WON) gameContext.incrementPlayerXScore();
                    else if (newState == State.NOUGHT_WON) gameContext.incrementPlayerOScore();

                    gameContext.switchPlayer();
                    setCurrentPlayer(gameContext.getCurrentPlayer() == Seed.CROSS ? "Player X" : "Player O");
                    updateScoreLabel();
                    boardPanel.repaint();
                }
            }
        });

        add(topPanel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);

    }

    private void updateScoreLabel() {
        String label;
        if ("Player X".equals(localPlayer)) {
            label = this.playerName + ": " + gameContext.getPlayerXScore() +
                    "   |   " + this.oppName + ": " + gameContext.getPlayerOScore();
        } else {
            label = this.playerName + ": " + gameContext.getPlayerOScore() +
                    "   |   " + this.oppName + ": " + gameContext.getPlayerXScore();
        }
        scoreLabel.setText(label);
    }

    public void initGame() {
        board = new Board();
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
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        board.paint(g);

        switch (gameContext.getCurrentState()) {
            case PLAYING:
                statusBar.setForeground(Color.DARK_GRAY);
                if ((gameContext.getCurrentPlayer() == Seed.CROSS && "Player X".equals(localPlayer)) ||
                        (gameContext.getCurrentPlayer() == Seed.NOUGHT && "Player O".equals(localPlayer))) {
                    statusBar.setText("Your Turn");
                } else {
                    statusBar.setText("Opponent's Turn");
                }
                break;

            case DRAW:
                statusBar.setForeground(new Color(200, 50, 50));
                statusBar.setText("It's a Draw! Click to play again.");
                SoundEffect.EXPLODE.play();
                break;

            case CROSS_WON:
                statusBar.setForeground(new Color(200, 50, 50));
                statusBar.setText((localPlayer.equals("Player X") ? "You Won!" : "You Lost.") + " Click to play again.");
                SoundEffect.EXPLODE.play();
                break;

            case NOUGHT_WON:
                statusBar.setForeground(new Color(200, 50, 50));
                statusBar.setText((localPlayer.equals("Player O") ? "You Won!" : "You Lost.") + " Click to play again.");
                SoundEffect.EXPLODE.play();
                break;
        }
    }

    private void syncBoardFromDB() {
        List<Move> moves = dbManager.getMoves(gameContext.getGameId());

        // klo nama lawan masih kosong (loading/baru join) ambil nama lawan sekali saja
        if (this.oppName == null || this.oppName.isEmpty()) {
            this.oppName = dbManager.getOppName(this.isHost);
        }

        // If board is empty but previously synced moves exist, reset the board
        if (moves.isEmpty() && lastSyncedMoveNumber > 0) {
            board.newGame();
            gameContext.resetGameState();
            setCurrentPlayer("Player X");
            lastSyncedMoveNumber = 0;
        }

        for (int i =0;i<moves.size();i++) {
            Move move = moves.get(i);
            if (move.moveNumber <= lastSyncedMoveNumber) continue;

            if (board.cells[move.row][move.col].content != Seed.NO_SEED) continue;

            Seed seed = (move.player == 'X') ? Seed.CROSS : Seed.NOUGHT;
            board.cells[move.row][move.col].content = seed;

            State newState = board.checkGameState(seed, move.row, move.col);

            gameContext.setCurrentState(newState);
            gameContext.setCurrentPlayer(seed == Seed.CROSS ? Seed.NOUGHT : Seed.CROSS);

            lastSyncedMoveNumber = move.moveNumber;

            switch (newState) {
                case CROSS_WON -> gameContext.incrementPlayerXScore();
                case NOUGHT_WON -> gameContext.incrementPlayerOScore();
            }

            updateScoreLabel();
        }
        repaint();
    }


    public void deleteGame() {
        dbManager.deleteGame(gameContext.getGameId());
    }

    private void setCurrentPlayer(String player) {
        gameContext.setCurrentPlayer("Player X".equals(player) ? Seed.CROSS : Seed.NOUGHT);
    }
}

