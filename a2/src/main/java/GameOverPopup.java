import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


public class GameOverPopup {
    StackPane gameOverPopup;

    GameOverPopup (boolean won, int score, int level) {
        gameOverPopup = new StackPane();

        Rectangle r = new Rectangle(400, 300, Color.LIGHTGRAY);
        gameOverPopup.getChildren().add(r);

        String result = won ? "YOU WON!" : "YOU LOSE";
        Label lblResult = new Label(result);
        lblResult.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 24));
        Label lblScore = new Label("HIGH SCORE: " + score);
        lblScore.setFont(Font.font("Comic Sans MS", 16));

        String action;
        if (won && level == 4) {
            action = "CLICK ENTER TO RESTART LEVEL 1";
        } else {
            action = "CLICK ENTER TO START LEVEL " + level;
        }
        Label lblAction = new Label(action);
        lblAction.setFont(Font.font("Comic Sans MS", 16));

        Label lblQuit = new Label("PRESS Q TO QUIT");
        lblQuit.setFont(Font.font("Comic Sans MS", 16));

        VBox transitions = new VBox(lblResult, lblScore, lblAction, lblQuit);
        transitions.setAlignment(Pos.CENTER);

        gameOverPopup.getChildren().add(transitions);
        gameOverPopup.setLayoutX(200);
        gameOverPopup.setLayoutY(150);
    }

    public StackPane getGameOverPopup() {
        return gameOverPopup;
    }
}
