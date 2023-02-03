import java.util.Vector;

public interface Sprite {
    enum STATE {
        NONE,
        SELECTED,
        MOVING,
        SCALING,
        ROTATING
    }

    enum PART {
        TORSO,
        FACE,
        UPPERARM,
        LOWERARM,
        HAND,
        UPPERLEG,
        LOWERLEG,
        FOOT
    }

    Sprite parent = null;
    Vector<Sprite> children = new Vector<>();
    public STATE state = STATE.NONE;

    // translate
    public void translate(double x, double y);

    // rotate
    public void rotate(double theta, double px, double py);

    // scale
    public void scale(double s, double px, double py);

    public void reset();
}
