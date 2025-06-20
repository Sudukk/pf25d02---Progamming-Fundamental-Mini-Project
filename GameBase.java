import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public abstract class GameBase extends JPanel {
    protected Board board;
    protected State currentState;
    protected Seed currentPlayer;

    protected JLabel statusBar;
    protected JLabel scoreLabel;

    protected String playerXName;
    protected String playerOName;
    protected int playerXScore = 0;
    protected int playerOScore = 0;

    protected BoardPanel boardPanel;

    public GameBase(String playerXName, String playerOName) {
        this.playerXName = playerXName;
        this.playerOName = playerOName;

        initGame();
        setupUI();
        newGame();
        updateScoreLabel();
        updateStatusBar();
    }

    protected void initGame() {
        this.board = new Board();
    }

    protected void newGame() {
        for (int row = 0; row < Board.ROWS; ++row)
            for (int col = 0; col < Board.COLS; ++col)
                board.cells[row][col].content = Seed.NO_SEED;

        currentPlayer = Seed.CROSS;
        currentState = State.PLAYING;
    }

    protected void setupUI() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + 70));
        setBackground(GameConstants.COLOR_BG);
        setBorder(BorderFactory.createLineBorder(new Color(100, 100, 130), 3));

        // bikin score label
        scoreLabel = new JLabel();
        scoreLabel.setFont(GameConstants.FONT_SCORE);
        scoreLabel.setForeground(new Color(240, 240, 255));
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);


        JButton backButton = new JButton("Back");
        backButton.setFocusPainted(false);
        backButton.setFont(GameConstants.FONT_STATUS);
        backButton.setMargin(new Insets(2, 10, 2, 10));
        backButton.setBackground(new Color(230, 230, 230));
        backButton.addActionListener(e -> {

            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            topFrame.getContentPane().removeAll();
            topFrame.getContentPane().add(new StartMenu(topFrame));
            topFrame.setSize(720,800);
            topFrame.revalidate();
            topFrame.repaint();
        });

        JPanel topPanel = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
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
        statusPanel.add(backButton, BorderLayout.EAST);

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

    protected void updateScoreLabel() {
        scoreLabel.setText(playerXName + ": " + playerXScore + "   |   " + playerOName + ": " + playerOScore);
    }

    protected void updateStatusBar() {
        if (statusBar == null) return;
        switch (currentState) {
            case PLAYING -> statusBar.setText((currentPlayer == Seed.CROSS ? playerXName : playerOName) + "'s Turn");
            case DRAW -> statusBar.setText("It's a Draw! Click anywhere to play again.");
            case CROSS_WON -> statusBar.setText(playerXName + " Won! Click anywhere to play again.");
            case NOUGHT_WON -> statusBar.setText(playerOName + " Won! Click anywhere to play again.");
        }
    }

    protected abstract void handleClick(int x, int y);
}
