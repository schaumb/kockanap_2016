package map;

import bean.ItemType;
import communication.Item;
import communication.TickMessage;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by qqcs on 25/11/16.
 */
public class DataMap implements IMap {

    private ArrayList<ArrayList<Item>> transformedMap = new ArrayList<>();
    private NavigableMap<Integer, TickMessage> messages = new TreeMap<>();
    private int ourId = -1;

    private int ourTick = 0;

    @Override
    public void initialize() {
        ourId = -1;
        ourTick = 0;
        messages.clear();
        transformedMap.clear();
    }

    @Override
    public void tick(TickMessage message) {
        if(getMessage() != null) {
            long c = getMessage().getItemList().stream()
                    .filter(i -> i.getType() == ItemType.WALL &&
                            message.getItemList().stream().noneMatch(mIt -> mIt.getItemId() == i.getItemId()))
                    .peek(message::addItem)
                    .count();

            if(c > 0) {
                getMessage().getItemList().stream()
                        .filter(i -> message.getItemList().stream().noneMatch(mIt -> mIt.getItemId() == i.getItemId()))
                        .forEach(message::addItem);
            }
        }
        messages.put(ourTick, message);
        messages.remove(ourTick - 3);

        if(message.getStatus() != null && ourId == -1) {
            ourId = message.getStatus().getPlayerId();
        } else if(message.getStatus() != null && message.getStatus().getPlayerId() != ourId){
            System.err.println("OUR ID IS NOT OUR ID");
        }

        transformedMap = new ArrayList<>();
        for(int i = 0; i < 20; ++i) {
            transformedMap.add(new ArrayList<>());
            for(int j = 0; j < 20; ++j) {
                transformedMap.get(i).add(getItemOnThisPos(new Point(i, j)));
            }
        }

        ++ourTick;

    }

    @Override
    public Item getOurTank() {
        if(messages.lastEntry() == null) return null;
        return messages.lastEntry().getValue().getItemList().stream()
                .filter(i -> i.getOwnerId() == ourId && i.getType() == ItemType.TANK)
                .findFirst().orElse(null);
    }

    @Override
    public Item getItemOnThisPos(Point pos) {
        if(messages.lastEntry() == null) return null;
        return messages.lastEntry().getValue().getItemList().stream()
                .sorted(Comparator.comparingDouble(i2 -> pos.distance(i2.getPosition())))
                .filter(i -> pos.distance(i.getPosition()) < Math.sqrt(2.0) / 2.0)
                .findFirst().orElse(null);
    }

    @Override
    public List<Item> getItems() {
        if(messages.lastEntry() == null) return null;
        return messages.lastEntry().getValue().getItemList();
    }

    public Item getOurPrevTank() {
        if(messages.lastEntry() == null) return null;
        return messages.lowerEntry(ourId).getValue().getItemList().stream()
                .filter(i -> i.getOwnerId() == ourId && i.getType() == ItemType.TANK)
                .findFirst().orElse(null);
    }

    public TickMessage getMessage() {
        if(messages.lastEntry() == null) return null;
        return messages.lastEntry().getValue();
    }

    @Override
    public int getOurId() {
        return ourId;
    }

    @Override
    public int getTick() {
        if(messages.lastEntry() == null) return -1;
        return messages.lastEntry().getValue().getStatus().getTimeLeft();
    }
}
