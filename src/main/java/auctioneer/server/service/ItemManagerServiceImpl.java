package auctioneer.server.service;

import auctioneer.model.Item;
import auctioneer.server.repository.ItemRepository;
import auctioneer.server.repository.ItemRepositoryStorageImpl;

import java.rmi.RemoteException;
import java.util.Map;

public class ItemManagerServiceImpl implements ItemManagerService {
    private final ItemRepository itemRepository;

    public ItemManagerServiceImpl(ItemRepositoryStorageImpl itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public void add(Item item) {
        itemRepository.add(item);
    }

    @Override
    public void delete(String itemName) {
        itemRepository.delete(itemName);
    }

    @Override
    public Item get(String itemName) {
        return null;
    }

    @Override
    public Map<String, Item> getMany() {
        return itemRepository.getMany();
    }

    @Override
    public void bidOnItem(String bidderName, String itemName, double bid) throws RemoteException {
        Item retrievedItem = itemRepository.get(itemName);

        if (retrievedItem.getCurrentBid() > bid) {
            throw new RemoteException("The bid was too low");
        }

        retrievedItem.setCurrentBidderName(bidderName);
        retrievedItem.setCurrentBid(bid);
        retrievedItem.updateObservers();

    }
}
