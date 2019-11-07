package auctioneer.model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Observable;


public class Item extends Observable {

    private String ownerName;
    private String itemName;
    private String itemDesc;
    private int currentBid;
    private String currentBidderName;
    private LocalDateTime expirationTime;
}