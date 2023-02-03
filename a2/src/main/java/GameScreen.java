import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import java.util.ArrayList;

public class GameScreen {
    private static final double ENEMY_LEVEL_1_SPEED = 0.5;
    private static final double ENEMY_LEVEL_2_SPEED = 1.0;
    private static final double ENEMY_LEVEL_3_SPEED = 1.5;
    private static final int FIRE_RATE = 500;
    private static final double BULLET_SPEED = 2.0;

    private Scene gameScreen;
    private static double enemySpeed;
    private static boolean gameOver;
    private AnimationTimer timer;
    private enum DIRECTION {LEFT, RIGHT, NONE};
    private DIRECTION playerDirection;
    private long lastPlayerShot = System.currentTimeMillis();
    private long lastEnemyShot = System.currentTimeMillis();
    private AudioClip explosion;
    private AudioClip invaderkilled;
    private AudioClip shoot;

    private int level;
    private int runScore;
    private int score;
    private int lives;

    private Group rootGame;
    private Indicators indicators;
    private Player player;
    private Enemies enemies;
    private int numEnemies;

    private ArrayList<PlayerBullet> playerBullets;
    private ArrayList<EnemyBullet> enemyBullets;

    GameScreen() {
        rootGame = new Group();
        gameScreen = new Scene(rootGame, 800, 600, Color.BLACK);
        rootGame.requestFocus();
    }

    public void init (int l, int s){
        rootGame.getChildren().clear();
        level = l;
        playerDirection = DIRECTION.NONE;
        runScore = 0;
        score = s;
        lives = 3;
        gameOver = false;
        numEnemies = 50;
        explosion = new AudioClip(getClass().getResource("sounds/explosion.wav").toString());
        invaderkilled = new AudioClip(getClass().getResource("sounds/invaderkilled.wav").toString());
        shoot = new AudioClip(getClass().getResource("sounds/shoot.wav").toString());

        switch (level) {
            case 1 -> enemySpeed = ENEMY_LEVEL_1_SPEED;
            case 2 -> enemySpeed = ENEMY_LEVEL_2_SPEED;
            case 3 -> enemySpeed = ENEMY_LEVEL_3_SPEED;
        }

        indicators = new Indicators(score, lives, level);
        player = new Player();
        enemies = new Enemies();

        rootGame.getChildren().addAll(
                indicators.getIndicators(),
                player.getPlayer(),
                enemies.getEnemies()
        );

        playerBullets = new ArrayList<PlayerBullet>();
        enemyBullets = new ArrayList<EnemyBullet>();

        startLevel();

        rootGame.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D) {
                playerDirection = DIRECTION.RIGHT;
            } else if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A) {
                playerDirection = DIRECTION.LEFT;
            } else if (event.getCode() == KeyCode.SPACE){
                shoot();
            }
        });

        rootGame.setOnKeyReleased(event -> {
            if(event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D ||
                    event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A) {
                playerDirection = DIRECTION.NONE;
            }
        });
    }

    private void startLevel() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                moveEnemies();
                movePlayer();

                enemyShoot();
                moveEnemyBullets();
                movePlayerBullets();

                enemyHitCheck();
                playerHitCheck();

                if (gameOver) {
                    timer.stop();
                    gameOverHandler();
                }
            }
        };
        timer.start();
    }

    private boolean collided(Node node, Node other) {
        return node.getBoundsInParent().intersects(other.getBoundsInParent());
    }

    private void movePlayer() {
        if (playerDirection == DIRECTION.RIGHT) {
            player.moveRight();
        } else if (playerDirection == DIRECTION.LEFT) {
            player.moveLeft();
        }
    }

    private void shoot() {
        if (System.currentTimeMillis() - FIRE_RATE > lastPlayerShot) {
            lastPlayerShot = System.currentTimeMillis();
            PlayerBullet pb = new PlayerBullet(player.getPosition());
            playerBullets.add(pb);
            rootGame.getChildren().add(pb.getBullet());
            shoot.play();
        }
    }

    private void movePlayerBullets() {
        PlayerBullet deadPB = null;
        for (PlayerBullet pb: playerBullets) {
            if (pb.getBullet().getY() < 0) {
                deadPB = pb;
            } else {
                pb.getBullet().setY(pb.getBullet().getY() - BULLET_SPEED);
            }
        }
        if (deadPB != null) deletePlayerBullet(deadPB);
    }

    private void deletePlayerBullet(PlayerBullet bullet) {
        playerBullets.remove(bullet);
        rootGame.getChildren().remove(bullet.getBullet());
    }

    private void respawn() {
        player = new Player();
        rootGame.getChildren().add(player.getPlayer());
    }

    private void playerHitCheck() {
        EnemyBullet deadEB = null;
        for (EnemyBullet eb : enemyBullets) {
            if (collided(eb.getBullet(), player.getPlayer())) {
                loseLife();
                deadEB = eb;
                rootGame.getChildren().remove(player.getPlayer());
                explosion.play();
                updateScore(0);
                if (lives > 0) {
                    respawn();
                }
            }
        }
        if (deadEB != null) {
            deleteEnemyBullet(deadEB);
        }
    }

    private void moveEnemies() {
        enemies.moveEnemies(enemySpeed);
        gameOver = enemies.hitBottomRow();
    }

    private void enemyShoot() {
        int eFireRate = (int) (Math.random() * 50000);
        if (System.currentTimeMillis() - eFireRate > lastEnemyShot) {
            lastEnemyShot = System.currentTimeMillis();

            int rand = (int) (Math.random() * numEnemies);
            int shooterType = (int) (Math.random() * 3) + 1;

            double xPos = enemies.getEnemies().getChildren().get(rand).getBoundsInParent().getCenterX();
            double yPos = enemies.getEnemies().getChildren().get(rand).getBoundsInParent().getMaxY();

            EnemyBullet eb = new EnemyBullet(shooterType, xPos, yPos);
            enemyBullets.add(eb);
            rootGame.getChildren().add(eb.getBullet());
        }
    }

    private void moveEnemyBullets() {
        EnemyBullet deadEB = null;
        for (EnemyBullet eb: enemyBullets) {
            if (eb.getBullet().getY() < 0) {
                deadEB = eb;
            } else {
                eb.getBullet().setY(eb.getBullet().getY() + BULLET_SPEED);
            }
        }
        if (deadEB != null) deleteEnemyBullet(deadEB);
    }

    private void deleteEnemyBullet(EnemyBullet bullet) {
        enemyBullets.remove(bullet);
        rootGame.getChildren().remove(bullet.getBullet());
    }

    private void deleteEnemy(int id) {
        numEnemies--;
        enemies.removeEnemy(id);
        enemySpeed += 0.02;

        if (numEnemies == 0) {
            gameOver = true;
        }
    }

    private void enemyHitCheck() {
        PlayerBullet deadPB = null;
        for (PlayerBullet pb : playerBullets) {
            for (int i = 0; i < numEnemies; ++i) {
                Node enemy = enemies.getEnemies().getChildren().get(i);
                if (collided(enemy, pb.getBullet())) {
                    deleteEnemy(i);
                    invaderkilled.play();
                    updateScore(level);
                    deadPB = pb;
                }
            }
        }
        if (deadPB != null) {
            deletePlayerBullet(deadPB);
        }
    }

    private void loseLife() {
        lives--;
        indicators.update(score, lives, level);

        if (lives == 0) {
            gameOver = true;
        }

    }

    private void updateScore (int l) {
        // gain points based on level
        switch (l) {
            // lose points if lost life
            case 0 -> runScore -= 10;
            case 1 -> runScore += 10;
            case 2 -> runScore += 15;
            case 3 -> runScore += 30;
        }

        if (runScore > score) {
            score = runScore;
        }

        indicators.update(score, lives, level);
    }

    private void gameOverHandler() {
        boolean won = (numEnemies == 0);

        level = won ? level + 1 : level;
        GameOverPopup gameOverPopup = new GameOverPopup(won, score, level);

        rootGame.getChildren().add(gameOverPopup.getGameOverPopup());
    }

    public boolean gameIsOver() {
        return gameOver;
    }

    public void restart() {
        if (level == 4) {
            level = 1;
        }
        init(level, score);
    }

    public Scene getScene() {
        return this.gameScreen;
    }
}
