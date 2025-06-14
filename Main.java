import java.awt.*;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // outer window
        JFrame frame = new JFrame("Tic Tac Toe");
        // frame.setBackground(new Color(76, 143, 112));

        // initialize outer window
        frame.setSize(400, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        frame.add(new Board(3, 3), BorderLayout.CENTER);
        frame.setVisible(true);

    }
    // public static void tmp() {
    // // outer window
    // JFrame frame = new JFrame("Tic Tac Toe");

    // // initialize outer window
    // frame.setSize(400, 500);
    // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // frame.setLayout(new BorderLayout());

    // // all video game menus
    // JPanel menus = new JPanel();
    // menus.setLayout(new CardLayout());

    // // one of video game menus
    // JPanel mainMenu = new JPanel();
    // mainMenu.setBackground(Color.green);
    // JButton startBtn = new JButton("start game!");
    // mainMenu.add(startBtn);
    // menus.add(mainMenu);

    // // one of video game menus
    // // TODO: make this tic-tac-toe
    // JPanel gameMenu = new JPanel();
    // gameMenu.setBackground(Color.blue);
    // JButton goToMenuBtn = new JButton("goto menu!");
    // gameMenu.add(goToMenuBtn);
    // menus.add(gameMenu);

    // // when clicked, it will go to the next menu
    // startBtn.addActionListener(_ -> {
    // CardLayout cl = (CardLayout) menus.getLayout();
    // cl.next(menus);
    // });

    // // when clicked, it will go to the next menu
    // goToMenuBtn.addActionListener(_ -> {
    // CardLayout cl = (CardLayout) menus.getLayout();
    // cl.next(menus);
    // });

    // frame.add(menus, BorderLayout.CENTER);
    // frame.setVisible(true);
    // }
}
