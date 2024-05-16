package bd.bd.controllers;

import bd.bd.DetailProviderDAO;
import bd.bd.DetailProviderDAOImpl;
import bd.bd.DetailProviderData;
import bd.bd.MainApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DetailWindowController {

    public Button createOrderButton;
    public TextField searchField;
    @FXML
    private TableView<DetailProviderData> detailsTable;
    @FXML
    private TableColumn<DetailProviderData, String> detailsNumberColumn;
    @FXML
    private TableColumn<DetailProviderData, String> manufacturColumn;
    @FXML
    private TableColumn<DetailProviderData, Number> amountColumn;
    @FXML
    private TableColumn<DetailProviderData, String> providerNameColumn;
    @FXML
    private TableColumn<DetailProviderData, String> deliveryColumn;
    @FXML
    private TableColumn<DetailProviderData, Number> priceColumn;
    private final DetailProviderDAO detailProviderDAO = new DetailProviderDAOImpl();
    private MainApplication app;
    public void setApp(MainApplication app) {
        this.app = app;
    }
    ObservableList<DetailProviderData> detailsproviders= FXCollections.observableArrayList();

    public void initialize()
    {
        detailsNumberColumn.setCellValueFactory(new PropertyValueFactory<>("detailsNumber"));
        manufacturColumn.setCellValueFactory(new PropertyValueFactory<>("manufactur"));
        amountColumn.setCellValueFactory(new  PropertyValueFactory<>("amount"));
        providerNameColumn.setCellValueFactory(new PropertyValueFactory<>("providerName"));
        deliveryColumn.setCellValueFactory(new PropertyValueFactory<>("detailDelivery"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        loadDetails();
    }
    protected void loadDetails() {
        detailsproviders.clear();
        detailsproviders.addAll(detailProviderDAO.loadDetails());
    }


    public void handleSearch(ActionEvent actionEvent) {
        String searchNubmer;
        searchNubmer = searchField.getText().toUpperCase();
        if(searchNubmer.isEmpty())
        {
            detailsTable.setItems(detailsproviders);
        }
        else
        {
            ObservableList <DetailProviderData> filteredlist = FXCollections.observableArrayList();
            for (DetailProviderData item : detailsproviders )
                if(item.getDetailsNumber().toUpperCase().contains(searchNubmer))
                {
                    filteredlist.add(item);
                }
            detailsTable.setItems(filteredlist);
        }

    }

    public void handleAddDetail(ActionEvent actionEvent) {
        setApp(app);
        app.newWindow("Добавление новой детали","AddNewDetail.fxml");
    }

    public void handleAddProvider(ActionEvent actionEvent) {
    }
}


