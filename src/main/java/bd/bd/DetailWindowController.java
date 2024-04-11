package bd.bd;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
//        String SQL_SELECT="SELECT \"Detail\".\"DetailsNumber\", \"Detail\".\"Manufactur\", \"Detail\".\"Amount\", \n" +
//                "       \"Provider\".\"Name\" AS \"ProviderName\", \"Provider\".\"Detail_delivery\", \"Provider\".\"Price\"\n" +
//                "FROM \"Detail\"\n" +
//                "JOIN \"Provider\" ON \"Detail\".\"Provider_id\" = \"Provider\".\"Provider_id\";\n";
        String SQL_SELECT = "SELECT \"Detail\".\"DetailsNumber\", \"Detail\".\"Manufactur\", " +
                "\"Provider\".\"Name\" AS \"ProviderName\", \"Provider\".\"Detail_delivery\", \"Provider\".\"Price\", \"Provider\".\"Amount_available\"\n" +
                "FROM \"Detail\"\n" +
                "JOIN \"Provider\" ON \"Detail\".\"Provider_id\" = \"Provider\".\"Provider_id\";";

        try(Connection conn = DatabaseHandler.connect();
            PreparedStatement pstmt = conn.prepareStatement(SQL_SELECT))
        {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next())
            {
                detailsproviders.add(new DetailProviderData(rs.getString("detailsNumber"),
                        rs.getString("manufactur"),
                        rs.getInt("Amount_available"),
                        rs.getInt("price"),
                        rs.getString("providerName"),
                        rs.getString("Detail_delivery")));
            }
            detailsTable.setItems(detailsproviders);

        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            Alerts.showErrSQL(e.getMessage());
        }


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
}


