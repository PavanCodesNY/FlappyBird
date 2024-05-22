import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePage extends JPanel {
    private App app;
    private JLabel highScoreLabel;
    private int highScore;

    public HomePage(App app, int highScore) {
        this.app = app;
        this.highScore = highScore;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel titleLabel = new JLabel("Flappy Bird");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton singlePlayerButton = new JButton("Single Player");
        singlePlayerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        singlePlayerButton.setFont(new Font("Arial", Font.PLAIN, 24));
        singlePlayerButton.setPreferredSize(new Dimension(200, 50));
        singlePlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.startCountdown(1);
            }
        });

        JButton twoPlayerButton = new JButton("Two Players");
        twoPlayerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        twoPlayerButton.setFont(new Font("Arial", Font.PLAIN, 24));
        twoPlayerButton.setPreferredSize(new Dimension(200, 50));
        twoPlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.startCountdown(2);
            }
        });

        highScoreLabel = new JLabel("High Score: " + highScore);
        highScoreLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        highScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createVerticalStrut(50)); // Spacer
        add(titleLabel);
        add(Box.createVerticalStrut(30)); // Spacer
        add(singlePlayerButton);
        add(Box.createVerticalStrut(20)); // Spacer
        add(twoPlayerButton);
        add(Box.createVerticalStrut(30)); // Spacer
        add(highScoreLabel);
    }
}
