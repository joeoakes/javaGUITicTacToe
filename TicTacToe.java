// Import Swing (GUI) and AWT (layout/event) packages
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Tic Tac Toe - A simple two-player Java Swing GUI game.
 *
 * Features:
 * - 3x3 grid of clickable buttons (X vs O).
 * - Displays whose turn it is.
 * - Detects wins, draws, and highlights the winning line.
 * - Includes a Reset button to start a new game.
 */
public class TicTacToe extends JFrame {

    // Create an array of 9 buttons for the 3x3 grid
    private final JButton[] cells = new JButton[9];

    // Label at the top to show game status (whose turn, win, draw)
    private final JLabel status = new JLabel("Player X's turn");

    // Reset button to restart the game
    private final JButton resetBtn = new JButton("Reset");

    // Track current player: either 'X' or 'O'
    private char currentPlayer = 'X';

    // Flag to check if the game has ended
    private boolean gameOver = false;

    // ----- Constructor: builds the GUI -----
    public TicTacToe() {
        super("Tic Tac Toe"); // Set window title

        // ----- TOP PANEL -----
        // Contains the status label and reset button
        JPanel top = new JPanel(new BorderLayout(8, 8));
        status.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10)); // Add padding
        top.add(status, BorderLayout.CENTER); // Center = status message
        top.add(resetBtn, BorderLayout.EAST); // Right side = reset button

        // Add click event to reset the board when "Reset" is pressed
        resetBtn.addActionListener(e -> resetGame());

        // ----- CENTER PANEL -----
        // Create a grid layout for the 3x3 cells
        JPanel grid = new JPanel(new GridLayout(3, 3, 6, 6));
        grid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Set font for X and O marks
        Font big = new Font(Font.SANS_SERIF, Font.BOLD, 42);

        // Create each button (cell) in the 3x3 board
        for (int i = 0; i < 9; i++) {
            JButton b = new JButton("");       // Start empty
            b.setFont(big);                        // Large bold text
            b.setFocusPainted(false);              // No focus outline
            b.setBackground(Color.WHITE);          // White background

            final int idx = i;                     // Index for event handler
            // Add click listener for each cell
            b.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleMove(idx);               // Handle button click
                }
            });

            cells[i] = b;                          // Store button in array
            grid.add(b);                           // Add button to panel
        }

        // ----- FRAME LAYOUT -----
        // Use BorderLayout to position top and grid panels
        setLayout(new BorderLayout());
        add(top, BorderLayout.NORTH);
        add(grid, BorderLayout.CENTER);

        // ----- WINDOW SETTINGS -----
        setDefaultCloseOperation(EXIT_ON_CLOSE);   // Close program on X
        setSize(360, 420);             // Set window size
        setLocationRelativeTo(null);               // Center window on screen
        setVisible(true);                          // Make window visible
    }

    /**
     * Handles a move when a player clicks on a cell.
     * @param idx index of the clicked cell (0–8)
     */
    private void handleMove(int idx) {
        // Ignore moves if game is already over
        if (gameOver) return;

        JButton cell = cells[idx];

        // Prevent overwriting a cell that already has X or O
        if (!cell.getText().isEmpty()) return;

        // Place current player's symbol in the selected cell
        cell.setText(String.valueOf(currentPlayer));

        // Check for a winning combination
        if (isWinner(currentPlayer)) {
            status.setText("Player " + currentPlayer + " wins!");
            highlightWinningLine(currentPlayer); // Optional visual highlight
            gameOver = true;
            return;
        }

        // If board is full and no winner, it's a draw
        if (isDraw()) {
            status.setText("It's a draw!");
            gameOver = true;
            return;
        }

        // Switch turns (X -> O or O -> X)
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
        status.setText("Player " + currentPlayer + "'s turn");
    }

    /**
     * Checks if the board is full and there is no winner.
     * @return true if all cells are filled.
     */
    private boolean isDraw() {
        for (JButton b : cells) {
            if (b.getText().isEmpty()) return false; // Found an empty spot
        }
        return true; // All filled → draw
    }

    /**
     * Checks if the specified player ('X' or 'O') has won.
     * @param p the player symbol to check
     * @return true if the player has 3 in a row
     */
    private boolean isWinner(char p) {
        String s = String.valueOf(p);

        // All 8 possible winning lines (rows, columns, diagonals)
        int[][] lines = {
                {0,1,2}, {3,4,5}, {6,7,8},  // rows
                {0,3,6}, {1,4,7}, {2,5,8},  // columns
                {0,4,8}, {2,4,6}            // diagonals
        };

        // Check each line for 3 of the same symbol
        for (int[] L : lines) {
            if (cells[L[0]].getText().equals(s) &&
                    cells[L[1]].getText().equals(s) &&
                    cells[L[2]].getText().equals(s)) {
                return true; // Found a winning line
            }
        }
        return false; // No winner found
    }

    /**
     * Visually highlights the winning line (light green background).
     * @param p the player who won
     */
    private void highlightWinningLine(char p) {
        String s = String.valueOf(p);
        int[][] lines = {
                {0,1,2}, {3,4,5}, {6,7,8},
                {0,3,6}, {1,4,7}, {2,5,8},
                {0,4,8}, {2,4,6}
        };

        // Find which line belongs to the winner
        for (int[] L : lines) {
            if (cells[L[0]].getText().equals(s) &&
                    cells[L[1]].getText().equals(s) &&
                    cells[L[2]].getText().equals(s)) {
                // Change background color of the winning buttons
                for (int i : L) {
                    cells[i].setBackground(new Color(220, 255, 220));
                }
                break;
            }
        }
    }

    /**
     * Resets the game board and all state variables.
     * Called when the "Reset" button is clicked.
     */
    private void resetGame() {
        for (JButton b : cells) {
            b.setText("");                     // Clear text
            b.setBackground(Color.WHITE);      // Reset background
        }
        currentPlayer = 'X';                   // X always starts
        gameOver = false;                      // Allow moves again
        status.setText("Player X's turn");     // Reset status message
    }

    /**
     * Main entry point of the program.
     */
    public static void main(String[] args) {
        // Run GUI on the Event Dispatch Thread (best practice for Swing)
        SwingUtilities.invokeLater(() -> {
            try {
                // Use system's native look and feel (Windows/Mac/Linux)
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            // Create and display the game window
            new TicTacToe();
        });
    }
}
