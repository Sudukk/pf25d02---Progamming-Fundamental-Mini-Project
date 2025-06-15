import java.awt.*;
import javax.swing.*;

public class Cell extends JPanel {
    int row;
    int col;
    CellContent content;
    CellSettings settings;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.content = CellContent.NONE;
        this.settings = new CellSettings.Builder().build();
        setSize(settings.width, settings.height);
        setBackground(settings.backgroundColor);
        setBorder(BorderFactory.createLineBorder(settings.borderColor, settings.borderThickness));
    }

    public Cell(int row, int col, CellSettings settings) {
        this.row = row;
        this.col = col;
        this.content = CellContent.NONE;
        this.settings = settings;
        setSize(settings.width, settings.height);
        setBackground(settings.backgroundColor);
        setBorder(BorderFactory.createLineBorder(settings.borderColor, settings.borderThickness));
    }

    public void clearContent() {
        this.content = CellContent.NONE;
    }

    public int getCellWidth() {
        return this.settings.width;
    }

    public int getCellHeight() {
        return this.settings.height;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D gtwod = (Graphics2D) g;
        gtwod.setStroke(new BasicStroke(5));

        // int padx = getWidth() / 10;
        // int pady = getHeight() / 10;
        int padx = getCellWidth() / 10;
        int pady = getCellHeight() / 10;

        if (content == CellContent.CROSS) {
            gtwod.setColor(Color.black);
            // int xone = row + padx;
            // int yone = col + pady;
            // int xtwo = row + (getWidth() - padx);
            // int ytwo = col + (getHeight() - pady);
            // gtwod.drawLine(xone, yone, xtwo, ytwo);
            // gtwod.drawLine(xone, ytwo, xtwo, yone);
            int width = getWidth();
            int height = getHeight();
            int size = Math.min(width, height);
            int padding = size / 6;
            gtwod.drawLine(padding, padding, width - padding, height - padding);
            gtwod.drawLine(padding, height - padding, width - padding, padding);
        } else if (content == CellContent.NOUGHT) {
            gtwod.setColor(Color.red);
            int width = getWidth();
            int height = getHeight();
            int size = Math.min(width, height);
            int padding = size / 6;
            int diameter = size - 2 * padding;
            int xone = (width - diameter) / 2;
            int yone = (height - diameter) / 2;
            gtwod.drawOval(xone, yone, diameter, diameter);
        }
    }
}
