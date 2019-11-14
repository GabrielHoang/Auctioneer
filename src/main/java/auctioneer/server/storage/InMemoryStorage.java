package auctioneer.server.storage;

import auctioneer.model.Item;

import java.util.Map;

public class InMemoryStorage {

    public static Map<String, Item> storage;

    public static Map<String, Item> getStorage() {
        return storage;
    }
}
