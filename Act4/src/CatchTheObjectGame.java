import javax.swing.*;

public class CatchTheObjectGame extends JFrame {
    public CatchTheObjectGame() {
        initUI();
    }

    private void initUI() {
        GameBoard gameBoard = new GameBoard();
        add(gameBoard);
        pack();  // Adjust the window size to fit the game board

        setTitle("Catch the Object Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // Center the window
        setResizable(false);  // Prevent resizing the window
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CatchTheObjectGame game = new CatchTheObjectGame();
            game.setVisible(true);
        });
    }

}