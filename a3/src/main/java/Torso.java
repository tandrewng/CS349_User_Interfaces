import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;

import java.util.Vector;

public class Torso extends Rectangle implements Sprite{
    private static int instanceCount = 0;
    private final int instance;
    public STATE state = STATE.NONE;

    private double prevX = -1;
    private double prevY = -1;
    protected Vector<Sprite> children = new Vector<>();

    // Creates a rectangle based at the origin with the specified width and height
    public Torso(int x, int y, int width, int height) {
        super();
        instance = ++instanceCount;

        Image img = new Image("torso.png");

        this.setX(x);
        this.setY(y);
        this.setWidth(width);
        this.setHeight(height);
        this.setArcWidth(15.0);
        this.setArcHeight(15.0);
        if (Ragdoll.doll == Ragdoll.DOLL.HUMAN){
            this.setFill(new ImagePattern(img));
        } else {
            this.setFill(Color.BLACK);
        }
        setOnMousePressed(event -> {
            prevX = -1;
            prevY = -1;
            state = STATE.SELECTED;
            System.out.println("SELECTED " + instance);
        });

        setOnMouseDragged(event -> {
            if (prevX < 0) prevX = event.getX();
            if (prevY < 0) prevY = event.getY();

            double distance = Math.sqrt(Math.pow(event.getX() - prevX, 2) + Math.pow(event.getY() - prevY, 2));

            translate(event.getX(), event.getY());

            this.state = STATE.MOVING;
            this.prevX = event.getX();
            this.prevY = event.getY();
        });

        setOnMouseReleased(event -> {
            this.state = STATE.NONE;
            System.out.println("UNSELECTED " + instance);
        });
    }

    // translate
    public void translate(double x, double y) {
        Translate translate = new Translate(x, y);
        this.getTransforms().add(translate);

        for (Sprite child : children) {
            child.translate(x, y);
        }
    }

    // rotate
    public void rotate(double theta, double px, double py) {
    }

    // scale
    public void scale(double s, double px, double py) {
    }

    @Override
    public String toString() {
        return ("Sprite " + instance);
    }

    public void reset() {
        Translate translate = (Translate) this.getTransforms().get(0);
        this.getTransforms().clear();
        this.getTransforms().add(translate);
        for (Sprite child : children) {
            child.reset();
        }
    }
}
