import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame {

    public MainMenu() {
        initUI();
    }

    private void initUI() {
        setTitle("Main Menu");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JButton playButton = new JButton("Play Game");
        JButton highScoreButton = new JButton("High Scores");

        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        highScoreButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the main menu
                CatchTheObjectGame game = new CatchTheObjectGame();
                game.setVisible(true);
            }
        });

        highScoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show high scores (assuming a HighScore class exists)
                HighScore highScore = new HighScore();
                highScore.setVisible(true);
            }
        });

        panel.add(Box.createRigidArea(new Dimension(0, 50))); // Spacer
        panel.add(playButton);
        panel.add(Box.createRigidArea(new Dimension(0, 20))); // Spacer
        panel.add(highScoreButton);

        add(panel);

        pack();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainMenu mainMenu = new MainMenu();
            mainMenu.setVisible(true);
        });
    }
}
