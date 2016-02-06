package ua.ukma.geronimo.cube;

public class MotionTracker {
    private float x, y, dx, dy;

    public MotionTracker(float x, float y) {
        reset(x, y);
    }

    public void reset() {
        reset(0, 0);
    }

    public void reset(float x, float y) {
        this.x = x;
        this.y = y;
        dx = dy = 0;
    }

    public void track(float x, float y) {
        this.dx = x - this.x;
        this.dy = y - this.y;
        this.x = x;
        this.y = y;
    }

    public float getDx() {
        return dx;
    }

    public float getDy() {
        return dy;
    }
}
