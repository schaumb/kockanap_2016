package strategy;

import bean.CommandType;
import bean.ItemType;
import communication.Item;
import communication.Move;
import communication.TickMessage;
import utils.MoveUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by qqcs on 26/11/16.
 */
public class NewAgressiveStrategy implements IStrategy {
    @Override
    public List<Move> apply(TickMessage message) {

        List<Move> moves = new ArrayList<>();
        Item tank = map.getOurTank();

        if(tank != null) {
            List<Item> list = map.getItems().stream()
                    .filter(i -> i.getType() == ItemType.TANK && i.getOwnerId() != map.getOurId())
                    .filter(i -> MoveUtil.getNextTargetTo(tank.getAbPos(), i.getAbPos(), it -> it == null ||
                        it.getType() == ItemType.TANK) != null)
                    .sorted(Comparator.comparingDouble(i2 -> i2.getPosition().distance(tank.getPosition())))
                    .filter(i -> ItemType.TANK ==
                            MoveUtil.closest(tank.getPosition(), MoveUtil.getRealDegree(tank.getPosition(), i.getAbPos())))
                    .collect(Collectors.toList());

            list.stream().limit(tank.getBulletNum()).forEach(i -> {
                moves.add(new Move(CommandType.SETROTATION, MoveUtil.getRealDegree(tank.getPosition(), i.getAbPos())));
                moves.add(new Move(CommandType.SHOOTBULLET, 0));
            });

            list.stream().skip(tank.getBulletNum()).limit(tank.getRocketNum())
                    .forEach(i -> {
                moves.add(new Move(CommandType.SETROTATION, MoveUtil.getRealDegree(tank.getPosition(), i.getAbPos())));
                moves.add(new Move(CommandType.SHOOTROCKET, 0));
            });
        }
        if(tank != null && moves.size() == 0) {
            List<Item> list = map.getItems().stream()
                    .filter(i -> i.getType() == ItemType.TANK && i.getOwnerId() != map.getOurId())
                    .sorted(Comparator.comparingDouble(i2 -> i2.getPosition().distance(tank.getPosition())))
                    .collect(Collectors.toList());
            /*
            if(list.size() > 0) {
                System.out.println("Go to enemy");
                List<Point> pos = new ArrayList<>();
                pos.add(list.get(0).getAbPos());
                moves.addAll(new TargetStrategy(pos).apply(message));
            }
            */

            System.out.println("Nothing to do");
        }

        return moves;
    }
}
