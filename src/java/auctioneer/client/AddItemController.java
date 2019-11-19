package auctioneer.client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Paint;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class AddItemController implements Initializable {

    @FXML
    private TextField itemNameTextField;

    @FXML
    private TextField ownerTextField;

    @FXML
    private TextField startingBidTextField;

    @FXML
    private TextField expirationTimeTextField;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private Label itemNameLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label ownerLabel;

    @FXML
    private Label startingBidLabel;

    @FXML
    private Label expirationTimeLabel;

    @FXML
    private Button addButton;

    @FXML
    private Button cancelButton;

    private HomeController homeController;

    private List<Label> labels;

    private List<TextField> textFields;

    public void setHomeController(HomeController homeController) {
        this.homeController = homeController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        startingBidTextField.setTextFormatter(getDoubleFormatter());
        expirationTimeTextField.setTextFormatter(getIntegerFormatter());

        cancelButton.setOnAction(event -> homeController.closeAddItemWindow());
        addButton.setOnAction(event -> {
            if (isValid()) {
                homeController.addItemToAuction(ownerTextField.getText(),
                        itemNameTextField.getText(),
                        itemNameTextField.getText(),
                        Double.valueOf(startingBidTextField.getText()),
                        Integer.parseInt(expirationTimeTextField.getText()));
            }
            setLabelDefaultColor();
            clearFields();
            homeController.closeAddItemWindow();
        });

        labels = Arrays.asList(
                itemNameLabel,
                descriptionLabel,
                ownerLabel,
                startingBidLabel,
                expirationTimeLabel
        );

        textFields = Arrays.asList(
                itemNameTextField,
                ownerTextField,
                startingBidTextField,
                expirationTimeTextField
        );

    }

    private boolean isValid() {
        boolean isValid = true;
        if (itemNameTextField.getText().isEmpty()) {
            itemNameLabel.setTextFill(Paint.valueOf("red"));
            isValid = false;
        }
        if (descriptionTextArea.getText().isEmpty()) {
            descriptionLabel.setTextFill(Paint.valueOf("red"));
            isValid = false;
        }
        if (ownerTextField.getText().isEmpty()) {
            ownerLabel.setTextFill(Paint.valueOf("red"));
            isValid = false;
        }
        if (startingBidTextField.getText().isEmpty()) {
            startingBidLabel.setTextFill(Paint.valueOf("red"));
            isValid = false;
        }
        if (expirationTimeTextField.getText().isEmpty()) {
            expirationTimeLabel.setTextFill(Paint.valueOf("red"));
            isValid = false;
        }
        return isValid;
    }

    private void setLabelDefaultColor() {
        for (Label label : labels) {
            label.setTextFill(Paint.valueOf("black"));
        }
    }

    private void clearFields() {
        for (TextField field : textFields) {
            field.setText("");
        }
        descriptionTextArea.setText("");
    }

    private TextFormatter<Integer> getIntegerFormatter() {
        return new TextFormatter<>(
                new IntegerStringConverter(),
                0,
                c -> Pattern.matches("\\d*", c.getText()) ? c : null);
    }

    private TextFormatter<Double> getDoubleFormatter() {
        return new TextFormatter<Double>(
                new DoubleStringConverter(),
                0D,
                c -> Pattern.matches("\\d*", c.getText()) ? c : null);
    }

}

