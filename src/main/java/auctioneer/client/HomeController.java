package auctioneer.client;

import auctioneer.interfaces.IAuctionListener;
import auctioneer.model.Item;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class HomeController implements IAuctionListener, Initializable {

    @FXML
    private TableView<Item> itemTableView;

    @FXML
    private TableColumn<Item, String> idColumn;

    @FXML
    private TableColumn<Item, String> itemNameColumn;

    @FXML
    private TableColumn<Item, String> currentPriceColumn;

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
    private Spinner<?> bidAmountSpinner;

    @FXML
    private TextArea messageBoxTextArea;

    @Override
    public void update(Item item) throws RemoteException {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        itemNameColumn.setCellValueFactory(item -> item.getValue().getItemName());
        //TODO create observable items
    }
}
