package strategy;

import communication.Move;
import communication.TickMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by qqcs on 25/11/16.
 */
public class StrategyCollector implements IStrategy {
    private final List<IStrategy> strategies;

    public StrategyCollector(IStrategy... strategies) {
        this.strategies = Arrays.asList(strategies);
    }

    @Override
    public IStrategy onStrategyChange() {
        for(int i = 0; i < strategies.size(); ++i) {
            strategies.set(i, strategies.get(i).onStrategyChange());
        }

        return IStrategy.super.onStrategyChange();
    }

    @Override
    public List<Move> apply(TickMessage tickMessage) {
        onStrategyChange();

        List<Move> result = new ArrayList<>();

        for(IStrategy strategy : strategies) {
            result.addAll(strategy.apply(tickMessage));
        }

        return result;
    }
}
