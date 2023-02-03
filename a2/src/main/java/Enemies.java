import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.ImageView;

public class Enemies {
    private static final double SCREEN_WIDTH = 800.0;
    private static final double SCREEN_HEIGHT = 600.0;
    private int ROWS = 5;
    private int COLLUMNS = 10;
    private static final double ENEMY_VERTICAL_SPEED = 10.0;

    private Group enemies;
    private enum DIRECTION {LEFT, RIGHT};
    private DIRECTION enemiesDirection;
    private boolean atBottom;

    Enemies() {
        enemies = new Group();
        atBottom = false;

        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLLUMNS; ++col) {
                if (row == 0) {
                    enemies.getChildren().add(new Enemy(3, row, col).getEnemy());
                } else if (row == 1 || row == 2) {
                    enemies.getChildren().add(new Enemy(2, row, col).getEnemy());
                } else {
                    enemies.getChildren().add(new Enemy(1, row, col).getEnemy());
                }
            }
        }
    }

    public void moveEnemies(double speed) {
        for (Node enemy: enemies.getChildren()) {
            ImageView e = (ImageView) enemy;
            if (enemiesDirection == DIRECTION.RIGHT) {
                if (e.getX() + speed <= SCREEN_WIDTH - 45){
                    e.setX(e.getX() + speed);
                } else {
                    enemiesDirection = DIRECTION.LEFT;
                    for (Node child: enemies.getChildren()) {
                        ImageView c = (ImageView) child;
                        c.setY(c.getY() + ENEMY_VERTICAL_SPEED);
                    }
                }
            } else {
                if (e.getX() - speed >= 0){
                    e.setX(e.getX() - speed);
                } else {
                    enemiesDirection = DIRECTION.RIGHT;
                    for (Node child: enemies.getChildren()) {
                        ImageView c = (ImageView) child;
                        c.setY(c.getY() + ENEMY_VERTICAL_SPEED);
                    }
                }
            }
        }
    }

    public boolean hitBottomRow() {
        for (Node enemy: enemies.getChildren()) {
            ImageView e = (ImageView) enemy;
            if (e.getY() > SCREEN_HEIGHT - 65) {
                atBottom = true;
                break;
            }
        }
        return atBottom;
    }

    public void removeEnemy(int id) {
        enemies.getChildren().remove(id);
    }

    public Group getEnemies() {
        return enemies;
    }
}
