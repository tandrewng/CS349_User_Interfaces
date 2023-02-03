import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Player {
    private static final double SCREEN_WIDTH = 800.0;
    private static final double SCREEN_HEIGHT = 600.0;
    private static final double RIGHT_BOUND = SCREEN_WIDTH - 50;
    private static final double PLAYER_SPEED = 3.0;
    private double playerX = RIGHT_BOUND/2;

    private ImageView player;

    Player() {
        Image playerImage = new Image("images/player.png", 50, 30, true, false);
        player = new ImageView(playerImage);
        player.setX(playerX);
        player.setY(SCREEN_HEIGHT - 35);
    }

    public void moveRight() {
        if (playerX < RIGHT_BOUND) {
            playerX += PLAYER_SPEED;
            player.setX(playerX);
        }
    }

    public void moveLeft() {
        if (playerX > 0) {
            playerX -= PLAYER_SPEED;
            player.setX(playerX);
        }
    }

    public double getPosition() {
        return playerX;
    }

    public ImageView getPlayer() {
        return player;
    }
}
