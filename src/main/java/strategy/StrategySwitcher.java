package strategy;

import communication.Move;
import communication.TickMessage;

import java.util.List;
import java.util.function.BooleanSupplier;

/**
 * Created by qqcs on 26/11/16.
 */
public class StrategySwitcher implements IStrategy {

    private final IStrategy caller;
    private IStrategy current;
    private final BooleanSupplier backToCaller;

    public StrategySwitcher(IStrategy caller, IStrategy current, BooleanSupplier backToCaller) {
        this.caller = caller;
        this.current = current;
        this.backToCaller = backToCaller;
    }

    @Override
    public List<Move> apply(TickMessage message) {
        return current.apply(message);
    }

    @Override
    public IStrategy onStrategyChange() {
        if(backToCaller.getAsBoolean()) {
            return caller.onStrategyChange();
        }
        current = current.onStrategyChange();

        return IStrategy.super.onStrategyChange();
    }
}
