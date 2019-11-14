package auctioneer.server.utils;

public class ObserverAlreadyRegisteredException extends Exception {
    public ObserverAlreadyRegisteredException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
