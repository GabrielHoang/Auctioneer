package auctioneer.server.service;

import auctioneer.model.Item;
import auctioneer.server.utils.BidTooLowException;
import auctioneer.server.utils.ItemAlreadyOnAuctionException;
import auctioneer.server.utils.ItemNotFoundException;

import java.rmi.RemoteException;
import java.util.Map;

public interface ItemManagerService {

    void placeItemForBid(String ownerName, String itemName, String itemDesc, double startBid, int auctionTime) throws ItemAlreadyOnAuctionException;

    void delete(String itemName);

    Item get(String itemName) throws ItemNotFoundException;

    Map<String, Item> getMany();

    void bidOnItem(String bidderName, String itemName, double bid) throws BidTooLowException, RemoteException, ItemNotFoundException;
}
