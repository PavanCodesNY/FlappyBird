import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

// The FlappyBird class extends JPanel and implements ActionListener and KeyListener
public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 800; // Width of the window
    int boardHeight = 640; // Height of the window

    // Images
    Image backgroundImg; // Background image
    Image birdImg; // Image for the first bird
    Image birdBlue; // Image for the second bird
    Image topPipeImg; // Image for the top pipe
    Image bottomPipeImg; // Image for the bottom pipe

    // Bird class dimensions
    int birdWidth = 34; // Width of the bird
    int birdHeight = 24; // Height of the bird

    // Inner class representing a bird
    class Bird {
        int x, y, width, height; // Coordinates and dimensions of the bird
        Image img; // Image of the bird

        Bird(int startX, int startY, Image img) { // Constructor for the Bird class
            this.x = startX; // Initial x-coordinate
            this.y = startY; // Initial y-coordinate
            this.width = birdWidth; // Width of the bird
            this.height = birdHeight; // Height of the bird
            this.img = img; // Image of the bird
        }
    }

    // Pipe class dimensions
    int pipeX = boardWidth; // Initial x-coordinate of the pipe
    int pipeY = 0; // Initial y-coordinate of the pipe
    int pipeWidth = 64; // Width of the pipe
    int pipeHeight = 512; // Height of the pipe

    // Inner class representing a pipe
    class Pipe {
        int x = pipeX; // Initial x-coordinate of the pipe
        int y = pipeY; // Initial y-coordinate of the pipe
        int width = pipeWidth; // Width of the pipe
        int height = pipeHeight; // Height of the pipe
        Image img; // Image of the pipe
        boolean passed = false; // Flag to check if the pipe is passed by the bird

        Pipe(Image img) { // Constructor for the Pipe class
            this.img = img; // Image of the pipe
        }
    }

    // Game logic
    ArrayList<Bird> birds; // List of birds
    ArrayList<Integer> velocitiesY; // List of vertical velocities for each bird
    int velocityX = -4; // Speed at which pipes move left (simulates bird moving right)
    int gravity = -1; // Gravity effect on the bird

    ArrayList<Pipe> pipes; // List of pipes
    Random random = new Random(); // Random number generator

    Timer gameLoop; // Main game loop timer
    Timer placePipeTimer; // Timer for placing pipes
    boolean gameOver = false; // Game over flag
    double score = 0; // Current score
    int numberOfPlayers; // Number of players
    App app; // Reference to the main app
    JButton restartButton; // Button to restart the game
    JButton menuButton; // Button to go back to the menu

    // Constructor for the FlappyBird class
    FlappyBird(int numberOfPlayers, App app) {
        this.numberOfPlayers = numberOfPlayers; // Set the number of players
        this.app = app; // Set the reference to the main app

        setPreferredSize(new Dimension(boardWidth, boardHeight)); // Set the size of the panel
        setFocusable(true); // Make the panel focusable
        addKeyListener(this); // Add key listener to the panel

        // Load images
        backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage(); // Load background image
        birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage(); // Load first bird image
        birdBlue = new ImageIcon(getClass().getResource("./blueFlappy.png")).getImage(); // Load second bird image
        topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage(); // Load top pipe image
        bottomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage(); // Load bottom pipe image

        // Initialize birds and velocities
        birds = new ArrayList<>(); // Initialize the list of birds
        velocitiesY = new ArrayList<>(); // Initialize the list of vertical velocities

        // Initialize birds based on number of players
        if (numberOfPlayers == 1) {
            birds.add(new Bird(boardWidth / 8, boardHeight / 2 + 1 * birdHeight * 2, birdImg)); // Add first bird for single player
            velocitiesY.add(0); // Add initial velocity for the first bird
        } else if (numberOfPlayers == 2) {
            birds.add(new Bird(boardWidth / 8, boardHeight / 2 + 1 * birdHeight * 2, birdImg)); // Add first bird for two players
            velocitiesY.add(0); // Add initial velocity for the first bird
            birds.add(new Bird(boardWidth / 8, boardHeight / 2 + 2 * birdHeight * 2, birdBlue)); // Add second bird for two players
            velocitiesY.add(0); // Add initial velocity for the second bird
        }

        pipes = new ArrayList<>(); // Initialize the list of pipes

        // Timer to place pipes periodically
        placePipeTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes(); // Call placePipes method to add new pipes
            }
        });
        placePipeTimer.start(); // Start the timer for placing pipes

        // Main game loop timer
        gameLoop = new Timer(1000 / 60, this); // Timer for frame updates (60 FPS)
        gameLoop.start(); // Start the game loop timer

        // Restart button setup
        restartButton = new JButton("Restart"); // Create a restart button
        restartButton.setFont(new Font("Poppins", Font.PLAIN, 24)); // Set font for the restart button
        restartButton.setBounds(boardWidth / 2 - 75, boardHeight / 2 - 25, 150, 50); // Set position and size of the restart button
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame(); // Call restartGame method when restart button is clicked
            }
        });
        restartButton.setVisible(false); // Hide the restart button initially

        // Menu button setup
        menuButton = new JButton("Menu"); // Create a menu button
        menuButton.setFont(new Font("Poppins", Font.PLAIN, 24)); // Set font for the menu button
        menuButton.setBounds(boardWidth / 2 - 75, boardHeight / 2 + 35, 150, 50); // Set position and size of the menu button
        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.showHomePage(); // Call showHomePage method of the main app when menu button is clicked
            }
        });
        menuButton.setVisible(false); // Hide the menu button initially

        this.setLayout(null); // Set layout to null for absolute positioning
        this.add(restartButton); // Add restart button to the panel
        this.add(menuButton); // Add menu button to the panel
    }

    // Method to place pipes at random heights
    void placePipes() {
        int randomPipeY = (int) (pipeY - pipeHeight / 4 - Math.random() * (pipeHeight / 2)); // Generate random y-coordinate for the top pipe
        int openingSpace = boardHeight / 4; // Set the opening space between the top and bottom pipes

        Pipe topPipe = new Pipe(topPipeImg); // Create a new top pipe
        topPipe.y = randomPipeY; // Set the y-coordinate of the top pipe
        pipes.add(topPipe); // Add the top pipe to the list of pipes

        Pipe bottomPipe = new Pipe(bottomPipeImg); // Create a new bottom pipe
        bottomPipe.y = topPipe.y + pipeHeight + openingSpace; // Set the y-coordinate of the bottom pipe
        pipes.add(bottomPipe); // Add the bottom pipe to the list of pipes
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // Call the superclass's paintComponent method
        draw(g); // Call the draw method to render the game
    }

    // Method to draw the game components
    public void draw(Graphics g) {
        // Draw background
        g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null); // Draw the background image

        // Draw birds
        for (Bird bird : birds) { // Loop through each bird
            g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null); // Draw each bird
        }

        // Draw pipes
        for (Pipe pipe : pipes) {
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        // Draw the score and high score on the screen
        g.setColor(Color.white); // Set the color to white for drawing text
        g.setFont(new Font("Poppins", Font.PLAIN, 32)); // Set the font for the score text
        g.drawString("Score: " + (int) score, 10, 35); // Draw the current score at the top left
        g.drawString("High Score: " + app.highScore, boardWidth - 250, 35); // Draw the high score at the top right

        // Draw the game over message if the game is over
        if (gameOver) {
            g.drawString("Game Over", boardWidth / 2 - 75, boardHeight / 2 - 50); // Draw the game over message
            restartButton.setVisible(true); // Show the restart button
            menuButton.setVisible(true); // Show the menu button
        }
    }

    // Method to move birds and pipes, and check for collisions
    public void move() {
        for (int i = 0; i < birds.size(); i++) { // Loop through each bird
            Bird bird = birds.get(i); // Get the current bird
            int velocityY = velocitiesY.get(i); // Get the current vertical velocity of the bird

            // Bird movement
            velocityY += gravity; // Apply gravity to the vertical velocity
            bird.y += velocityY; // Update the bird's y-coordinate
            bird.y = Math.max(bird.y, 0); // Prevent the bird from going above the canvas
            velocitiesY.set(i, velocityY); // Update the vertical velocity in the list

            // Check for out-of-bounds
            if (bird.y > boardHeight || bird.y < 0) { // If the bird is out of bounds
                stopGame(); // Stop the game
            }

            // Pipe movement
            for (int j = 0; j < pipes.size(); j++) { // Loop through each pipe
                Pipe pipe = pipes.get(j); // Get the current pipe
                pipe.x += velocityX; // Move the pipe to the left

                // Check if the bird passed the pipe
                if (!pipe.passed && bird.x > pipe.x + pipe.width) { // If the bird passed the pipe
                    score += 0.5; // Increase the score
                    pipe.passed = true; // Mark the pipe as passed
                }

                // Check for collision
                if (collision(bird, pipe)) { // If there is a collision between the bird and the pipe
                    stopGame(); // Stop the game
                }
            }
        }
    }

    // Method to check for collisions between birds and pipes
    boolean collision(Bird a, Pipe b) {
        return a.x < b.x + b.width &&   // a's top left corner doesn't reach b's top right corner
            a.x + a.width > b.x &&   // a's top right corner passes b's top left corner
            a.y < b.y + b.height &&  // a's top left corner doesn't reach b's bottom left corner
            a.y + a.height > b.y;    // a's bottom left corner passes b's top left corner
    }

    // Method called when an action event is performed
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) { // If the game is not over
            move(); // Move game components
            repaint(); // Repaint the game panel
        }
    }

    // Method called when a key is pressed
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) { // If the space bar is pressed
            velocitiesY.set(0, 10); // Player 1 jump (space bar), set the jump intensity

            if (gameOver) { // If the game is over
                restartGame(); // Restart the game
            }
        } else if (e.getKeyCode() == KeyEvent.VK_W && numberOfPlayers == 2) { // If 'W' key is pressed and there are 2 players
            velocitiesY.set(1, 10); // Player 2 jump (W key), set the jump intensity

            if (gameOver) { // If the game is over
                restartGame(); // Restart the game
            }
        }
    }

    // Unused keyTyped method required by the KeyListener interface
    @Override
    public void keyTyped(KeyEvent e) {}

    // Unused keyReleased method required by the KeyListener interface
    @Override
    public void keyReleased(KeyEvent e) {}

    // Method to stop the game
    private void stopGame() {
        placePipeTimer.stop(); // Stop the timer for placing pipes
        gameLoop.stop(); // Stop the main game loop timer
        gameOver = true; // Set the game over flag to true
        if (score > app.getHighScore()) { // If the current score is higher than the high score
            app.setHighScore((int) score); // Set the new high score
        }
        restartButton.setVisible(true); // Show the restart button
        menuButton.setVisible(true); // Show the menu button
        repaint(); // Repaint the game panel
    }

    // Method to restart the game
    private void restartGame() {
        for (int i = 0; i < birds.size(); i++) { // Loop through each bird
            Bird bird = birds.get(i); // Get the current bird
            bird.y = boardWidth / 2 + i * birdHeight * 2; // Reset the bird's y-coordinate
            velocitiesY.set(i, 0); // Reset the bird's vertical velocity
        }
        pipes.clear(); // Clear the list of pipes
        gameOver = false; // Set the game over flag to false
        score = 0; // Reset the score
        restartButton.setVisible(false); // Hide the restart button
        menuButton.setVisible(false); // Hide the menu button
        placePipeTimer.start(); // Restart the timer for placing pipes
        gameLoop.start(); // Restart the main game loop timer
        requestFocusInWindow(); // Ensure the game panel regains focus for key events
    }
}