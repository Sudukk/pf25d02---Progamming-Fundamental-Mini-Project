import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Board extends JPanel {
    Cell[][] cells;
    CellContent currentPlayer;
    PlayerState currentState;
    int rows;
    int cols;

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.currentPlayer = CellContent.CROSS;
        this.currentState = PlayerState.PLAYING;
        setLayout(new GridLayout(rows, cols));

        this.cells = new Cell[cols][rows];

        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                this.cells[i][j] = new Cell(i, j);
                add(this.cells[i][j]);
            }
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                Cell sampleCell = cells[0][0];
                int col = x / sampleCell.getCellWidth();
                int row = y / sampleCell.getCellHeight();
                if (currentState == PlayerState.PLAYING) {
                    if (row >= 0 && row < rows && col >= 0 && col < cols
                            && cells[row][col].content == CellContent.NONE) {
                        currentState = stepGame(currentPlayer, row, col);
                        currentPlayer = (currentPlayer == CellContent.CROSS) ? CellContent.NOUGHT : CellContent.CROSS;
                    }
                } else {
                    newGame();
                }
                repaint();
                System.out.println("x position: " + x);
                System.out.println("y position: " + y);
                System.out.println("row position: " + row);
                System.out.println("column position: " + col);
            }
        });
    }

    public void newGame() {
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                this.cells[i][j].content = CellContent.NONE;
            }
        }
        currentPlayer = CellContent.CROSS;
        currentState = PlayerState.PLAYING;
    }

    public PlayerState stepGame(CellContent player, int selectedRow, int selectedCol) {
        // Update game board
        cells[selectedRow][selectedCol].content = player;

        // Compute and return the new game state
        if (cells[selectedRow][0].content == player // 3-in-the-row
                && cells[selectedRow][1].content == player
                && cells[selectedRow][2].content == player

                || cells[0][selectedCol].content == player // 3-in-the-column

                        && cells[1][selectedCol].content == player

                        && cells[2][selectedCol].content == player
                || selectedRow == selectedCol // 3-in-the-diagonal
                        && cells[0][0].content == player
                        && cells[1][1].content == player
                        && cells[2][2].content == player
                || selectedRow + selectedCol == 2 // 3-in-the-opposite-diagonal
                        && cells[0][2].content == player
                        && cells[1][1].content == player
                        && cells[2][0].content == player) {
            return (player == CellContent.CROSS) ? PlayerState.CROSS_WON : PlayerState.NOUGHT_WON;
        } else {
            // Nobody win. Check for DRAW (all cells occupied) or PLAYING.
            for (int row = 0; row < rows; ++row) {
                for (int col = 0; col < cols; ++col) {
                    if (cells[row][col].content == CellContent.NONE) {
                        return PlayerState.PLAYING; // still have empty cells
                    }
                }
            }
            return PlayerState.DRAW; // no empty cell, it's a draw
        }
    }

}
