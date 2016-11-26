package strategy;

import communication.Move;
import communication.TickMessage;
import map.IMap;

import java.util.List;
import java.util.function.Function;

/**
 * Created by qqcs on 26/11/16.
 */
public interface IStrategy extends Function<TickMessage, List<Move>> {
    IMap map = IMap.MapConfig.getMap();

    default IStrategy onStrategyChange() {
        return this;
    }
}
