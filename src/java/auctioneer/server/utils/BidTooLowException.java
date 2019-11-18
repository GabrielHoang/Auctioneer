package auctioneer.server.utils;

import java.rmi.RemoteException;

public class BidTooLowException extends RemoteException {

    public BidTooLowException(String s) {
        super("Bid was not higher than current bid. Bid not accepted.");
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
