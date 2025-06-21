public class GameBotMain extends GameBase {
    public GameBotMain(String playerXName) {
        super(playerXName, "Bot");
        initGame();
        setupUI();
        newGame();
        updateScoreLabel();
        updateStatusBar();
    }

    @Override
    public void handleClick(int x, int y) {
        int row = y / Cell.SIZE;
        int col = x / Cell.SIZE;

        if (outOfBounds(row, col)) {
            return;
        }
        if (currentState != State.PLAYING) {
            newGame();
            updateStatusBar();
            boardPanel.repaint();
            return;
        }

        if (board.cells[row][col].content == Seed.NO_SEED) {
            currentState = board.stepGame(currentPlayer, row, col);
            if (currentState == State.CROSS_WON) playerXScore++;

            currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
            SoundEffect.MOUSE_CLICK.play(5);
            updateScoreLabel(); updateStatusBar(); boardPanel.repaint();

            if (currentPlayer == Seed.NOUGHT && currentState == State.PLAYING) {
                makeBotMove();
            }
        }
    }

    private boolean outOfBounds(int row, int col) {
        return row < 0 || row >= Board.ROWS || col < 0 || col >= Board.COLS;
    }

    private void makeBotMove() {
        for (int row = 0; row < Board.ROWS; row++) {
            for (int col = 0; col < Board.COLS; col++) {
                if (board.cells[row][col].content == Seed.NO_SEED) {
                    currentState = board.stepGame(currentPlayer, row, col);
                    if (currentState == State.NOUGHT_WON) playerOScore++;
                    currentPlayer = Seed.CROSS;

                    updateScoreLabel(); updateStatusBar(); boardPanel.repaint();
                    return;
                }
            }
        }
    }
}
