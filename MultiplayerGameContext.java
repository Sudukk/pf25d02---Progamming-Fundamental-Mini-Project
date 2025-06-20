public class MultiplayerGameContext {
    private int gameId;
    private Seed currentPlayer;
    private State currentState;
    private int playerXScore;
    private int playerOScore;

    public MultiplayerGameContext(int gameId) {
        this.gameId = gameId;
        this.currentPlayer = Seed.CROSS;
        this.currentState = State.PLAYING;
        this.playerXScore = 0;
        this.playerOScore = 0;
    }

    public int getGameId() {
        return gameId;
    }

    public Seed getCurrentPlayer() {
        return currentPlayer;
    }

    public void switchPlayer() {
        this.currentPlayer = (this.currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
    }

    public void setCurrentPlayer(Seed player) {
        this.currentPlayer = player;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State state) {
        this.currentState = state;
    }

    public int getPlayerXScore() {
        return playerXScore;
    }

    public int getPlayerOScore() {
        return playerOScore;
    }

    public void incrementPlayerXScore() {
        playerXScore++;
    }

    public void incrementPlayerOScore() {
        playerOScore++;
    }

    public void resetGameState() {
        currentState = State.PLAYING;
        currentPlayer = Seed.CROSS;
    }
}
