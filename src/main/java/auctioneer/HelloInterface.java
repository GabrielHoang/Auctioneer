package auctioneer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HelloInterface extends Remote {
    public String say(String message) throws RemoteException;
}
