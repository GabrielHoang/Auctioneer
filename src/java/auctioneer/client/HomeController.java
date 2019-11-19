package auctioneer.client;

import auctioneer.client.utils.*;
import auctioneer.interfaces.IAuctionListener;
import auctioneer.interfaces.IAuctionServer;
import auctioneer.model.Item;
import auctioneer.model.ItemObs;
import auctioneer.server.utils.BidTooLowException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.DoubleStringConverter;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

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
    private ChoiceBox<String> strategyChoiceBox;

    @FXML
    private TextField bidTextField;

    @FXML
    private TextField maxBidTextField;

    @FXML
    private TextArea messageBoxTextArea;

    @FXML
    private Button addItemButton;

    private ObservableList<ItemObs> items = FXCollections.observableArrayList();

    private IAuctionServer auctionServer;

    private String messageFeed = "";

    private ObservableList<String> strategies = FXCollections.observableArrayList();

    private Timeline tableRefresher = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
        getItems();
    }));

    private Stage addItemStage;

    private AddItemController addItemController;

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
        tableRefresher.setCycleCount(Timeline.INDEFINITE);
        tableRefresher.play();

        itemNameColumn.setCellValueFactory(item -> item.getValue().getItemName());
        currentPriceColumn.setCellValueFactory(item -> item.getValue().getCurrentBid().asString());
        bidderColumn.setCellValueFactory(item -> item.getValue().getCurrentBidderName());

        messageBoxTextArea.setWrapText(true);

        strategies.addAll("auto bidder", "last minute", "manual");
        strategyChoiceBox.setItems(strategies);
        strategyChoiceBox.setValue("manual");

        maxBidTextField.setTextFormatter(getDoubleFormatter());
        maxBidTextField.setEditable(true);
        strategyChoiceBox.setOnAction(event -> {
            if (strategyChoiceBox.getSelectionModel().getSelectedItem().equals("auto bidder")) {
//                AutoBidder autoBidder = new AutoBidder();
//                autoBidder.activate();
            }
        });

        bidTextField.setTextFormatter(getDoubleFormatter());
        bidButton.setOnAction(event -> {
            try {
                placeBid();
            } catch (NoItemSelectedException | NoBidAmountException | NoBidderNameException | BidTooLowException e) {
                setMessage(e.getMessage());
            } catch (RemoteException e) {

            }
        });

        initializeItemAddWindow();

        addItemButton.setOnAction(event -> addItemStage.show());

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
            items.clear();
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

    public void closeAddItemWindow() {
        addItemStage.close();
        initializeItemAddWindow();
    }

    public void addItemToAuction(String ownerName, String itemName,
                                 String itemDesc, double startBid, int auctionTime) {
        try {
            auctionServer.placeItemForBid(ownerName, itemName, itemDesc, startBid, auctionTime);
            auctionServer.registerListener(this, itemName);
            getItems();
        } catch (RemoteException e) {
            setMessage(e.getMessage());
        }
    }

    private void initializeItemAddWindow() {
        try {
            addItemStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addItem.fxml"));
            addItemStage.setTitle("Add new auction item");
            addItemStage.initModality(Modality.APPLICATION_MODAL);
            addItemStage.setScene(new Scene(loader.load()));

            this.addItemController = loader.getController();
            addItemController.setHomeController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private TextFormatter<Double> getDoubleFormatter() {
        return new TextFormatter<Double>(
                new DoubleStringConverter(),
                0D,
                c -> Pattern.matches("\\d*", c.getText()) ? c : null);
    }

//    class AutoBidder implements Strategy {
//
//        ObservableList<ItemObs> trackedItems;
//        String bidder;
//        Double maxBid;
//
//        @Override
//        public void activate() {
//            bidder = bidderNameTextField.getText();
//            maxBid = Double.parseDouble(maxBidTextField.getText());
//            getItems();
//            items.stream()
//                    .filter(item -> item.getCurrentBidderName().getValue().equals(bidder))
//                    .map(item -> trackedItems.add(item));
//
//            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
//            executor.scheduleAtFixedRate(upbid,0, 2, TimeUnit.SECONDS);
//        }

//        Runnable upbid = () -> {
//            System.out.println("Running upbid");
//            getItems();
//            for (ItemObs item : items) {
//                for (ItemObs tracked :trackedItems) {
//                    if (item.getItemName().getValue().equals(tracked.getItemName().getValue())
//                    && !item.getCurrentBidderName().getValue().equals(tracked.getCurrentBidderName().getValue())) {
//                        try {
//                            auctionServer.bidOnItem(bidder, tracked.getItemName().getValue(), item.getCurrentBid().doubleValue()+1);
//                        } catch (RemoteException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        };
//    }
}


