package auctioneer.interfaces;

import auctioneer.model.Item;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IAuctionListener extends Remote {
    void update(Item item) throws RemoteException;
}
