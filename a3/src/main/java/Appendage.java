import javafx.scene.shape.Ellipse;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

import java.util.Vector;

public abstract class Appendage extends Ellipse implements Sprite{
    enum STATE {
        NONE,
        SELECTED,
        MOVING,
        SCALING,
        ROTATING
    }

    enum Part {
        TORSO,
        FACE,
        UPPERARM,
        LOWERARM,
        HAND,
        UPPERLEG,
        LOWERLEG,
        FOOT
    }

    protected Sprite parent = null;
    protected Vector<Sprite> children = new Vector<>();
    public STATE state = STATE.NONE;

    // translate
    public void translate(double x, double y) {
        Translate translate = new Translate(x, y);
        if (this.getTransforms().isEmpty()) {
            this.getTransforms().add(translate);
        } else {
            this.getTransforms().add(1,translate);
        }

        for (Sprite child : children) {
            child.translate(x, y);
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
        Scale scale = new Scale(s, s);
        scale.setPivotX(px);
        scale.setPivotY(py);
        this.getTransforms().add(scale);
    }

    public void reset(double x, double y){
        Translate translate = (Translate) this.getTransforms().get(0);
        this.getTransforms().clear();
        this.getTransforms().add(translate);
        for (Sprite child : children) {
            child.reset();
        }
    }
}
