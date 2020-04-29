package robot;

import java.awt.*;

public class Target {
    private volatile int targetPositionX;
    private volatile int targetPositionY;

    public Target(int targetPositionX, int targetPositionY) {
        this.targetPositionX = targetPositionX;
        this.targetPositionY = targetPositionY;
    }

    public void setTargetPosition(Point point) {
        targetPositionX = point.x;
        targetPositionY = point.y;
    }

    public Point getTargetPosition() {
        return new Point(targetPositionX, targetPositionY);
    }
}
