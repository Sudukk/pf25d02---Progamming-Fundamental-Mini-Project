import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.io.File;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        // JPanel panelfirst = new JPanel();
        // panelfirst.setBackground(Color.red);

        // JPanel panelTwo = new JPanel();
        // panelTwo.setBackground(Color.green);
        // panelTwo.setLayout(new BorderLayout());

        // JPanel panelThree = new JPanel();
        // panelThree.setBackground(Color.yellow);

        // panelTwo.add(panelThree, BorderLayout.NORTH);

        // JPanel panelLast = new JPanel();
        // panelLast.setBackground(Color.blue);

        JFrame frame = new JFrame("tic tac toe");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new SettingsMenu());
        // frame.add(panelfirst, BorderLayout.NORTH);
        // frame.add(panelTwo, BorderLayout.CENTER);
        // frame.add(panelLast, BorderLayout.SOUTH);

        frame.pack();
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
