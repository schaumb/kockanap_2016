package map;

import communication.Item;
import communication.TickMessage;

import java.awt.*;
import java.util.List;

/**
 * Created by qqcs on 25/11/16.
 */
public interface IMap {

    void initialize();

    void tick(TickMessage message);

    Item getOurTank();

    Item getItemOnThisPos(Point pos);

    List<Item> getItems();

    int getOurId();

    int getTick();

    class MapConfig {
        private MapConfig() {
        }

        public static IMap getMap() {
            return MapGui.get();
        }
    }
}
