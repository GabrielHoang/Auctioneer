package auctioneer.server.service;

import auctioneer.interfaces.IAuctionListener;
import auctioneer.model.Item;

import java.rmi.RemoteException;

public class IAuctionListenerImpl implements IAuctionListener {
    @Override
    public void update(Item item) throws RemoteException {

    }
}
