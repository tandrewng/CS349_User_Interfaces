import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import java.util.Vector;

public class Head extends Appendage{
    private static int instanceCount = 0;
    private final int instance;
    public Sprite.STATE state = Sprite.STATE.NONE;

    private double rotated = 0;
    private double prevX = -1;
    private double prevY = -1;
    protected Vector<Sprite> children = new Vector<>();
    private Torso parent = null;

    // Creates a rectangle based at the origin with the specified width and height
    public Head(int x, int y, int width, int height, Torso p) {
        super();
        instance = ++instanceCount;
        parent = p;
        Image img = new Image("head.png");

        this.setCenterX(x);
        this.setCenterY(y);
        this.setRadiusX(width);
        this.setRadiusY(height);
        if (Ragdoll.doll == Ragdoll.DOLL.HUMAN){
            this.setFill(new ImagePattern(img));
        } else {
            this.setFill(Color.GREEN);
        }
        setOnMousePressed(event -> {
            prevX = -1;
            prevY = -1;
            state = Sprite.STATE.SELECTED;
            System.out.println("SELECTED " + instance);
        });

        setOnMouseDragged(event -> {
            if (prevX < 0) prevX = event.getX();
            if (prevY < 0) prevY = event.getY();

            double distance = Math.sqrt(Math.pow(event.getX() - prevX, 2) + Math.pow(event.getY() - prevY, 2));

            translate(event.getX(), event.getY());
//            rotate(Math.toDegrees(Math.atan(distance)), event.getX(), event.getY());

            state = Sprite.STATE.MOVING;
            prevX = event.getX();
            prevY = event.getY();
        });

        setOnMouseReleased(event -> {
            state = Sprite.STATE.NONE;
            System.out.println("UNSELECTED " + instance);
        });
    }

    // translate
    public void translate(double x, double y) {
        Translate translate = new Translate(x, y);
        if (this.getTransforms().isEmpty() || this.getTransforms().size() == 1) {
            this.getTransforms().add(translate);
        } else {
            this.getTransforms().add(2,translate);
        }
    }

    // rotate
    public void rotate(double theta, double px, double py) {
        Rotate rotate = new Rotate(theta, px, py);
        rotate.setPivotX(px);
        rotate.setPivotY(py);
        this.getTransforms().add(rotate);
    }

    // scale
    public void scale(double s, double px, double py) {
    }

    @Override
    public String toString() {
        return ("Sprite " + instance);
    }

    public void reset() {
        Translate translate0 = (Translate) this.getTransforms().get(0);
        Translate translate1 = (Translate) this.getTransforms().get(1);
        this.getTransforms().clear();
        this.getTransforms().add(translate0);
        this.getTransforms().add(translate1);
        for (Sprite child : children) {
            child.reset();
        }
    }
}
