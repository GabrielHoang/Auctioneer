package auctioneer.server.repository;

import auctioneer.model.Item;
import auctioneer.server.utils.ItemNotFoundException;

import java.util.Map;

public interface ItemRepository {
    void add(Item item);

    void delete(String itemName);

    void update(Item item);

    Item get(String itemName) throws ItemNotFoundException;

    Map<String, Item> getMany();

}
