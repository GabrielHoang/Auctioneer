package auctioneer.server.service;

import auctioneer.model.Item;

import java.rmi.RemoteException;
import java.util.Map;

public interface ItemManagerService {

    void add(Item item);

    void delete(String itemName);

    Item get(String itemName);

    Map<String, Item> getMany();

    void bidOnItem(String bidderName, String itemName, double bid) throws RemoteException;
}
