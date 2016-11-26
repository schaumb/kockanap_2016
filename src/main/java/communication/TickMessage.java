package communication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qqcs on 25/11/16.
 */
public class TickMessage {
    private Status status;
    private List<Item> itemList = new ArrayList<>();

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void addItem(Item item) {
        itemList.add(item);
    }
}
