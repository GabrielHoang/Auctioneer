package auctioneer.server;

import auctioneer.interfaces.IAuctionServer;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerOperation {
    public static void main(String[] args) {
        System.setProperty("java.security.policy", "file:java.policy");

        String english = "english";
        AuctionFactory auctionFactory = new AuctionFactory();
        IAuctionServer englishAuctionServer = auctionFactory.create(english);
        try {
            IAuctionServer stub = (IAuctionServer) UnicastRemoteObject.exportObject(englishAuctionServer, 0);
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind(english, stub);
            System.out.println("Server started");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
