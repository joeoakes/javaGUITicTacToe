import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Tic Tac Toe - simple 2-player Swing GUI (X vs O).
 * - Click a square to place the current player's mark.
 * - The status bar shows whose turn it is or who won.
 * - "Reset" clears the board for a new game.
 */
public class TicTacToe extends JFrame {

    // UI
    private final JButton[] cells = new JButton[9];
    private final JLabel status = new JLabel("Player X's turn");
    private final JButton resetBtn = new JButton("Reset");

    // Game state
    private char currentPlayer = 'X';   // 'X' starts
    private boolean gameOver = false;

    public TicTacToe() {
        super("Tic Tac Toe");

        // ----- Top panel: status + reset -----
        JPanel top = new JPanel(new BorderLayout(8, 8));
        status.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        top.add(status, BorderLayout.CENTER);

        resetBtn.addActionListener(e -> resetGame());
        top.add(resetBtn, BorderLayout.EAST);

        // ----- Center panel: 3x3 grid of buttons -----
        JPanel grid = new JPanel(new GridLayout(3, 3, 6, 6));
        grid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        Font big = new Font(Font.SANS_SERIF, Font.BOLD, 42);

        for (int i = 0; i < 9; i++) {
            JButton b = new JButton("");
            b.setFont(big);
            b.setFocusPainted(false);
            b.setBackground(Color.WHITE);
            final int idx = i; // needed for lambda / inner class
            b.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleMove(idx);
                }
            });
            cells[i] = b;
            grid.add(b);
        }

        // ----- Frame layout -----
        setLayout(new BorderLayout());
        add(top, BorderLayout.NORTH);
        add(grid, BorderLayout.CENTER);

        // ----- Frame settings -----
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(360, 420);
        setLocationRelativeTo(null); // center on screen
        setVisible(true);
    }

    /** Called when a cell is clicked. */
    private void handleMove(int idx) {
        if (gameOver) return;

        JButton cell = cells[idx];

        // Ignore clicks on already-filled cells
        if (!cell.getText().isEmpty()) return;

        // Mark the cell with the current player's symbol
        cell.setText(String.valueOf(currentPlayer));

        // Check for win/draw after the move
        if (isWinner(currentPlayer)) {
            status.setText("Player " + currentPlayer + " wins!");
            highlightWinningLine(currentPlayer); // optional flair
            gameOver = true;
            return;
        }

        if (isDraw()) {
            status.setText("It's a draw!");
            gameOver = true;
            return;
        }

        // Switch players and update status
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
        status.setText("Player " + currentPlayer + "'s turn");
    }

    /** Returns true if the board has no empty cells and nobody won. */
    private boolean isDraw() {
        for (JButton b : cells) {
            if (b.getText().isEmpty()) return false;
        }
        return true;
    }

    /**
     * Returns true if 'p' ('X' or 'O') has a winning 3-in-a-row.
     * We check all 8 winning lines by index.
     */
    private boolean isWinner(char p) {
        String s = String.valueOf(p);
        int[][] lines = {
                {0,1,2}, {3,4,5}, {6,7,8},  // rows
                {0,3,6}, {1,4,7}, {2,5,8},  // cols
                {0,4,8}, {2,4,6}            // diagonals
        };
        for (int[] L : lines) {
            if (cells[L[0]].getText().equals(s) &&
                    cells[L[1]].getText().equals(s) &&
                    cells[L[2]].getText().equals(s)) {
                return true;
            }
        }
        return false;
    }

    /** Highlights the winning line for player p, if any. */
    private void highlightWinningLine(char p) {
        String s = String.valueOf(p);
        int[][] lines = {
                {0,1,2}, {3,4,5}, {6,7,8},
                {0,3,6}, {1,4,7}, {2,5,8},
                {0,4,8}, {2,4,6}
        };
        for (int[] L : lines) {
            if (cells[L[0]].getText().equals(s) &&
                    cells[L[1]].getText().equals(s) &&
                    cells[L[2]].getText().equals(s)) {
                // Simple highlight effect
                for (int i : L) {
                    cells[i].setBackground(new Color(220, 255, 220));
                }
                break;
            }
        }
    }

    /** Clears the board and resets the game state. */
    private void resetGame() {
        for (JButton b : cells) {
            b.setText("");
            b.setBackground(Color.WHITE);
        }
        currentPlayer = 'X';
        gameOver = false;
        status.setText("Player X's turn");
    }

    public static void main(String[] args) {
        // Ensure a consistent look & feel on different platforms
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new TicTacToe();
        });
    }
}
