import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class SpaceInvaders extends Application {

    Scene titleScreen, gameScreen;
    enum SCENES {TITLE, GAME};

    @Override
    public void start(Stage stage) {
        stage.setTitle("Space Invaders");

        titleScreen = new TitleScreen().getScene();
        GameScreen game = new GameScreen();
        gameScreen = game.getScene();

        // pressing Enter to gameScreen1, Q to exit, 1 to level 1, 2 to level 2, 3 to level 3
        titleScreen.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.DIGIT1) {
                game.init(1, 0);
                setScene(stage, SCENES.GAME);
            } else if (event.getCode() == KeyCode.Q){
                System.exit(0);
            } else if (event.getCode() == KeyCode.DIGIT2){
                game.init(2, 0);
                setScene(stage, SCENES.GAME);
            } else if (event.getCode() == KeyCode.DIGIT3){
                game.init(3, 0);
                setScene(stage, SCENES.GAME);
            }
        });


        gameScreen.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.Q) {
                System.exit(0);
            } else if(event.getCode() == KeyCode.ENTER && game.gameIsOver()) {
                game.restart();
                setScene(stage, SCENES.GAME);
            }
        });

        // show title scene
        setScene(stage, SCENES.TITLE);
        stage.show();
    }

    void setScene(Stage stage, SCENES scene) {
        switch (scene) {
            case TITLE -> {
                stage.setTitle("Space Invaders");
                stage.setScene(titleScreen);
                stage.setResizable(false);
            }
            case GAME -> {
                stage.setTitle("Space Invaders");
                stage.setScene(gameScreen);
                stage.setResizable(false);
            }
        }
    }
}
