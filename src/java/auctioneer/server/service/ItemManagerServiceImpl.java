package auctioneer.server.service;

import auctioneer.model.Item;
import auctioneer.server.repository.ItemRepository;
import auctioneer.server.repository.ItemRepositoryStorageImpl;
import auctioneer.server.utils.BidTooLowException;
import auctioneer.server.utils.ItemNotFoundException;

import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.Map;

public class ItemManagerServiceImpl implements ItemManagerService {
    private final ItemRepository itemRepository;

    public ItemManagerServiceImpl(ItemRepositoryStorageImpl itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public void placeItemForBid(String ownerName, String itemName, String itemDesc, double startBid, int auctionTime) throws RemoteException {
        if (isItemAlreadyPresent(itemName)) {
            throw new RemoteException("Item already present on auction");
        }

        itemRepository.add(new Item(ownerName, itemName, itemDesc, startBid, "no bidders", LocalDateTime.now().plusSeconds(auctionTime)));
    }

    @Override
    public Item get(String itemName) throws ItemNotFoundException {
        return itemRepository.get(itemName);
    }

    @Override
    public Map<String, Item> getMany() {
        return itemRepository.getMany();
    }

    @Override
    public void bidOnItem(String bidderName, String itemName, double bid) throws RemoteException, ItemNotFoundException, BidTooLowException {
        Item retrievedItem = itemRepository.get(itemName);

        if (retrievedItem.getCurrentBid() > bid) {
            throw new BidTooLowException("Bid was too low.");
        }

        retrievedItem.setCurrentBidderName(bidderName);
        retrievedItem.setCurrentBid(bid);
        retrievedItem.updateObservers();
        System.out.println("Updated " + itemName + " (" + bidderName + ", " + bid + ")");

    }

    private boolean isItemAlreadyPresent(String itemName) {
        Map<String, Item> items = getMany();
        return items.entrySet().stream().anyMatch(entry -> entry.getKey().equalsIgnoreCase(itemName));
    }
}
