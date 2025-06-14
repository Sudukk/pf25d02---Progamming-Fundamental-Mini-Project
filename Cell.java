import java.awt.*;
import javax.swing.*;

public class Cell extends JPanel {
    int row;
    int col;
    int size;
    CellContent content;

    public Cell(int row, int col, int size) {
        this.content = CellContent.NONE;
        this.size = size;
        setSize(size, size);
        setBackground(Color.white);
        setBorder(BorderFactory.createLineBorder(Color.black));
    }

    public void clearContent() {
        this.content = CellContent.NONE;
    }

    public int getCellSize() {
        return this.size;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D gtwod = (Graphics2D) g;
        gtwod.setStroke(new BasicStroke(5));

        int padx = getWidth() / 10;
        int pady = getHeight() / 10;

        if (content == CellContent.CROSS) {
            gtwod.setColor(Color.black);
            int xone = row + padx;
            int yone = col + pady;
            int xtwo = row + (getWidth() - padx);
            int ytwo = col + (getHeight() - pady);
            gtwod.drawLine(xone, yone, xtwo, ytwo);
            gtwod.drawLine(xone, ytwo, xtwo, yone);
        } else if (content == CellContent.NOUGHT) {
            gtwod.setColor(Color.red);
            int xone = row * (getWidth() / 2) + padx;
            int yone = col * (getHeight() / 2) + pady;
            gtwod.drawOval(xone, yone, size, size);
        }
    }
}
