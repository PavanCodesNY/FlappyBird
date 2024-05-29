import javax.swing.*; 
import java.awt.*; 

public class App { // Define the App class

    // Declaration of the window, classes and instance variables
    JFrame frame; // JFrame or JPanel to hold the application window
    HomePage homePage; // HomePage panel
    FlappyBird gamePanel; // FlappyBird game panel
    CountdownPanel countdownPanel; // CountdownPanel panel
    int highScore; // Variable to keep track of the high score

    public App() { // Constructor for the App class
        frame = new JFrame("Flappy Bird"); // Create a new JFrame or JPanel with title "Flappy Bird"
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Set default close operation
        frame.setSize(800, 640); // Set size of the frame
        frame.setResizable(false); // Make frame non-resizable
        frame.setLocationRelativeTo(null); // Center the frame on the screen

        highScore = 0; // Initialize high score to 0

        homePage = new HomePage(this, highScore); // Initialize HomePage with the current high score
        frame.add(homePage); // Add HomePage to the frame

        frame.setVisible(true); // Make the frame visible
    }

    // Method to start the countdown before the game starts
    public void startCountdown(int numberOfPlayers) {
        frame.getContentPane().removeAll(); // Remove all components from the frame
        countdownPanel = new CountdownPanel(this, numberOfPlayers); // Initialize CountdownPanel
        frame.add(countdownPanel); // Add CountdownPanel to the frame
        frame.revalidate(); // Revalidate the frame to apply changes
        frame.repaint(); // Repaint the frame to reflect changes
    }

    // Method to start the game
    public void startGame(int numberOfPlayers) {
        frame.getContentPane().removeAll(); // Remove all components from the frame
        gamePanel = new FlappyBird(numberOfPlayers, this); // Initialize FlappyBird game panel
        frame.add(gamePanel); // Add game panel to the frame
        frame.revalidate(); // Revalidate the frame to apply changes
        frame.repaint(); // Repaint the frame to reflect changes
        gamePanel.requestFocusInWindow(); // Request focus for the game panel to capture key events
    }

    // Method to end the game and update the high score if needed
    public void endGame(int score) {
        if (score > highScore) { // Check if the current score is higher than the high score
            highScore = score; // Update high score
        }
        homePage.updateHighScore(highScore); // Update the high score on the HomePage
        frame.revalidate(); // Revalidate the frame to apply changes
        frame.repaint(); // Repaint the frame to reflect changes
    }

    // Getter method for high score
    public int getHighScore() {
        return highScore; // Return the current high score
    }

    // Setter method for high score
    public void setHighScore(int highScore) {
        this.highScore = highScore; // Update the high score
        homePage.updateHighScore(highScore); // Update the high score on the HomePage
    }

    // Main method to start the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() { // Schedule a job for the event dispatch thread
            @Override
            public void run() {
                new App(); // Create an instance of the App class
            }
        });
    }

    // Method to show the HomePage
    public void showHomePage() {
        frame.getContentPane().removeAll(); // Remove all components from the frame
        homePage = new HomePage(this, highScore); // Reinitialize HomePage with the current high score
        frame.add(homePage); // Add HomePage to the frame
        frame.revalidate(); // Revalidate the frame to apply changes
        frame.repaint(); // Repaint the frame to reflect changes
    }
}