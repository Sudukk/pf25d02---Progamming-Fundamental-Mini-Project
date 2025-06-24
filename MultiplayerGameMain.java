import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

public class MultiplayerGameMain extends GameBase {
    private Board board;
    private JLabel statusBar;
    private JLabel scoreLabel;
    private DBManager dbManager;

    private int gameId;
    private int playerXScore = 0;
    private int playerOScore = 0;
    private Seed currentPlayer = Seed.CROSS;
    private State currentState = State.PLAYING;

    private String localPlayer;
    private boolean isHost;
    private String playerName;
    private String opponentName = "";

    private int lastSyncedMoveNumber = 0;
    private Timer syncTimer;

    public MultiplayerGameMain(String playerName) {
        super(null, null);
        this.playerName = playerName;
        this.dbManager = new DBManager();

        if (dbManager.isGamesTableEmpty()) {
            this.isHost = true;
            this.localPlayer = "Player X";
            this.gameId = dbManager.createNewGame(playerName, true);
        } else {
            this.isHost = false;
            this.localPlayer = "Player O";
            this.gameId = dbManager.getLatestGameId();
            dbManager.insertOppName(gameId, playerName);
        }

        initGame();
        setupUI();
        newGame();
        updateScoreLabel();

        syncTimer = new Timer(100, e->syncBoardFromDB());
        syncTimer.start();
    }

    @Override
    public void setupUI() {
        setLayout(new BorderLayout());

        setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + 70));
        setBackground(GameConstants.COLOR_BG);
        setBorder(BorderFactory.createLineBorder(new Color(100, 100, 130), 3));

        scoreLabel = new JLabel();
        scoreLabel.setFont(GameConstants.FONT_SCORE);
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel topPanel = new JPanel(new BorderLayout()) {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, new Color(40, 40, 70), getWidth(), getHeight(), new Color(30, 30, 50)));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        topPanel.setPreferredSize(new Dimension(Board.CANVAS_WIDTH, 45));
        topPanel.add(scoreLabel, BorderLayout.CENTER);

        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(GameConstants.COLOR_BG_STATUS);
        statusPanel.setPreferredSize(new Dimension(Board.CANVAS_WIDTH, 30));

        statusBar = new JLabel();
        statusBar.setFont(GameConstants.FONT_STATUS);
        statusBar.setForeground(Color.DARK_GRAY);
        statusBar.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));

        JButton exitButton = new JButton("Exit");
        exitButton.setFont(GameConstants.FONT_STATUS);
        exitButton.setBackground(GameConstants.COLOR_BG);
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        exitButton.addActionListener(e -> {
            deleteGame();
            if (syncTimer != null) syncTimer.stop();
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(MultiplayerGameMain.this);
            topFrame.setContentPane(new StartMenu(topFrame));
            topFrame.setSize(720, 800);
            topFrame.revalidate();
            topFrame.repaint();
        });

        statusPanel.add(statusBar, BorderLayout.CENTER);
        statusPanel.add(exitButton, BorderLayout.EAST);

        boardPanel = new BoardPanel(this.board);
        boardPanel.setBackground(GameConstants.COLOR_BG);
        boardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick(e.getX(), e.getY());
            }
        });

        add(topPanel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);
    }

    //update skornya sesuai nama playernya
    @Override
    public void updateScoreLabel() {
        String label;
        if (localPlayer.equals("Player X")) {
            label = playerName + ": " + playerXScore + "   |   " + opponentName + ": " + playerOScore;
        } else {
            label = playerName + ": " + playerOScore + "   |   " + opponentName + ": " + playerXScore;
        }
        scoreLabel.setText(label);
    }

    @Override
    public void initGame() {
        board = new Board();
        ArrayList<Move> moves = dbManager.getMoves(gameId);
        lastSyncedMoveNumber = moves.size();

        //memasukkan seed ke boardnya berdasarkan moves yg ada di DB
        for (int i = 0; i < moves.size(); i++) {
            Move move = moves.get(i);
            Seed seed = (move.player == 'X') ? Seed.CROSS : Seed.NOUGHT;
            board.cells[move.row][move.col].content = seed;
        }
    }

    @Override
    public void newGame() {
        board.newGame();
        currentPlayer = Seed.CROSS;
        currentState = State.PLAYING;
        lastSyncedMoveNumber = 0;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        board.paint(g);

        if (currentState == State.PLAYING) {
            statusBar.setForeground(Color.DARK_GRAY);
            boolean isLocalTurn = (currentPlayer == Seed.CROSS && localPlayer.equals("Player X")) || (currentPlayer == Seed.NOUGHT && localPlayer.equals("Player O"));
            statusBar.setText(isLocalTurn ? "Your Turn" : "Opponent's Turn");
        } else if (currentState == State.DRAW) {
            statusBar.setForeground(new Color(200, 50, 50));
            statusBar.setText("It's a Draw! Click to play again.");
            SoundEffect.EXPLODE.play();
        } else if (currentState == State.CROSS_WON || currentState == State.NOUGHT_WON) {
            boolean won = (currentState == State.CROSS_WON && localPlayer.equals("Player X")) || (currentState == State.NOUGHT_WON && localPlayer.equals("Player O"));
            statusBar.setForeground(new Color(200, 50, 50));
            statusBar.setText((won ? "You Won!" : "You Lost.") + " Click to play again.");
            SoundEffect.EXPLODE.play();
        }
    }

    //method untuk sinkronisasi dengan db
    private void syncBoardFromDB() {
        ArrayList<Move> moves = dbManager.getMoves(gameId);

        //ambil nama klo nama opponent masih kosong
        if (opponentName == null || opponentName.isEmpty()) {
            opponentName = dbManager.getOppName(isHost);
        }

        //klo tidak ada langkah tapi lastSyncedMoveNumber masih > 0 akan direset
        if (moves.isEmpty() && lastSyncedMoveNumber > 0) {
            newGame();
        }

        //looping setiap langkah yang ada di db
        for (int i = 0; i < moves.size(); i++) {
            Move move = moves.get(i);

            //skip langkah kalo sudah diproses dan juga cek langkahnya valid atau tidak
            if (move.moveNumber <= lastSyncedMoveNumber) continue;
            if (board.cells[move.row][move.col].content != Seed.NO_SEED) continue;

            //masukkan seed yg sesuai ke board
            Seed seed = (move.player == 'X') ? Seed.CROSS : Seed.NOUGHT;
            board.cells[move.row][move.col].content = seed;
            currentState = board.checkGameState(seed, move.row, move.col);
            currentPlayer = (seed == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
            lastSyncedMoveNumber = move.moveNumber;

            //tambah skor kalau playernya menang
            if (currentState == State.CROSS_WON) playerXScore++;
            if (currentState == State.NOUGHT_WON) playerOScore++;
            updateScoreLabel();
        }
        repaint();
    }

    @Override
    public void handleClick(int x, int y) {
        int row = y / Cell.SIZE;
        int col = x / Cell.SIZE;

        //cek apakah posisi mouse diluar board atau tidak
        if (row < 0 || row >= Board.ROWS || col < 0 || col >= Board.COLS) return;
        //cek apakah ini giliran dia atau tidak
        if ((isHost && currentPlayer != Seed.CROSS) || (!isHost && currentPlayer != Seed.NOUGHT)) return;

        //kalau sedang tidak bermain reset semuanya
        if (currentState != State.PLAYING) {
            newGame();
            dbManager.clearMoves(gameId);
            boardPanel.repaint();
            return;
        }

        //masukkan move ke database kalau boardnya itu kosong
        if (board.cells[row][col].content == Seed.NO_SEED) {
            SoundEffect.MOUSE_CLICK.play(5);
            State newState = board.stepGame(currentPlayer, row, col); //memainkan movenya dan mengembalikan state yg baru
            currentState = newState;

            //update move numbernya dan simpan movenya ke db
            int moveNumber = ++lastSyncedMoveNumber;
            dbManager.insertMove(gameId, (currentPlayer == Seed.CROSS) ? 'X' : 'O', row, col, moveNumber);

            if (newState == State.CROSS_WON) playerXScore++;
            else if (newState == State.NOUGHT_WON) playerOScore++;

            //switch playernya
            currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
            updateScoreLabel();
            boardPanel.repaint();
        }
    }

    public void deleteGame() {
        dbManager.deleteGame(gameId);
    }
}
