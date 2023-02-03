import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Ragdoll extends Application {
    enum DOLL {
        HUMAN,
        SNAKE,
        TREE
    }
    public static DOLL doll = DOLL.HUMAN;
    public final double screen_width = 1024.0;
    public final double screen_height = 768.0;
    public Torso torso;
    @Override
    public void start(Stage stage) {
        stage.setTitle("Ragdoll");

        VBox root = new VBox();

        //region Menu
        // create a menu
        Menu m = new Menu("File");

        // create a menu bar
        MenuBar mb = new MenuBar();

        // create menu items
        MenuItem human = new MenuItem("Human");
        human.setOnAction(e -> {
            doll = DOLL.HUMAN;
            root.getChildren().clear();
            root.getChildren().addAll(mb);
            Pane canvas = create();
            canvas.prefHeight(screen_height);
            canvas.prefWidth(screen_width);
            root.getChildren().addAll(canvas);
        });
        MenuItem snake = new MenuItem("Snake");
        snake.setOnAction(e -> {
            doll = DOLL.SNAKE;
            root.getChildren().clear();
            root.getChildren().addAll(mb);
            Pane canvas = create();
            canvas.prefHeight(screen_height);
            canvas.prefWidth(screen_width);
            root.getChildren().addAll(canvas);
        });
        MenuItem tree = new MenuItem("Tree");
        tree.setOnAction(e -> {
            doll = DOLL.TREE;
            root.getChildren().clear();
            root.getChildren().addAll(mb);
            Pane canvas = create();
            canvas.prefHeight(screen_height);
            canvas.prefWidth(screen_width);
            root.getChildren().addAll(canvas);
        });
        MenuItem reset = new MenuItem("Reset (Ctrl-R)");
        reset.setOnAction(e -> {
            torso.reset();
        });
        SeparatorMenuItem sep = new SeparatorMenuItem();
        MenuItem quit = new MenuItem("Quit (Ctrl-Q)");
        quit.setOnAction(e -> {
            System.exit(0);
        });

        // add menu items to menu
        m.getItems().add(human);
        m.getItems().add(snake);
        m.getItems().add(tree);

        m.getItems().add(reset);
        m.getItems().add(sep);
        m.getItems().add(quit);

        // add menu to menu bar
        mb.getMenus().add(m);

        root.getChildren().addAll(mb);

        //endregion Menu

        Pane canvas = create();

        canvas.prefHeight(screen_height);
        canvas.prefWidth(screen_width);
        root.getChildren().addAll(canvas);

        // key handlers should be defined at the scene level
        // individual nodes can also have their own mouse handlers
        root.setOnMousePressed(event -> {
            Sprite sprite = getSelectedSprite(torso);
            if (sprite != null) {
                System.out.println("CANVAS SELECTED " + sprite);
            }
        });

        Scene scene = new Scene(root, screen_width, screen_height);

        scene.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.R) {
                torso.reset();
            }

            if(event.getCode() == KeyCode.Q) {
                System.exit(0);
            }
        });

        // show the scene
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setWidth(screen_width);
        stage.setHeight(screen_height);
        stage.show();
    }

    // find out which node has been selected
    // relies on nodes implementing their own STATE flag
    private Sprite getSelectedSprite(Sprite root) {
        if (root.state == Sprite.STATE.SELECTED) {
            return root;
        }
        for (Sprite child : root.children) {
            Sprite sprite = getSelectedSprite(child);
            if (sprite != null) return sprite;
        }
        return null;
    }

    private Pane create() {
        if (doll == DOLL.HUMAN) {
            // create nodes at origin
            torso = new Torso(-30, -40, 60, 80);
            Head head = new Head(0, 0, 14, 15, torso);
            UpperArm leftUpperArm = new UpperArm(0, 0, 20, 10, true, torso);
            LowerArm leftLowerArm = new LowerArm(0, 0, 20, 10, true, leftUpperArm);
            Hand leftHand = new Hand(0, 0, 15, 6, true, leftLowerArm);
            UpperArm rightUpperArm = new UpperArm(0, 0, 20, 10, false, torso);
            LowerArm rightLowerArm = new LowerArm(0, 0, 20, 10, false, leftUpperArm);
            Hand rightHand = new Hand(0, 0, 15, 6, false, leftLowerArm);
            UpperLeg leftUpperLeg = new UpperLeg(0, 0, 10, 20, true, torso);
            LowerLeg leftLowerLeg = new LowerLeg(-0, 0, 10, 20, true, leftUpperLeg);
            Foot leftFoot = new Foot(0, 0, 15, 6, true, leftLowerLeg);
            UpperLeg rightUpperLeg = new UpperLeg(0, 0, 10, 20, false, torso);
            LowerLeg rightLowerLeg = new LowerLeg(0, 0, 10, 20, false, rightUpperLeg);
            Foot rightFoot = new Foot(0, 0, 15, 6, false, rightLowerLeg);
            torso.children.add(head);
            torso.children.add(leftUpperArm);
            torso.children.add(rightUpperArm);
            torso.children.add(leftUpperLeg);
            torso.children.add(rightUpperLeg);

            leftUpperArm.children.add(leftLowerArm);
            leftLowerArm.children.add(leftHand);

            rightUpperArm.children.add(rightLowerArm);
            rightLowerArm.children.add(rightHand);

            leftUpperLeg.children.add(leftLowerLeg);
            leftLowerLeg.children.add(leftFoot);

            rightUpperLeg.children.add(rightLowerLeg);
            rightLowerLeg.children.add(rightFoot);

            // move to starting point
            torso.translate(screen_width/2.0, screen_height/2.0-40);
            head.translate(0, -55);
            leftUpperArm.translate(-50, -25);
            leftLowerArm.translate(-40, 0);
            leftHand.translate(-35, 0);
            rightUpperArm.translate(50, -25);
            rightLowerArm.translate(40, 0);
            rightHand.translate(35, 0);
            leftUpperLeg.translate(-20, 60);
            leftLowerLeg.translate(0, 40);
            leftFoot.translate(-8, 25);
            rightUpperLeg.translate(20, 60);
            rightLowerLeg.translate(0, 40);
            rightFoot.translate(8, 25);

             return new Pane(torso, head, leftUpperArm, leftLowerArm, leftHand, rightUpperArm, rightLowerArm, rightHand, leftUpperLeg,
                     leftLowerLeg, leftFoot, rightUpperLeg, rightLowerLeg, rightFoot);
        } else if (doll == DOLL.SNAKE) {
            // create nodes at origin
            torso = new Torso(-20, -10, 40, 20);
            UpperArm head = new UpperArm(0, 0, 20, 10, true, torso);
            UpperArm tail = new UpperArm(0, 0, 20, 10, false, torso);
            torso.children.add(head);
            torso.children.add(tail);

            // move to starting point
            torso.translate(screen_width/2.0, screen_height/2.0-40);
            head.translate(40, 0);
            tail.translate(-40, 0);

            return new Pane(torso, head, tail);
        } else {
            // create nodes at origin
            torso = new Torso(-20, -40, 40, 80);
            UpperArm lowerLeft = new UpperArm(0, 0, 5, 20, true, torso);
            UpperArm lowerRight = new UpperArm(0, 0, 5, 20, false, torso);
            LowerArm farLeft = new LowerArm(0, 0, 5, 20, false, lowerLeft);
            LowerArm midLeft = new LowerArm(0, 0, 5, 20, false, lowerLeft);
            LowerArm midRight = new LowerArm(0, 0, 5, 20, false, lowerRight);
            LowerArm farRight = new LowerArm(0, 0, 5, 20, false, lowerRight);
            torso.children.add(lowerLeft);
            torso.children.add(lowerRight);
            lowerLeft.children.add(farLeft);
            lowerLeft.children.add(midLeft);
            lowerRight.children.add(midRight);
            lowerRight.children.add(farRight);

            // need to add rotation to branches
            // move to starting point
            torso.translate(screen_width/2.0, screen_height/2.0-40);
            lowerLeft.translate(-15, -60);
            midLeft.translate(-10, -30);
            farLeft.translate(10, -30);
            lowerRight.translate(15, -60);
            midRight.translate(-10, -30);
            farRight.translate(10, -30);

            return new Pane(torso, lowerLeft, lowerRight, farLeft, midLeft, midRight, farRight);
        }
    }
}
