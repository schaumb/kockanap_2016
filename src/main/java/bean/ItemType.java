package bean;

import java.awt.*;

/**
 * Created by qqcs on 25/11/16.
 */
public enum ItemType {
    EMPTY(0, Color.WHITE),
    WALL(35, Color.BLACK),
    WEAKWALL(35, Color.gray),
    TANK(20, Color.RED),
    BULLET(15, Color.CYAN),
    ROCKET(15, Color.MAGENTA),
    AMMOCRATE(25, Color.YELLOW);

    private final int size;
    private final Color color;

    ItemType(int size, Color color) {
        this.size = size;
        this.color = color;
    }

    public int getSize() {
        return size;
    }

    public Color getColor() {
        return color;
    }
}
