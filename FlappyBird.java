import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 800;
    int boardHeight = 640;

    //images
    Image backgroundImg;
    Image birdImg;
    Image topPipeImg;
    Image bottomPipeImg;

    //bird class
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

    //pipe class
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 64;  //scaled by 1/6
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

    //game logic
    ArrayList<Bird> birds;
    ArrayList<Integer> velocitiesY;
    int velocityX = -4; // move pipes to the left speed (simulates bird moving right)
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

    FlappyBird(int numberOfPlayers, App app) {
        this.numberOfPlayers = numberOfPlayers;
        this.app = app;

        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);
        addKeyListener(this);

        //load images
        backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

        //initialize birds and velocities
        birds = new ArrayList<>();
        velocitiesY = new ArrayList<>();

        for (int i = 0; i < numberOfPlayers; i++) {
            birds.add(new Bird(boardWidth / 8, boardHeight / 2 - birdHeight / 2, birdImg));
            velocitiesY.add(0);
        }

        pipes = new ArrayList<>();

        //place pipes timer
        placePipeTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });
        placePipeTimer.start();

        //game timer
        gameLoop = new Timer(1000 / 60, this); //how long it takes to start timer, milliseconds gone between frames 
        gameLoop.start();

        //restart button
        restartButton = new JButton("Restart");
        restartButton.setFont(new Font("Arial", Font.PLAIN, 24));
        restartButton.setBounds(boardWidth / 2 - 75, boardHeight / 2 - 25, 150, 50);
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });
        restartButton.setVisible(false);
        this.setLayout(null);
        this.add(restartButton);
    }

    void placePipes() {
        int randomPipeY = (int) (-pipeHeight / 2 - Math.random() * (pipeHeight / 2));
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
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if (gameOver) {
            g.drawString("Game Over", boardWidth / 2 - 90, boardHeight / 2 - 100);
        } else {
            g.drawString("Score: " + (int) score, 20, 40);
        }
    }

    public void move() {
        if (!gameOver) {
            for (int i = 0; i < numberOfPlayers; i++) {
                int newY = birds.get(i).y + velocitiesY.get(i);
                velocitiesY.set(i, velocitiesY.get(i) + gravity);
                birds.get(i).y = newY;
            }

            for (Pipe pipe : pipes) {
                pipe.x += velocityX;
            }

            // Remove pipes that have gone off screen
            pipes.removeIf(pipe -> pipe.x + pipe.width < 0);

            // Check for collisions
            for (Bird bird : birds) {
                for (Pipe pipe : pipes) {
                    if (bird.x < pipe.x + pipe.width &&
                        bird.x + bird.width > pipe.x &&
                        bird.y < pipe.y + pipe.height &&
                        bird.y + bird.height > pipe.y) {
                        gameOver = true;
                        placePipeTimer.stop();
                        restartButton.setVisible(true);
                    }
                }
            }

            // Update score
            for (Pipe pipe : pipes) {
                if (pipe.x + pipe.width < birds.get(0).x && !pipe.passed) {
                    pipe.passed = true;
                    score++;
                }
            }

            repaint();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_UP) {
            for (int i = 0; i < numberOfPlayers; i++) {
                velocitiesY.set(i, -10);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    public void restartGame() {
        gameOver = false;
        score = 0;
        pipes.clear();
        velocitiesY.clear();
        for (int i = 0; i < numberOfPlayers; i++) {
            birds.set(i, new Bird(boardWidth / 8, boardHeight / 2 - birdHeight / 2, birdImg));
            velocitiesY.add(0);
        }
        placePipeTimer.start();
        restartButton.setVisible(false);
    }
}
