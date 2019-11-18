package auctioneer.server.utils;

public class BidTooLowException extends Exception {

    public BidTooLowException(String s) {
        super(s);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
