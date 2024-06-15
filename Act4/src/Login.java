import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class Login extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JTextArea responseArea;
    public static String username; // Add this line to store the username

    public Login() {
        setTitle("User Login");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Dispose on close to separate windows

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Username label and field
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        usernameField = new JTextField(15);
        panel.add(usernameField, gbc);

        // Password label and field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        // Login button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginButton = new JButton("Login");
        panel.add(loginButton, gbc);

        // Response area
        responseArea = new JTextArea(5, 20);
        responseArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(responseArea);
        panel.add(scrollPane, gbc);

        add(panel);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (username.isEmpty() || password.isEmpty()) {
                    responseArea.setText("Username and password are required");
                    return;
                }

                try {
                    // Send login data to Node.js server (replace with your backend logic)
                    URL url = new URL("http://localhost:8000/login");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setDoOutput(true);

                    // Create JSON object
                    String jsonInputString = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";
                    try (OutputStream os = conn.getOutputStream()) {
                        byte[] input = jsonInputString.getBytes("utf-8");
                        os.write(input, 0, input.length);
                    }

                    // Read response
                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        responseArea.setText("Login successful");
                        // Show the main menu after successful login
                        SwingUtilities.invokeLater(() -> {
                            MainMenu mainMenu = new MainMenu();
                            mainMenu.setVisible(true);
                            dispose(); // Close the login window
                        });
                    } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        responseArea.setText("Invalid username or password");
                    } else {
                        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "utf-8"))) {
                            StringBuilder response = new StringBuilder();
                            String responseLine;
                            while ((responseLine = br.readLine()) != null) {
                                response.append(responseLine.trim());
                            }
                            responseArea.setText("Error logging in: " + response.toString());
                        }
                    }

                    conn.disconnect();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    responseArea.setText("Error connecting to server: " + ex.getMessage());
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }
}
