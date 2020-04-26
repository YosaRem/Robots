package store;

public class WindowPosition {
    private String name;
    private int width;
    private int height;
    private boolean isHide;
    private float alignmentX;
    private float alignmentY;

    public WindowPosition(String name, int width, int height,
                          boolean isHide, float alignmentX, float alignmentY) {
        this.name = name;
        this.height = height;
        this.width = width;
        this.isHide = isHide;
        this.alignmentX = alignmentX;
        this.alignmentY = alignmentY;
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

    public float getAlignmentX() {
        return alignmentX;
    }

    public float getAlignmentY() {
        return alignmentY;
    }
}
