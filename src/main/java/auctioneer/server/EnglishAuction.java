package auctioneer.server;

import auctioneer.interfaces.IAuctionListener;
import auctioneer.interfaces.IAuctionServer;
import auctioneer.model.Item;
import auctioneer.server.repository.ItemRepositoryStorageImpl;
import auctioneer.server.service.ItemManagerService;
import auctioneer.server.service.ItemManagerServiceImpl;

import java.rmi.RemoteException;
import java.time.LocalDateTime;
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
        itemManagerService.add(new Item("testOwner1", "item1", "itemDesc1", 10, "testOwner1", LocalDateTime.now().plusDays(2)));
        itemManagerService.add(new Item("testOwner2", "item2", "itemDesc2", 10, "testOwner2", LocalDateTime.now().plusDays(2)));
        itemManagerService.add(new Item("testOwner3", "item3", "itemDesc3", 10, "testOwner3", LocalDateTime.now().plusDays(2)));
    }

    @Override
    public void placeItemForBid(String ownerName, String itemName, String itemDesc, double startBid, int auctionTime) throws RemoteException {

    }

    @Override
    public void bidOnItem(String bidderName, String itemName, double bid) throws RemoteException {
        itemManagerService.bidOnItem(bidderName, itemName, bid);
    }

    @Override
    public Item[] getItems() throws RemoteException {
        Map<String, Item> itemMap = itemManagerService.getMany();
        Item[] itemArray = new Item[itemMap.size()];

        return itemMap.values().toArray(itemArray);
    }

    @Override
    public void registerListener(IAuctionListener al, String itemName) throws RemoteException {

    }
}
