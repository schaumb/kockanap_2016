package communication;

import bean.ItemType;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.nio.ByteBuffer;

/**
 * Created by qqcs on 25/11/16.
 */
public class Item {

    private final int itemId;
    private final ItemType type;
    private final char itemChar;
    private final int ownerId;
    private final Point.Double position;
    private final int rotation;
    private final int speed;
    private final int bulletNum;
    private final int rocketNum;

    private Item(int itemId, ItemType type, char itemChar, int ownerId, Point.Double position, int rotation, int speed, int bulletNum, int rocketNum) {
        this.itemId = itemId;
        this.type = type;
        this.itemChar = itemChar;
        this.ownerId = ownerId;
        this.position = position;
        this.rotation = rotation;
        this.speed = speed;
        this.bulletNum = bulletNum;
        this.rocketNum = rocketNum;
    }

    public static Item create(byte[] data) {
        return new Item(ByteBuffer.wrap(data).getInt(), ItemType.values()[data[4] & 0xFF], (char) (data[5] & 0xFF), data[6] & 0xFF,
                new Point2D.Double((data[7] & 0xFF) + (data[8] & 0xFF) / 100.0,
                (data[9] & 0xFF) + (data[10] & 0xFF) / 100.0), ByteBuffer.wrap(data, 11, 2).getShort(),
                (data[13] & 0xFF) - 100, data[14] & 0xFF, data[15] & 0xFF);
    }

    public int getItemId() {
        return itemId;
    }

    public ItemType getType() {
        return type;
    }

    public char getItemChar() {
        return itemChar;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public Point.Double getPosition() {
        return position;
    }

    public int getRotation() {
        return rotation;
    }

    public int getSpeed() {
        return speed;
    }

    public int getBulletNum() {
        return bulletNum;
    }

    public int getRocketNum() {
        return rocketNum;
    }

    public Point getAbPos() {
        return new Point((int)Math.round(getPosition().getX()), (int)Math.round(getPosition().getY()));
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemId=" + itemId +
                ", type=" + type +
                ", itemChar=" + itemChar +
                ", ownerId=" + ownerId +
                ", position=" + position +
                ", rotation=" + rotation +
                ", speed=" + speed +
                ", bulletNum=" + bulletNum +
                ", rocketNum=" + rocketNum +
                '}';
    }

    public Area getShape() {
        int size = getType().getSize();
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.toRadians(getRotation()), position.getX() * 35 + size/2 + (35 - size) / 2, position.getY() * 35 + size/2 + (35 - size) / 2);
        return new Area(transform.createTransformedShape(
                new Rectangle2D.Double(position.getX() * 35 + (35 - size) / 2, position.getY() * 35 + (35 - size) / 2, size, size)));
    }
}
