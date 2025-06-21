public class GameMain extends GameBase {
    public GameMain(String playerXName, String playerOName) {
        super(playerXName, playerOName);
    }

    @Override
    public void handleClick(int x, int y) {
        int row = y / Cell.SIZE;
        int col = x / Cell.SIZE;

        if (outOfBounds(row, col))
            return;
        if (currentState != State.PLAYING) {
            newGame();
            updateStatusBar();
            boardPanel.repaint();
            return;
        }

        if (board.cells[row][col].content == Seed.NO_SEED) {
            currentState = board.stepGame(currentPlayer, row, col);

            if (currentState == State.CROSS_WON)
                playerXScore++;
            else if (currentState == State.NOUGHT_WON)
                playerOScore++;

            SoundEffect.MOUSE_CLICK.play(5);
            currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;

            updateScoreLabel();
            updateStatusBar();
            boardPanel.repaint();
        }
    }

    private boolean outOfBounds(int row, int col) {
        return row < 0 || row >= Board.ROWS || col < 0 || col >= Board.COLS;
    }
}
