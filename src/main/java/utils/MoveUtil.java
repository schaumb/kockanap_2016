package utils;

import bean.ItemType;
import communication.Item;
import map.IMap;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by qqcs on 26/11/16.
 */
public class MoveUtil {
    public static IMap map = IMap.MapConfig.getMap();

    public static boolean canTurn(Point.Double position) {
        Point p = new Point((int)Math.round(position.getX()), (int)Math.round(position.getY()));
        return p.distance(position) < 0.2;
    }

    public static List<Point> getNeighbours(Point p) {
        return Arrays.asList(
                new Point(p.x - 1, p.y),
                new Point(p.x, p.y - 1),
                new Point(p.x + 1, p.y),
                new Point(p.x, p.y + 1));
    }


    public static Point getNextTargetTo(Point from, Point to, Predicate<Item> goodItem) {
        if(from.equals(to)) return from;

        Map<Point, List<Point>> checkedPoints = new TreeMap<>((p1, p2) -> {
            if(p1.x != p2.x) return Integer.compare(p1.x, p2.x);
            return Integer.compare(p1.y, p2.y);
        });

        ArrayDeque<Point> checkables = new ArrayDeque<>();
        checkables.addLast(from);
        checkedPoints.put(from, Collections.emptyList());
        while(!checkables.isEmpty()) {
            Point nextCheckable = checkables.removeFirst();
            for(Point p : getNeighbours(nextCheckable)) {
                if(checkedPoints.containsKey(p)) {
                    continue;
                }

                if(p.equals(to)) {
                    List<Point> points = new ArrayList<>(checkedPoints.get(nextCheckable));
                    points.add(nextCheckable);
                    Point firstCheckPoint = points.get(0);
                    int i = 1;
                    while(i < points.size() && firstCheckPoint.x == points.get(i).x) {
                        firstCheckPoint = points.get(i);
                        ++i;
                    }
                    if(i == 1) {
                        while(i < points.size() && firstCheckPoint.y == points.get(i).y) {
                            firstCheckPoint = points.get(i);
                            ++i;
                        }
                    }

                    return firstCheckPoint;
                }

                Item i = map.getItemOnThisPos(p);

                if(goodItem.test(i)) {
                    List<Point> newRoute = new ArrayList<>(checkedPoints.get(nextCheckable));
                    newRoute.add(nextCheckable);
                    checkedPoints.put(p, newRoute);
                    checkables.addLast(p);
                }
            }
        }
        return null;
    }

    public static int getDegree(Point from, Point to) {
        if(from.x == to.x) {
            return from.y < to.y ? 90 : 270;
        } else if(from.y == to.y) {
            return from.x < to.x ? 0 : 180;
        }
        return 0;
    }

    public static int getRealDegree(Point2D.Double from, Point targetPosition) {
        double sx = from.getX();
        double sy = from.getY();
        double tx = targetPosition.getX();
        double ty = targetPosition.getY();

        double vecX = tx - sx;
        double vecY = ty - sy;

        double angleRad = Math.atan2(vecY, vecX);
        double toAngleInDegree = Math.toDegrees(angleRad);

        if(toAngleInDegree < 0) // positive correction -> 0 - 360
            toAngleInDegree = 360.0 + toAngleInDegree;

        return (int)Math.round(toAngleInDegree);
    }

    public static boolean hasForwardWallOrEnemy(Point.Double abPos, int angle) {

        Point.Double diff = new Point.Double(Math.cos(Math.toRadians(angle)), Math.sin(Math.toRadians(angle)));

        Point.Double from = new Point.Double(abPos.getX(), abPos.getY());
        int in = 0;
        while(0 < from.x && from.x < 20 && 0 < from.y && from.y < 20 && ++in < 3) {
            Item i = map.getItemOnThisPos(new Point((int)Math.round(from.getX()), (int)Math.round(from.getY())));
            if(i != null && (i.getType() == ItemType.WEAKWALL || (i.getType() == ItemType.TANK
                && i.getOwnerId() != map.getOurId()))) {
                return true;
            }
            from.setLocation(from.getX() + diff.x, from.getY() + diff.y);
        }
        return false;
    }

    public static ItemType closest(Point.Double abPos, int angle) {
        Point.Double diff = new Point.Double(Math.cos(Math.toRadians(angle)), Math.sin(Math.toRadians(angle)));

        Point.Double from = new Point.Double(abPos.getX(), abPos.getY());
        int in = 0;
        while(0 < from.x && from.x < 20 && 0 < from.y && from.y < 20 && ++in < 3) {
            Item i = map.getItemOnThisPos(new Point((int)Math.round(from.getX()), (int)Math.round(from.getY())));
            if(i != null && (i.getType() != ItemType.TANK
                    || i.getOwnerId() != map.getOurId())) {
                return i.getType();
            }
            from.setLocation(from.getX() + diff.x, from.getY() + diff.y);
        }
        return null;
    }
}
