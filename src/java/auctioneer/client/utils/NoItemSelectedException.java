package auctioneer.client.utils;

public class NoItemSelectedException extends Exception {
    public NoItemSelectedException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
