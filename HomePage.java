import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class HomePage extends JPanel { 
    private App app; // Reference to the main application class
    private JLabel highScoreLabel; // Label to display the high score
    private int highScore; // Variable to store the high score

    // Constructor for HomePage
    public HomePage(App app, int highScore) {
        this.app = app; // Initialize the app reference
        this.highScore = highScore; // Initialize the high score
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // Set layout to vertical BoxLayout

        // Create and configure title label
        JLabel titleLabel = new JLabel("Reverse Flappy Bird ↺ ↻");
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 48)); // Set font for title
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align the title

        // Create and configure single player button
        JButton singlePlayerButton = new JButton("Single Player");
        singlePlayerButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align the button
        singlePlayerButton.setFont(new Font("Poppins", Font.PLAIN, 24)); // Set font for button
        singlePlayerButton.addActionListener(new ActionListener() { // Add action listener to the button
            @Override
            public void actionPerformed(ActionEvent e) { // Define action to perform on button click
                app.startCountdown(1); // Start countdown for single player mode
            }
        });

        // Create and configure two player button
        JButton twoPlayerButton = new JButton("Two Players");
        twoPlayerButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align the button
        twoPlayerButton.setFont(new Font("Poppins", Font.PLAIN, 24)); // Set font for button
        twoPlayerButton.addActionListener(new ActionListener() { // Add action listener to the button
            @Override
            public void actionPerformed(ActionEvent e) { // Define action to perform on button click
                app.startCountdown(2); // Start countdown for two players mode
            }
        });

        // Create and configure high score label
        highScoreLabel = new JLabel("High Score: " + highScore);
        highScoreLabel.setFont(new Font("Poppins", Font.PLAIN, 24)); // Set font for label
        highScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align the label

        add(Box.createVerticalStrut(100)); // Add vertical spacer
        add(titleLabel); // Add title label to the panel
        add(Box.createVerticalStrut(50)); // Add vertical spacer
        add(singlePlayerButton); // Add single player button to the panel
        add(Box.createVerticalStrut(20)); // Add vertical spacer
        add(twoPlayerButton); // Add two player button to the panel
        add(Box.createVerticalStrut(50)); // Add vertical spacer
        add(highScoreLabel); // Add high score label to the panel
    }

    // Method to update the high score
    public void updateHighScore(int newHighScore) {
        if (newHighScore > highScore) { // Check if the new high score is greater than the current high score
            highScore = newHighScore; // Update the high score
            highScoreLabel.setText("High Score: " + highScore); // Update the label text
        }
    }
}