package auctioneer.client;

import auctioneer.interfaces.IAuctionServer;
import auctioneer.model.Item;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;

public class ClientOperation extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/home.fxml"));
        primaryStage.setTitle("Auctioneer");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
        connectClient();
    }

    private void connectClient() {
        String name = "english";
        try {
            Registry registry = LocateRegistry.getRegistry();
            IAuctionServer englishAuctionService = (IAuctionServer) registry.lookup(name);

            Item[] items = englishAuctionService.getItems();
            Arrays.stream(items).forEach(item -> System.out.println(item.getItemName()));


        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }
}
