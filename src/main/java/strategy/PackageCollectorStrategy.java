package strategy;

import bean.ItemType;
import communication.Item;
import communication.Move;
import communication.TickMessage;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by qqcs on 26/11/16.
 */
public class PackageCollectorStrategy extends TargetStrategy {
    public PackageCollectorStrategy() {
        super(new ArrayList<>());
    }

    @Override
    public List<Move> apply(TickMessage message) {

        Item ourTank = map.getOurTank();

        if(ourTank != null) {
            getTargets().clear();
            getTargets().addAll(message.getItemList().stream().filter(i -> i.getType() == ItemType.AMMOCRATE)
                    .map(Item::getPosition)
                    .sorted(Comparator.comparingDouble(d -> ourTank.getPosition().distance(d)))
                    .map(d -> new Point((int)Math.round(d.getX()), (int)Math.round(d.getY())))
                    .collect(Collectors.toList()));
        }

        return super.apply(message);
    }

    @Override
    public IStrategy onStrategyChange() {

        if(map.getItems() != null && map.getItems().stream().noneMatch(i -> i.getType() == ItemType.AMMOCRATE)) {
            return new StrategySwitcher(this, new AgressiveStrategy(),
                    () -> map.getItems().stream().anyMatch(i -> i.getType() == ItemType.AMMOCRATE));
        }

        return super.onStrategyChange();
    }
}
