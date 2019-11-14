package auctioneer.server.repository;

import auctioneer.model.Item;

import java.util.Map;

public interface ItemRepository {
    void add(Item item);

    void delete(String itemName);

    void update(Item item);

    Item get(String itemName);

    Map<String, Item> getMany();

}
