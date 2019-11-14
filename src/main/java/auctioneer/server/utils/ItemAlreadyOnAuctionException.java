package auctioneer.server.utils;

public class ItemAlreadyOnAuctionException extends Exception {

    public ItemAlreadyOnAuctionException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
