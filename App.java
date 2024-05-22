import javax.swing.*;
import java.awt.*;

public class App {
    private JFrame frame;
    private HomePage homePage;
    private FlappyBird gamePanel;
    private CountdownPanel countdownPanel;
    private int highScore;

    public App() {
        frame = new JFrame("Flappy Bird");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 640);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        highScore = 0; // Initialize high score

        homePage = new HomePage(this, highScore);
        frame.add(homePage);

        frame.setVisible(true);
    }

    public void startCountdown(int numberOfPlayers) {
        frame.getContentPane().removeAll();
        countdownPanel = new CountdownPanel(this, numberOfPlayers);
        frame.add(countdownPanel);
        frame.revalidate();
        frame.repaint();
    }

    public void startGame(int numberOfPlayers) {
        frame.getContentPane().removeAll();
        gamePanel = new FlappyBird(numberOfPlayers, this);
        frame.add(gamePanel);
        frame.revalidate();
        frame.repaint();
        gamePanel.requestFocusInWindow();
    }

    public void endGame(int score) {
        if (score > highScore) {
            highScore = score;
        }
        frame.getContentPane().removeAll();
        homePage = new HomePage(this, highScore);
        frame.add(homePage);
        frame.revalidate();
        frame.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new App();
            }
        });
    }
}
