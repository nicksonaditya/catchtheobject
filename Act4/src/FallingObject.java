import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class FallingObject {

    private int x;
    private int y;
    private final int WIDTH = 20; // Desired width
    private final int HEIGHT = 20; // Desired height
    private final int SPEED = 2;
    private Image image;

    public FallingObject() {
        loadImage();
        resetPosition();
    }

    private void loadImage() {
        URL imageUrl = getClass().getResource("/fallingObject.png");
        if (imageUrl != null) {
            ImageIcon ii = new ImageIcon(imageUrl);
            image = ii.getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
        } else {
            System.err.println("Falling object image not found!");
        }
    }

    public void fall() {
        y += SPEED;
    }

    public void resetPosition() {
        x = (int) (Math.random() * 380);
        y = 0;
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
