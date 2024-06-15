import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class GameBoard extends JPanel implements ActionListener {

    private Timer gameTimer;
    private Timer countdownTimer;
    private Player player;
    private List<FallingObject> fallingObjects;
    private boolean inGame;
    private int score;
    private int timeLeft; // Time left in seconds
    private final int GAME_DURATION = 60; // Game duration in seconds
    private final int B_WIDTH = 400; // Width of the game board
    private final int B_HEIGHT = 600; // Height of the game board
    private final int DELAY = 10; // Timer delay in milliseconds

    public GameBoard() {
        initBoard();
    }

    private void initBoard() {
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new TAdapter());

        inGame = true;
        score = 0;
        timeLeft = GAME_DURATION;

        player = new Player();
        fallingObjects = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            fallingObjects.add(new FallingObject());
        }

        gameTimer = new Timer(DELAY, this);
        gameTimer.start();
        startCountdownTimer();
    }

    private void startCountdownTimer() {
        countdownTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLeft--;
                if (timeLeft <= 0) {
                    inGame = false;
                    gameTimer.stop();
                    countdownTimer.stop();
                    showGameOver();
                }
            }
        });
        countdownTimer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (inGame) {
            drawObjects(g);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void drawObjects(Graphics g) {
        g.drawImage(player.getImage(), player.getX(), player.getY(), this);

        for (FallingObject obj : fallingObjects) {
            g.drawImage(obj.getImage(), obj.getX(), obj.getY(), this);
        }

        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 10, 20);
        g.drawString("Time: " + timeLeft + " seconds", B_WIDTH - 100, 20);
    }

    private void showGameOver() {
        String msg = "Game Over\nFinal Score: " + score;
        int option = JOptionPane.showOptionDialog(this, msg, "Game Over",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, new Object[]{"Main Menu"}, null);

        if (option == 0) {
            saveScoreToDatabase(score); // Save score to database
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(GameBoard.this);
            topFrame.dispose(); // Close the game window
            MainMenu mainMenu = new MainMenu(); // Assuming MainMenu is the class for the main menu
            mainMenu.setVisible(true);
        }
    }

    private void saveScoreToDatabase(int score) {
        SwingUtilities.invokeLater(() -> {
            try {
                URL url = new URL("http://localhost:8000/score");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                // Create JSON object with username and score
                String jsonInputString = "{\"username\": \"" + Login.username + "\", \"score\": " + score + "}";
                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    System.out.println("Score saved successfully");
                } else {
                    System.out.println("Failed to save score");
                }

                conn.disconnect();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            updatePlayer();
            updateFallingObjects();
            checkCollisions();
        }
        repaint();
    }

    private void updatePlayer() {
        player.move();
    }

    private void updateFallingObjects() {
        for (FallingObject obj : fallingObjects) {
            obj.fall();
            if (obj.getY() > B_HEIGHT) {
                obj.resetPosition();
            }
        }
    }

    private void checkCollisions() {
        Rectangle playerBounds = player.getBounds();

        for (FallingObject obj : fallingObjects) {
            Rectangle objBounds = obj.getBounds();

            if (playerBounds.intersects(objBounds)) {
                score++;
                obj.resetPosition();
            }
        }
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            player.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            player.keyPressed(e);
        }
    }
}
