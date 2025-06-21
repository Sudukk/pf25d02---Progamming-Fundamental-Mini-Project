public class Move {
    public char player;
    public int row, col;
    public int moveNumber;

    public Move(char player, int row, int col, int moveNumber) {
        this.player = player;
        this.row = row;
        this.col = col;
        this.moveNumber = moveNumber;
    }
}
