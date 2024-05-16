package bd.bd.controllers;

import bd.bd.MainApplication;
import bd.bd.ProviderDAO;
import bd.bd.ProviderDAOImpl;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.util.Optional;

public class AddNewDetailController {
    public TextField detailNumberField;
    public TextField manufacturerField;
    public TextField priceField;
    public ComboBox providerComboBox;
    public TextField providerNameField;
    public TextField providerAddressField;
    public TextField providerContactField;
    public BorderPane mainPane;
    public TextField providerPriceField;
    public TextField providerDeliveryField;
    public TextField providerAmountField;
    ProviderDAO providerDAO = new ProviderDAOImpl();
    private MainApplication app;

    public void handleAddDetail(ActionEvent actionEvent) {
        String name = providerNameField.getText();
        String adress = providerAddressField.getText();
        String contact = providerContactField.getText();
        int price = Integer.parseInt(providerPriceField.getText());
        String deliv=providerDeliveryField.getText();
        int amount = Integer.parseInt(providerAmountField.getText());
        String detailNumber = detailNumberField.getText();
        String manufactur = manufacturerField.getText();
        Alert conf = new Alert(Alert.AlertType.CONFIRMATION);
        conf.setTitle("Подтверждение");
        conf.setHeaderText(null);
        conf.setContentText("Вы точно хотите добавить?");
        Optional<ButtonType> action = conf.showAndWait();
        if ((action.isPresent()) && (action.get() == ButtonType.OK)) {
            providerDAO.addProvider(name,adress,contact,deliv,price,amount,detailNumber,manufactur);
        }

    }

    public void setApp(MainApplication mainApplication) {
        this.app=app;
    }
}
