import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HighScore extends JFrame {

    private static final String HIGH_SCORE_URL = "http://localhost:81/get_high_scores.php"; // Replace with your actual endpoint

    public HighScore() {
        initUI();
    }

    private void initUI() {
        setTitle("High Scores");
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create a table model with columns: Rank, Username, Score
        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);
        model.addColumn("Rank");
        model.addColumn("Username");
        model.addColumn("Score");

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);

        fetchAndDisplayHighScores(model);

        pack();
    }

    private void fetchAndDisplayHighScores(DefaultTableModel model) {
        try {
            URL url = new URL(HIGH_SCORE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "text/plain");

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                int rank = 1;

                while ((inputLine = in.readLine()) != null) {
                    String[] parts = inputLine.split(",");
                    if (parts.length >= 2) {
                        String username = parts[0].trim();
                        int score = Integer.parseInt(parts[1].trim());
                        model.addRow(new Object[]{rank, username, score});
                        rank++;
                    }
                }
                in.close();
            } else {
                model.addRow(new Object[]{"Error fetching scores", "", ""});
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addRow(new Object[]{"Error fetching scores", "", ""});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HighScore highScore = new HighScore();
            highScore.setVisible(true);
        });
    }
}
