import java.awt.*;
import javax.swing.*;

public class Board extends JPanel {
    Cell[][] cells;
    int row;
    int col;

    public Board(int row, int col) {
        this.row = row;
        this.col = col;
        setLayout(new GridLayout(row, col));

        this.cells = new Cell[col][row];

        for (int i = 0; i < col; i++) {
            for (int j = 0; j < row; j++) {
                this.cells[i][j] = new Cell(i, j, 100);
                add(this.cells[i][j]);
            }
        }

    }
}
