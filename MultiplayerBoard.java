import java.util.List;

public class MultiplayerBoard extends Board {
    private DBManager dbManager;

    public MultiplayerBoard(DBManager dbManager) {
        super(); // Initializes board
        this.dbManager = dbManager;
    }

    /** Sync board from DB state */
    public void syncFromDatabase() {
        List<String[]> moves = dbManager.getAllMoves();
        newGame(); // Clear board before sync
        if (!moves.isEmpty()) {
            String[] last = moves.get(moves.size() - 1);
            String px = last[0];
            String po = last[1];

            // Update cell contents based on px & po strings
            for (int i = 0; i < px.length(); i++) {
                int r = i / COLS;
                int c = i % COLS;
                char pxChar = px.charAt(i);
                char poChar = po.charAt(i);
                if (pxChar == '1') cells[r][c].content = Seed.CROSS;
                else if (poChar == '1') cells[r][c].content = Seed.NOUGHT;
                else cells[r][c].content = Seed.NO_SEED;
            }
        }
    }

    /** Convert current board to string format like "100010000" */
    public String getBoardString(Seed player) {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                sb.append(cells[row][col].content == player ? '1' : '0');
            }
        }
        return sb.toString();
    }
}
