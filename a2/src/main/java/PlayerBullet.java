import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PlayerBullet {
    private static final double BULLET_HEIGHT = 565;
    private ImageView bullet;

    PlayerBullet(double playerPos) {
        double xPos = playerPos + 22;

        Image bulletImage = new Image("images/player_bullet.png", 5, 15, false, false);
        bullet = new ImageView(bulletImage);
        bullet.setX(xPos);
        bullet.setY(BULLET_HEIGHT);
    }

    public ImageView getBullet() {
        return bullet;
    }
}