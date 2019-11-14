package auctioneer.server.repository;

import auctioneer.model.Item;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ItemRepositoryStorageImpl implements ItemRepository {
    private static Map<String, Item> storage = new HashMap<>();

    @Override
    public void add(Item item) {
        storage.put(item.getItemName(), item);
    }

    @Override
    public void delete(String itemName) {
        storage.remove(itemName);
    }

    @Override
    public void update(Item item) {
        storage.replace(item.getItemName(), item);
    }

    @Override
    public Item get(String itemName) {
        return storage.get(itemName);
    }

    @Override
    public Map<String, Item> getMany() {
        storage.isEmpty();
        return storage.entrySet().stream().filter(stringItemEntry ->
                !stringItemEntry.getValue().getExpirationTime().isBefore(LocalDateTime.now()))
                .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));
    }
}
