package strategy;

import bean.CommandType;
import bean.ItemType;
import communication.Item;
import communication.Move;
import communication.TickMessage;
import utils.MoveUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qqcs on 26/11/16.
 */
public class AgressiveStrategy extends  TargetStrategy {
    public AgressiveStrategy() {
        super(new ArrayList<>());
    }

    @Override
    public List<Move> apply(TickMessage message) {

        Item ourTank = map.getOurTank();

        List<Move> moves = new ArrayList<>();

        if(ourTank != null) {
            message.getItemList().stream().filter(i -> i.getType() == ItemType.TANK &&
                    i.getOwnerId() != map.getOurId())
                    .map(Item::getAbPos)
                    .filter(p -> p.x == ourTank.getAbPos().x || p.y == ourTank.getAbPos().y)
                    .filter(p -> MoveUtil.closest(ourTank.getPosition(), MoveUtil.getRealDegree(ourTank.getPosition(), p)) ==
                        ItemType.TANK)
                    .forEach(p -> {
                        if (ourTank.getBulletNum() > 0 || ourTank.getRocketNum() > 0) {
                            moves.add(new Move(CommandType.SETROTATION, MoveUtil.getRealDegree(ourTank.getPosition(), p)));
                            if(ourTank.getRocketNum() > 0) {
                                moves.add(new Move(CommandType.SHOOTROCKET, 0));
                            } else if (ourTank.getBulletNum() > 0) {
                                moves.add(new Move(CommandType.SHOOTBULLET, 0));
                            }
                        }
                    });
        }

        return moves;
    }
}
