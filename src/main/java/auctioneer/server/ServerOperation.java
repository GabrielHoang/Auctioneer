package auctioneer.server;

import auctioneer.HelloInterface;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerOperation {
    public static void main(String[] args) {
        System.setProperty("java.security.policy", "file:java.policy");

//        if (System.getSecurityManager() == null) {
//            System.setSecurityManager(new SecurityManager());
//        }

        String name="helloTest";
        HelloInterface server = new HelloInterfaceImpl();
        try {
            HelloInterface stub = (HelloInterface) UnicastRemoteObject.exportObject(server, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println("Server started");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
