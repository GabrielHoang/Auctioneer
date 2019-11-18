package auctioneer.client;

import auctioneer.model.Item;
import auctioneer.model.ItemObs;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class ItemMapper {

    private static ItemMapper instance = null;

    private ItemMapper() {
        if (instance == null) {
            instance = new ItemMapper();
        }
    }

    public static ItemObs itemToItemObs(Item item) {
        ItemObs itemObs = new ItemObs();
        itemObs.setOwnerName(new SimpleStringProperty(item.getOwnerName()));
        itemObs.setItemName(new SimpleStringProperty(item.getItemName()));
        itemObs.setItemDesc(new SimpleStringProperty(item.getItemDesc()));
        itemObs.setCurrentBidderName(new SimpleStringProperty(item.getCurrentBidderName()));
        itemObs.setCurrentBid(new SimpleDoubleProperty(item.getCurrentBid()));
        itemObs.setExpirationTime(item.getExpirationTime());

        return itemObs;
    }

    public ItemMapper getInstance() {
        return instance;
    }
}
