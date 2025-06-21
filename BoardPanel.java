import javax.swing.*;
import java.awt.*;

/**
 * A separate class for the Tic-Tac-Toe board panel.
 */
public class BoardPanel extends JPanel {
    private Board board;

    public BoardPanel(Board board) {
        this.board = board;
        setBackground(GameConstants.COLOR_BG);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.board.paint(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT);
    }
}
