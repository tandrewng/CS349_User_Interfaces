import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Enemy {
    private ImageView enemy;

    public Enemy(int type, int row, int col) {
        String loc = "images/enemy" + type + ".png";
        Image enemyImage = new Image(loc, 45, 30, false, false);
        enemy = new ImageView(enemyImage);
        enemy.setX(col * 55 + 80);
        enemy.setY(row * 35 + 40);
    }

    public ImageView getEnemy() {
        return enemy;
    }
}
