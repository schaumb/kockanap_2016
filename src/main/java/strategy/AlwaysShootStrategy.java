package strategy;

import bean.CommandType;
import communication.Item;
import communication.Move;
import communication.TickMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qqcs on 26/11/16.
 */
public class AlwaysShootStrategy implements IStrategy {
    @Override
    public List<Move> apply(TickMessage message) {
        Item tank = map.getOurTank();

        List<Move> results = new ArrayList<>();

        if(tank == null) return results;

        if(tank.getBulletNum() > 0) {
            results.add(new Move(CommandType.SHOOTBULLET, 0));
        }
        else if(tank.getRocketNum() > 0) {
            results.add(new Move(CommandType.SHOOTROCKET, 0));
        }

        return results;
    }
}
