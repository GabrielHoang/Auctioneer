package auctioneer.server;

import auctioneer.HelloInterface;

import java.rmi.RemoteException;

public class HelloInterfaceImpl implements HelloInterface {
    @Override
    public String say(String message) throws RemoteException {
        if (message.equals("test")) {
            return "test ok";
        }
        return "different text thant test";
    }
}
