package auctioneer.client;

import auctioneer.client.utils.AuctionHouseNotConnectedException;
import auctioneer.client.utils.NoBidAmountException;
import auctioneer.client.utils.NoBidderNameException;
import auctioneer.client.utils.NoItemSelectedException;
import auctioneer.interfaces.IAuctionListener;
import auctioneer.interfaces.IAuctionServer;
import auctioneer.model.Item;
import auctioneer.model.ItemObs;
import auctioneer.server.utils.BidTooLowException;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.Serializable;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ResourceBundle;

public class HomeController extends UnicastRemoteObject implements Initializable, Serializable, IAuctionListener {

    private static final long serialVersionUID = 1L;
    @FXML
    private TableView<ItemObs> itemTableView;

    @FXML
    private TableColumn<ItemObs, String> itemNameColumn;

    @FXML
    private TableColumn<ItemObs, String> currentPriceColumn;

    @FXML
    private TableColumn<ItemObs, String> bidderColumn;

    @FXML
    private Button bidButton;

    @FXML
    private TextField bidderNameTextField;

    @FXML
    private Label bidderNameLabel;

    @FXML
    private Label strategyLabel;

    @FXML
    private ChoiceBox<?> strategyChoiceBox;

    @FXML
    private Label bidAmountLabel;

    @FXML
    private TextField bidTextField;

    @FXML
    private TextArea messageBoxTextArea;

    private ObservableList<ItemObs> items = FXCollections.observableArrayList();

    private IAuctionServer auctionServer;

    private String messageFeed = "";

    public HomeController() throws RemoteException {
    }

    @Override
    public void update(Item item) {
        ItemObs updatedItem = items.stream()
                .filter(itemObs -> item.getItemName().equals(itemObs.getItemName().getValue()))
                .findFirst()
                .orElse(null);

        updatedItem.setCurrentBidderName(new SimpleStringProperty(item.getCurrentBidderName()));
        updatedItem.setCurrentBid(new SimpleDoubleProperty(item.getCurrentBid()));
        itemTableView.refresh();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connectClient();
        getItems();
        itemTableView.setItems(items);

        itemNameColumn.setCellValueFactory(item -> item.getValue().getItemName());
        currentPriceColumn.setCellValueFactory(item -> item.getValue().getCurrentBid().asString());
        bidderColumn.setCellValueFactory(item -> item.getValue().getCurrentBidderName());

        messageBoxTextArea.setWrapText(true);


        bidButton.setOnAction(event -> {
            try {
                placeBid();
            } catch (NoItemSelectedException | NoBidAmountException | NoBidderNameException | BidTooLowException e) {
                setMessage(e.getMessage());
            } catch (RemoteException e) {

            }
        });


    }

    private void connectClient() {
        String name = "english";
        try {
            Registry registry = LocateRegistry.getRegistry();
            auctionServer = (IAuctionServer) registry.lookup(name);

            if (auctionServer == null) {
                throw new AuctionHouseNotConnectedException("Could not connect to auction house");
            }

        } catch (RemoteException | AuctionHouseNotConnectedException | NotBoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void getItems() {
        try {
            Item[] itemArray = auctionServer.getItems();
            for (Item item : itemArray) {
                items.add(ItemMapper.itemToItemObs(item));
                auctionServer.registerListener(this, item.getItemName());
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void setMessage(String message) {
        messageFeed += message + "\n";
        messageBoxTextArea.setText(messageFeed);
    }

    private void placeBid() throws NoItemSelectedException, NoBidAmountException, RemoteException, NoBidderNameException, BidTooLowException {
        ItemObs item = itemTableView.getSelectionModel().getSelectedItem();

        if (item == null) {
            throw new NoItemSelectedException("No item was selected");
        }

        if (bidderNameTextField.getText().isEmpty()) {
            throw new NoBidderNameException("Bidder name is empty");
        }

        if (bidTextField.getText().isEmpty()) {
            throw new NoBidAmountException("Amount of bid is not set");
        }

        if (getBidValue() <= item.getCurrentBid().getValue()) {
            throw new BidTooLowException("Bid has to be higher than existing one");
        }

        auctionServer.bidOnItem(bidderNameTextField.getText(),
                item.getItemName().getValue(),
                getBidValue());
        setMessage("Bid on " + item.getItemName().getName()
                + " placed (" + getBidValue() + ")");
    }

    private double getBidValue() {
        return Double.valueOf(bidTextField.getText());
    }

//    class ItemUpdater extends UnicastRemoteObject implements Serializable,IAuctionListener{
//
//        ItemUpdater() throws RemoteException {
//        }
//
//        @Override
//        public void update(Item item) throws RemoteException {
//            ItemObs updatedItem = items.stream()
//                    .filter(itemObs -> item.getItemName().equals(itemObs.getItemName().getValue()))
//                    .findFirst()
//                    .orElse(null);
//
//            updatedItem.setCurrentBidderName(new SimpleStringProperty(item.getCurrentBidderName()));
//            updatedItem.setCurrentBid(new SimpleDoubleProperty(item.getCurrentBid()));
//            itemTableView.refresh();
//        }
//    }
}


