import javax.swing.*;

public class GameManager {
    public static final String TITLE = "Tic Tac Toe";

    public static void main(String[] args) {
        SoundEffect.playBGMusic();
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(TITLE);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new StartMenu(frame));
            frame.pack();
            frame.setSize(720, 900);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
