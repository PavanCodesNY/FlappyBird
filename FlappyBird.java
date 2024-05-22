import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 800;
    int boardHeight = 640;

    // Images
    Image backgroundImg;
    Image birdImg;
    Image topPipeImg;
    Image bottomPipeImg;

    // Bird class
    int birdWidth = 34;
    int birdHeight = 24;

    class Bird {
        int x, y, width, height;
        Image img;

        Bird(int startX, int startY, Image img) {
            this.x = startX;
            this.y = startY;
            this.width = birdWidth;
            this.height = birdHeight;
            this.img = img;
        }
    }

    // Pipe class
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 64;  // Scaled by 1/6
    int pipeHeight = 512;

    class Pipe {
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false;

        Pipe(Image img) {
            this.img = img;
        }
    }

    // Game logic
    ArrayList<Bird> birds;
    ArrayList<Integer> velocitiesY;
    int velocityX = -4; // Move pipes to the left speed (simulates bird moving right)
    int gravity = 1;

    ArrayList<Pipe> pipes;
    Random random = new Random();

    Timer gameLoop;
    Timer placePipeTimer;
    boolean gameOver = false;
    double score = 0;
    int numberOfPlayers;
    App app;
    JButton restartButton;
    JButton menuButton;

    FlappyBird(int numberOfPlayers, App app) {
        this.numberOfPlayers = numberOfPlayers;
        this.app = app;

        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);
        addKeyListener(this);

        // Load images
        backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

        // Initialize birds and velocities
        birds = new ArrayList<>();
        velocitiesY = new ArrayList<>();

        for (int i = 0; i < numberOfPlayers; i++) {
            birds.add(new Bird(boardWidth / 8, boardHeight / 2 + i * birdHeight * 2, birdImg));
            velocitiesY.add(0);
        }

        pipes = new ArrayList<>();

        // Place pipes timer
        placePipeTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });
        placePipeTimer.start();

        // Game timer
        gameLoop = new Timer(1000 / 60, this); // How long it takes to start timer, milliseconds gone between frames 
        gameLoop.start();

        // Restart button
        restartButton = new JButton("Restart");
        restartButton.setFont(new Font("Poppins", Font.PLAIN, 24));
        restartButton.setBounds(boardWidth / 2 - 75, boardHeight / 2 - 25, 150, 50);
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });
        restartButton.setVisible(false);

        // Menu button
        menuButton = new JButton("Menu");
        menuButton.setFont(new Font("Poppins", Font.PLAIN, 24));
        menuButton.setBounds(boardWidth / 2 - 75, boardHeight / 2 + 35, 150, 50);
        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.showHomePage(); // Change this to a new method in App to show the home page
            }
        });
        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // app.endGame((int) score);
            }
        });
        menuButton.setVisible(false);

        this.setLayout(null);
        this.add(restartButton);
        this.add(menuButton);
    }

    void placePipes() {
        int randomPipeY = (int) (pipeY - pipeHeight / 4 - Math.random() * (pipeHeight / 2));
        int openingSpace = boardHeight / 4;

        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottomPipeImg);
        bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
        pipes.add(bottomPipe);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // Draw background
        g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null);

        // Draw birds
        for (Bird bird : birds) {
            g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);
        }

        // Draw pipes
        for (Pipe pipe : pipes) {
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        // Draw score
        g.setColor(Color.white);
        g.setFont(new Font("Poppins", Font.PLAIN, 32));
        g.drawString("Score: " + (int) score, 10, 35);
        g.drawString("High Score: " + app.highScore, boardWidth - 250, 35);

        if (gameOver) {
            g.drawString("Game Over", boardWidth / 2 - 75, boardHeight / 2 - 50);
            restartButton.setVisible(true);
            menuButton.setVisible(true);
        }
    }

    public void move() {
        for (int i = 0; i < birds.size(); i++) {
            Bird bird = birds.get(i);
            int velocityY = velocitiesY.get(i);
    
            // Bird movement
            velocityY += gravity;
            bird.y += velocityY;
            bird.y = Math.max(bird.y, 0); // Prevent the bird from going above the canvas
            velocitiesY.set(i, velocityY);
    
            // Check for out-of-bounds
            if (bird.y > boardHeight || bird.y < 0) {
                stopGame();
            }
    
            // Pipe movement
            for (int j = 0; j < pipes.size(); j++) {
                Pipe pipe = pipes.get(j);
                pipe.x += velocityX;
    
                // Check if the bird passed the pipe
                if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                    score += 0.5; // 0.5 because there are 2 pipes! so 0.5 * 2 = 1, 1 for each set of pipes
                    pipe.passed = true;
                }
    
                // Check for collision
                if (collision(bird, pipe)) {
                    stopGame();
                }
            }
        }
    }
    

    boolean collision(Bird a, Pipe b) {
        return a.x < b.x + b.width &&   // a's top left corner doesn't reach b's top right corner
               a.x + a.width > b.x &&   // a's top right corner passes b's top left corner
               a.y < b.y + b.height &&  // a's top left corner doesn't reach b's bottom left corner
               a.y + a.height > b.y;    // a's bottom left corner passes b's top left corner
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            move();
            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocitiesY.set(0, -9); // Player 1 jump

            if (gameOver) {
                restartGame();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_W && numberOfPlayers == 2) {
            velocitiesY.set(1, -9); // Player 2 jump (W key for player 2)

            if (gameOver) {
                restartGame();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    private void stopGame() {
        placePipeTimer.stop();
        gameLoop.stop();
        gameOver = true;
        if (score > app.getHighScore()) {
            app.setHighScore((int) score);
        }
        restartButton.setVisible(true);
        menuButton.setVisible(true);
        repaint();
    }
    
    


    private void restartGame() {
        for (int i = 0; i < birds.size(); i++) {
            Bird bird = birds.get(i);
            bird.y = boardWidth / 2 + i * birdHeight * 2;
            velocitiesY.set(i, 0);
        }
        pipes.clear();
        gameOver = false;
        score = 0;
        restartButton.setVisible(false);
        menuButton.setVisible(false);
        placePipeTimer.start();
        gameLoop.start();
        requestFocusInWindow(); // Ensure the game panel regains focus for key events
    }    
}
