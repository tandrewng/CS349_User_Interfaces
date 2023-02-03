import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Indicators {
    private HBox indicators;
    private Label lblScore;
    private Label lblLives;
    private Label lblLevel;

    Indicators(int score, int lives, int level) {
        lblScore = new Label("Score: " + score);
        lblScore.setFont(new Font("Comic Sans MS", 20));
        lblScore.setTextFill(Color.WHITE);

        lblLives = new Label("Lives: " + lives);
        lblLives.setFont(new Font("Comic Sans MS", 20));
        lblLives.setTextFill(Color.WHITE);

        lblLevel = new Label("Level: " + level);
        lblLevel.setFont(new Font("Comic Sans MS", 20));
        lblLevel.setTextFill(Color.WHITE);

        HBox rightIndicators = new HBox(30);
        rightIndicators.getChildren().addAll(lblLives, lblLevel);

        indicators = new HBox(500);
        indicators.setAlignment(Pos.TOP_CENTER);
        indicators.getChildren().addAll(lblScore, rightIndicators);

    }

    public void update(int score, int lives, int level) {
        lblScore.setText("Score: " + score);
        lblLives.setText("Lives: " + lives);
        lblLevel.setText("Level: " + level);
    }

    public HBox getIndicators() {
        return this.indicators;
    }
}
