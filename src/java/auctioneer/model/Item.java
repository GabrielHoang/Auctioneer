package auctioneer.model;

import auctioneer.interfaces.IAuctionListener;
import lombok.Data;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class Item implements Serializable {

    private String ownerName;
    private String itemName;
    private String itemDesc;
    List<IAuctionListener> observers;
    private String currentBidderName;
    private LocalDateTime expirationTime;
    private double currentBid;

    public Item(String ownerName, String itemName, String itemDesc, double currentBid, String currentBidderName, LocalDateTime expirationTime) {
        this.ownerName = ownerName;
        this.itemName = itemName;
        this.itemDesc = itemDesc;
        this.currentBid = currentBid;
        this.currentBidderName = currentBidderName;
        this.expirationTime = expirationTime;
        this.observers = new ArrayList<>();
    }

    public void registerObserver(IAuctionListener observer) {
        if (observers.isEmpty() || !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(IAuctionListener observer) {
        observers.remove(observer);
    }

    public void updateObservers() throws RemoteException {
        for (IAuctionListener observer : observers) {
            observer.update(this);
        }
    }

}