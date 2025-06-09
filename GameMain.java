import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Tic-Tac-Toe: Two-player Graphic version with better OO design.
 * The Board and Cell classes are separated in their own classes.
 */
public class GameMain extends JPanel {
    private static final long serialVersionUID = 1L; // to prevent serializable warning

    // Define named constants for the drawing graphics
    public static final String TITLE = "Tic Tac Toe";
    public static final Color COLOR_BG = Color.orange;
    public static final Color COLOR_BG_STATUS = new Color(0, 0, 0);
    public static final Color COLOR_CROSS = new Color(239, 105, 80); // Red #EF6950
    public static final Color COLOR_NOUGHT = new Color(64, 154, 225); // Blue #409AE1

    // Mengubah font menjadi Segoe UI dengan style bold dan ukuran 14
    public static final Font FONT_STATUS = new Font("Segoe UI", Font.BOLD, 14);

    // Define game objects
    private Board board; // the game board
    private State currentState; // the current state of the game
    private Seed currentPlayer; // the current player
    private JLabel statusBar; // for displaying status message
    private JButton restartButton; // tombol untuk mengulang permainan baik salah satu menang atau remis.

    /** Constructor to setup the UI and game components */
    public GameMain() {

        // This JPanel fires MouseEvent
        super.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { // mouse-clicked handler
                int mouseX = e.getX();
                int mouseY = e.getY();
                // Get the row and column clicked
                int row = mouseY / Cell.SIZE;
                int col = mouseX / Cell.SIZE;

                if (currentState == State.PLAYING) {
                    if (row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS
                            && board.cells[row][col].content == Seed.NO_SEED) {
                        // Update cells[][] and return the new game state after the move
                        currentState = board.stepGame(currentPlayer, row, col);
                        // Switch player
                        currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                    }
                }
                // menghapus kondisi jika `currentState` bukan `State.PLAYING`. Awalnya terdapat
                // kondisi `else` yang berfungsi untuk
                // mengulang permainan jika user mengklik `JPanel` alias `GameMain`, tetapi
                // logika ini diurus oleh `restartButton`.
                // Refresh the drawing canvas
                repaint(); // Callback paintComponent().
            }
        });

        // Setup the status bar (JLabel) to display status message
        statusBar = new JLabel();
        statusBar.setFont(FONT_STATUS);
        statusBar.setBackground(COLOR_BG_STATUS);
        statusBar.setOpaque(true);
        statusBar.setPreferredSize(new Dimension(300, 30));
        statusBar.setHorizontalAlignment(JLabel.CENTER);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));

        super.setLayout(new BorderLayout());
        // Ubah posisi `statusBar` menjadi dibawah
        super.add(statusBar, BorderLayout.PAGE_END);
        super.setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + 30));
        // account for statusBar in height
        super.setBorder(BorderFactory.createLineBorder(COLOR_BG_STATUS, 2, false));

        // inisialisasi `restartButton`
        restartButton = new JButton("Play Again?");
        restartButton.setVisible(false);
        restartButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Hal yang akan dilakukan `restartButton` jika user mengklik tombol.
        restartButton.addActionListener(_ -> {
            newGame();
            repaint();
        });

        // Atur posisi `restartButton` menjadi bagian bawah
        super.add(restartButton, BorderLayout.PAGE_START);

        // Set up Game
        initGame();
        newGame();
    }

    /** Initialize the game (run once) */
    public void initGame() {
        board = new Board(); // allocate the game-board
    }

    /** Reset the game-board contents and the current-state, ready for new game */
    public void newGame() {
        restartButton.setVisible(false);
        for (int row = 0; row < Board.ROWS; ++row) {
            for (int col = 0; col < Board.COLS; ++col) {
                board.cells[row][col].content = Seed.NO_SEED; // all cells empty
            }
        }
        currentPlayer = Seed.CROSS; // cross plays first
        currentState = State.PLAYING; // ready to play
    }

    /** Custom painting codes on this JPanel */
    @Override
    public void paintComponent(Graphics g) { // Callback via repaint()
        super.paintComponent(g);
        setBackground(COLOR_BG); // set its background color

        board.paint(g); // ask the game board to paint itself

        // Print status-bar message
        if (currentState == State.PLAYING) {
            statusBar.setForeground(Color.BLACK);
            // Karena `X` and `O` mempunyai warna masing-masing (merah dan hitam)
            // Sebaiknya setiap langkah (baik `X` maupun `O`) ditandai dengan warnanya
            // masing-masing
            if (currentPlayer == Seed.CROSS) {
                statusBar.setForeground(Color.BLACK);
                statusBar.setText("X's Turn");
            } else {
                statusBar.setForeground(Color.RED);
                statusBar.setText("O's Turn");
            }
        } else if (currentState == State.DRAW) {
            // Ubah warna merah menjadi warna kuning ketika remis.
            statusBar.setForeground(Color.YELLOW);
            statusBar.setText("It's a Draw! Click to play again.");

            // Pada kondisi remis. `restartButton` akan muncul pada bagian atas.
            restartButton.setVisible(true);
        } else if (currentState == State.CROSS_WON) {
            // Ubah warna text saat `X` menang yaitu warna hitam
            statusBar.setForeground(Color.BLACK);
            statusBar.setText("'X' Won! Click to play again.");

            // Pada kondisi `X` menang, `restartButton` akan muncul pada bagian atas.
            restartButton.setVisible(true);
        } else if (currentState == State.NOUGHT_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("'O' Won! Click to play again.");

            // Pada kondisi `O` menang, `restartButton` akan muncul pada bagian atas.
            restartButton.setVisible(true);
        }
    }

    /** The entry "main" method */
    public static void main(String[] args) {
        // Run GUI construction codes in Event-Dispatching thread for thread safety
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame(TITLE);
                // Set the content-pane of the JFrame to an instance of main JPanel
                frame.setContentPane(new GameMain());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setLocationRelativeTo(null); // center the application window
                frame.setVisible(true); // show it
            }
        });
    }
}
