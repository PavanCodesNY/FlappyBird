import javax.swing.*; 
import java.awt.*; 
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener; 

public class CountdownPanel extends JPanel { 
    private JLabel countdownLabel; // Label to display the countdown value
    private int countdownValue; // Variable to store the countdown value
    private Timer countdownTimer; // Timer for countdown
    private App app; // Reference to the main application class
    private int numberOfPlayers; // Variable to store the number of players

    // Constructor for CountdownPanel
    public CountdownPanel(App app, int numberOfPlayers) {
        this.app = app; // Initialize the app reference
        this.numberOfPlayers = numberOfPlayers; // Initialize the number of players

        setLayout(new BorderLayout()); // Set layout to BorderLayout

        countdownValue = 3; // Initialize countdown value to 3
        countdownLabel = new JLabel(String.valueOf(countdownValue), SwingConstants.CENTER); 
        countdownLabel.setFont(new Font("Arial", Font.BOLD, 72)); // Set font for countdown label
        add(countdownLabel, BorderLayout.CENTER); // Add countdown label to the center of the panel

        // Initialize countdown timer with a 1-second delay
        countdownTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { // Define action to perform on timer tick
                countdownValue--; // Decrement the countdown value
                if (countdownValue <= 0) { // Check if countdown is complete
                    countdownTimer.stop(); // Stop the countdown timer
                    app.startGame(numberOfPlayers); // Start the game with the specified number of players
                } else {
                    countdownLabel.setText(String.valueOf(countdownValue)); // Update the countdown label text
                }
            }
        });
        countdownTimer.start(); // Start the countdown timer
    }
}