package strategy;

import bean.CommandType;
import bean.ItemType;
import communication.Item;
import communication.Move;
import communication.TickMessage;
import utils.MoveUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by qqcs on 25/11/16.
 */
public class TargetStrategy implements IStrategy {

    private final List<Point> targets;
    private int shootedLastTarget = 0;
    private boolean startedEmptyDijsktra = false;
    private Point previousTarget = null;
    private boolean previousTargetChange = false;

    public TargetStrategy(List<Point> targets) {
        this.targets = targets;
    }

    protected List<Point> getTargets() {
        return targets;
    }

    @Override
    public List<Move> apply(TickMessage message) {
        Item tank = map.getOurTank();

        List<Move> results = new ArrayList<>();

        if(tank == null || targets.isEmpty()) {
            System.out.println("No tank or targets is empty");
            results.add(new Move(CommandType.SETSPEED, 100));
            results.addAll(new NewAgressiveStrategy().apply(message));
            return results;
        }
        while (!targets.isEmpty() && targets.get(0) == null) {
            System.out.println("WTF?");
            targets.remove(0);
        }
        if(targets.isEmpty()) {
            System.out.println("VERY WTF?");
            results.add(new Move(CommandType.SETSPEED, 100));
            results.addAll(new NewAgressiveStrategy().apply(message));
            return results;
        }
        if(tank.getBulletNum() + tank.getRocketNum() > 5) {
            List<Move> moves = new NewAgressiveStrategy().apply(message);
            results.addAll(moves);
            if(moves.size() > 2) {
                shootedLastTarget = map.getTick();
            }
        }

        System.out.println("Next target is: " + targets.get(0) + " Our pos: " + tank.getAbPos());

        Point p = tank.getAbPos();

        boolean goodNextTarget = p.x == targets.get(0).x || p.y == targets.get(0).y;
        boolean wantToTurn = goodNextTarget && MoveUtil.getDegree(tank.getAbPos(), targets.get(0)) != tank.getRotation();
        boolean canTurn = MoveUtil.canTurn(tank.getPosition());
        boolean atTheDestination = canTurn && tank.getAbPos().equals(targets.get(0));
        boolean hasBullet = tank.getRocketNum() > 0 || tank.getBulletNum() > 0;
        boolean targetChanged = !targets.get(0).equals(previousTarget);

        if(targetChanged) {
            startedEmptyDijsktra = false;
        }
        if(!goodNextTarget) {
            System.out.println("Bad coord -> use other");
            Point to;
            if(!hasBullet) {
                to = MoveUtil.getNextTargetTo(p, targets.get(0), Objects::isNull);
                if(to != null) {
                    List<Point> l = new ArrayList<>();
                    l.add(to);

                    IStrategy strategy = new TargetStrategy(l);
                    results.addAll(strategy.apply(message));

                    startedEmptyDijsktra = true;
                }
            }
            if(!startedEmptyDijsktra) {
                to = MoveUtil.getNextTargetTo(p, targets.get(0), i -> i == null || i.getType() != ItemType.WALL);
                List<Point> l = new ArrayList<>();
                l.add(to);

                IStrategy strategy = new TargetStrategy(l);
                results.addAll(strategy.apply(message));
            }
        } else if(wantToTurn && canTurn) {
            System.out.println("GO MAX SPEED");
            results.add(new Move(CommandType.SETROTATION, MoveUtil.getDegree(tank.getAbPos(), targets.get(0))));
            results.add(new Move(CommandType.SETSPEED, 100));
        } else if(wantToTurn) {
            Point toPos = tank.getAbPos();

            System.out.println("Easy way");
            results.add(new Move(CommandType.SETROTATION, MoveUtil.getRealDegree(tank.getPosition(), toPos)));

            results.add(new Move(CommandType.SETSPEED, 25));

            if(MoveUtil.hasForwardWallOrEnemy(tank.getPosition(), tank.getRotation()) &&
                    shootedLastTarget != map.getTick()) {
                if (tank.getRocketNum() > 0) {
                    results.add(new Move(CommandType.SHOOTROCKET, 0));
                }
                else if(tank.getBulletNum() > 2) {
                    results.add(new Move(CommandType.SHOOTBULLET, 0));
                }

                shootedLastTarget = map.getTick();
            }
        } else if(atTheDestination) {
            System.out.println("STOP Hammertime");
            targets.add(targets.get(0));
            targets.remove(0);
            results.add(new Move(CommandType.SETROTATION, MoveUtil.getDegree(tank.getAbPos(), targets.get(0))));
            results.add(new Move(CommandType.SETSPEED, 0));
        } else {
            System.out.println("IDDLE");
            results.add(new Move(CommandType.SETSPEED, 100));
            if(MoveUtil.hasForwardWallOrEnemy(tank.getPosition(), tank.getRotation())) {
                if(shootedLastTarget != map.getTick()) {
                    System.out.println("Has Wall or enemy");
                    if (tank.getBulletNum() > 0) {
                        results.add(new Move(CommandType.SHOOTBULLET, 0));
                    } else if(tank.getRocketNum() > 1) {
                        results.add(new Move(CommandType.SHOOTROCKET, 0));
                    }
                    shootedLastTarget = map.getTick();
                }
            } else {
                System.out.println("NO forward wall");
            }
        }
        previousTarget = targets.get(0);
        previousTargetChange = targetChanged;

        return results;
    }

    @Override
    public IStrategy onStrategyChange() {
        return IStrategy.super.onStrategyChange();
    }
}
