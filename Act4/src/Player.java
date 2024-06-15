import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.net.URL;

public class Player {

    private int x;
    private int y;
    private int dx;
    private final int WIDTH = 80; // Desired width
    private final int HEIGHT = 45; // Desired height
    private final int SPEED = 4;  // Adjust the speed as needed
    private Image image;

    public Player() {
        loadImage();
        x = 175;
        y = 550;
    }

    private void loadImage() {
        URL imageUrl = getClass().getResource("/basketbaru.png");
        if (imageUrl != null) {
            ImageIcon ii = new ImageIcon(imageUrl);
            image = ii.getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
        } else {
            System.err.println("Player image not found!");
        }
    }

    public void move() {
        x += dx;

        // Ensure the basket stays within the window boundaries
        if (x < 0) {
            x = 0;
        }

        if (x > 400 - WIDTH) { // Adjusted to fit perfectly within the window
            x = 400 - WIDTH;
        }
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = -SPEED;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = SPEED;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
            dx = 0;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return WIDTH;
    }

    public int getHeight() {
        return HEIGHT;
    }

    public Image getImage() {
        return image;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }
}
