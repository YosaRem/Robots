package store;

public class WindowPosition {
    private String name;
    private int width;
    private int height;
    private boolean isHide;
    private int X;
    private int Y;

    public WindowPosition(String name, int width, int height,
                          boolean isHide, float X, float Y) {
        this.name = name;
        this.height = height;
        this.width = width;
        this.isHide = isHide;
        this.X = (int) X;
        this.Y = (int) Y;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public boolean isHide() {
        return isHide;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }
}
