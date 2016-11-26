package strategy;

import communication.Move;
import communication.TickMessage;

import java.util.Collections;
import java.util.List;

/**
 * Created by qqcs on 25/11/16.
 */
public class MapRefresherStrategy implements IStrategy {
    @Override
    public List<Move> apply(TickMessage tickMessage) {
        map.tick(tickMessage);

        return Collections.emptyList();
    }
}
