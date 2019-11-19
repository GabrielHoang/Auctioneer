package auctioneer.server;

import auctioneer.interfaces.IAuctionListener;
import auctioneer.interfaces.IAuctionServer;
import auctioneer.model.Item;
import auctioneer.server.repository.ItemRepositoryStorageImpl;
import auctioneer.server.service.ItemManagerService;
import auctioneer.server.service.ItemManagerServiceImpl;
import auctioneer.server.utils.BidTooLowException;
import auctioneer.server.utils.ItemAlreadyOnAuctionException;
import auctioneer.server.utils.ItemNotFoundException;

import java.rmi.RemoteException;
import java.util.Map;

public class EnglishAuction extends Auction implements IAuctionServer {
    private static EnglishAuction instance = null;
    private final ItemManagerService itemManagerService;

    private EnglishAuction(ItemManagerService itemManagerService) {
        this.itemManagerService = itemManagerService;

    }

    public static EnglishAuction getInstance() {
        if (instance == null) {
            instance = new EnglishAuction(new ItemManagerServiceImpl(new ItemRepositoryStorageImpl()));
        }

        instance.startupConfig();
        return instance;
    }

    public void startupConfig() {
        try {
            itemManagerService.placeItemForBid("testOwner1", "item1", "itemDesc1", 10, 60);
            itemManagerService.placeItemForBid("testOwner1", "item2", "itemDesc2", 10, 60);
            itemManagerService.placeItemForBid("testOwner1", "item3", "itemDesc3", 10, 60);
        } catch (ItemAlreadyOnAuctionException | RemoteException e) {
            e.getMessage();
        }
    }

    @Override
    public void placeItemForBid(String ownerName, String itemName, String itemDesc, double startBid, int auctionTime) throws RemoteException {
        try {
            itemManagerService.placeItemForBid(ownerName, itemName, itemDesc, startBid, auctionTime);
        } catch (ItemAlreadyOnAuctionException e) {
            e.getMessage();
        }
    }

    @Override
    public void bidOnItem(String bidderName, String itemName, double bid) throws RemoteException {
        try {
            itemManagerService.bidOnItem(bidderName, itemName, bid);
        } catch (ItemNotFoundException | BidTooLowException e) {
            e.getMessage();
        }
    }

    @Override
    public Item[] getItems() throws RemoteException {
        Map<String, Item> itemMap = itemManagerService.getMany();
        Item[] itemArray = new Item[itemMap.size()];

        return itemMap.values().toArray(itemArray);
    }

    @Override
    public void registerListener(IAuctionListener al, String itemName) throws RemoteException {
        try {
            Item item = itemManagerService.get(itemName);
            item.registerObserver(al);
        } catch (ItemNotFoundException e) {
            e.getMessage();
        }
    }
}
