package auctioneer.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemObs {

    private SimpleStringProperty ownerName;
    private SimpleStringProperty itemName;
    private SimpleStringProperty itemDesc;
    private SimpleStringProperty currentBidderName;
    private LocalDateTime expirationTime;
    private SimpleDoubleProperty currentBid;
}
