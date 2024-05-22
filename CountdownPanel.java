import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CountdownPanel extends JPanel {
    private JLabel countdownLabel;
    private int countdownValue;
    private Timer countdownTimer;
    private App app;
    private int numberOfPlayers;

    public CountdownPanel(App app, int numberOfPlayers) {
        this.app = app;
        this.numberOfPlayers = numberOfPlayers;

        setLayout(new BorderLayout());

        countdownValue = 3;
        countdownLabel = new JLabel(String.valueOf(countdownValue), SwingConstants.CENTER);
        countdownLabel.setFont(new Font("Arial", Font.BOLD, 72));
        add(countdownLabel, BorderLayout.CENTER);

        countdownTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                countdownValue--;
                if (countdownValue <= 0) {
                    countdownTimer.stop();
                    app.startGame(numberOfPlayers);
                } else {
                    countdownLabel.setText(String.valueOf(countdownValue));
                }
            }
        });
        countdownTimer.start();
    }
}
