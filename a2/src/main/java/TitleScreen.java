import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class TitleScreen {
    private Scene titleScreen;

    TitleScreen() {
        BorderPane rootTitle = new BorderPane();
        titleScreen = new Scene(rootTitle, 800, 600, Color.LIGHTGRAY);
        Image titleImage = new Image("images/logo.png");
        ImageView titleView = new ImageView(titleImage);
        rootTitle.setTop(titleView);
        BorderPane.setAlignment(titleView, Pos.TOP_CENTER);
        BorderPane.setMargin(titleView, new Insets(15,0,0,0));

        Label instructions = new Label("Instructions");
        instructions.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 48));

        Label start = new Label("ENTER - Start Game");
        start.setFont(Font.font("Comic Sans MS", 16));

        Label movement = new Label("A or <- , D or -> - Move ship left or right");
        movement.setFont(Font.font("Comic Sans MS", 16));

        Label fire = new Label("SPACE - Fire!");
        fire.setFont(Font.font("Comic Sans MS", 16));

        Label quit = new Label("Q - Quit Game");
        quit.setFont(Font.font("Comic Sans MS", 16));

        Label levels = new Label("1 or 2 or 3 - Start Game at a specific level");
        levels.setFont(Font.font("Comic Sans MS", 16));

        VBox info = new VBox(instructions, start, movement, fire, quit, levels);
        VBox.setMargin(start, new Insets(48,0,0,0));
        VBox.setMargin(movement, new Insets(16,0,0,0));
        VBox.setMargin(fire, new Insets(16,0,0,0));
        VBox.setMargin(quit, new Insets(16,0,0,0));
        VBox.setMargin(levels, new Insets(16,0,0,0));
        info.setAlignment(Pos.TOP_CENTER);
        rootTitle.setCenter(info);
        BorderPane.setMargin(info, new Insets(20,0,0,0));

        Label credit = new Label("Implemented by An Nguyen 20726636 for CS349, University of Waterloo, S20");
        credit.setFont(Font.font("Comic Sans MS", 11));
        rootTitle.setBottom(credit);
        BorderPane.setAlignment(credit, Pos.BOTTOM_CENTER);
    }

    public Scene getScene() {
        return this.titleScreen;
    }
}
