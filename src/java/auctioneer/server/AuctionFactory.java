package auctioneer.server;

import auctioneer.interfaces.IAuctionServer;

public class AuctionFactory implements AbstractFactory<IAuctionServer> {
    @Override
    public IAuctionServer create(String auctionType) {
        if (auctionType.equalsIgnoreCase("english")) {
            return EnglishAuction.getInstance();
        }

        return null;
    }
}
