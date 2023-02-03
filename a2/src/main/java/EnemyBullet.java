import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class EnemyBullet {
    private ImageView bullet;

    EnemyBullet(int type, double xPos, double yPos) {
        String loc = "images/bullet" + type + ".png";
        Image bulletImage = new Image(loc, 10, 30, true, false);
        bullet = new ImageView(bulletImage);
        bullet.setX(xPos);
        bullet.setY(yPos);
    }

    public ImageView getBullet() {
        return bullet;
    }
}
