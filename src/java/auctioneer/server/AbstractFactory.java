package auctioneer.server;

public interface AbstractFactory<T> {
    T create(String auctionType);
}
