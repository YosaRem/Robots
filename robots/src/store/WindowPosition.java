package store;

public class WindowPosition {
    private String name;
    private int width;
    private int height;
    private boolean isHide;

    public WindowPosition(String name, int width, int height, boolean isHide) {
        this.name = name;
        this.height = height;
        this.width = width;
        this.isHide = isHide;
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
}
