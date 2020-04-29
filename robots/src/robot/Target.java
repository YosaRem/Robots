package robot;

import java.awt.*;

public class Target {
    private final int targetPositionX;
    private final int targetPositionY;

    public Target(Point point) {
        this.targetPositionX = point.x;
        this.targetPositionY = point.y;
    }

    public Point getTargetPosition() {
        return new Point(targetPositionX, targetPositionY);
    }
}
