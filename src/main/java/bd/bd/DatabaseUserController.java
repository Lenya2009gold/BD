package bd.bd;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.io.IOException;
import java.sql.*;
import java.util.Optional;

import static bd.bd.Alerts.showSuc;

public class DatabaseUserController {
    public TableColumn<OrderData, Integer> columnOrderId;
    public TableColumn<OrderData, String> columnDatetime;
    public TableColumn<OrderData, String> columnStatus;
    public TableColumn<OrderData, String> columnCustomerName;
    public TableColumn<OrderData, String> columnCustomerPhone;
    public TableColumn<OrderData, String> columnEmployeeName;
    public TableColumn<OrderData, String> columnDetailsNumber;
    public TableColumn<OrderData, Integer> columnAmount;
    public TableColumn<OrderData, String> columnProviderName;
    public TableColumn<OrderData, String> columnDetailDelivery;
    public TextField userInput;
    public TextArea botResponse;
    private Button deleteButton;
    private MainApplication app;
    public void setApp(MainApplication app) {
        this.app = app;
    }
    @FXML
    private TableView<OrderData> databaseTable;


    public void initialize() {
        columnOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        columnDatetime.setCellValueFactory(new PropertyValueFactory<>("datetime"));
        columnEmployeeName.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        columnDetailsNumber.setCellValueFactory(new PropertyValueFactory<>("detailsNumber"));
        columnAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        columnProviderName.setCellValueFactory(new PropertyValueFactory<>("providerName"));
        columnDetailDelivery.setCellValueFactory(new PropertyValueFactory<>("detailDelivery"));
        databaseTable.setEditable(true);
        setupStatusColumnForEditing();
        setupPhoneColumnForEditing();
        setupCustomerNameForEditing();
    }

    @FXML
    private Label titleLabel;

    @FXML
    protected void handleAddRecord() {
        app.switchWindow("AddRecordDialog.fxml", "Добавить запись");
    }
    @FXML
    protected void handleExit() {
        app.showLoginWindow();
    }

    @FXML
    protected void handleSearchRecords() {
        // Код для поиска записей в базе данных
    }

    @FXML
    protected void handleDeleteRecord() {
        OrderData selectedOrder = databaseTable.getSelectionModel().getSelectedItem();
        if(selectedOrder != null)
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Подтверждение удаления записи");
            alert.setHeaderText(null);
            alert.setContentText("Вы точно хотите удалить выбранную запись?");
            Optional<ButtonType> action = alert.showAndWait();
            if ((action.isPresent()) && (action.get() == ButtonType.OK)) {
                deleteOrderFromDatabase(selectedOrder.getOrderId());
                databaseTable.getItems().remove(selectedOrder);
            }
        }

        else
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Нет выбранной записи");
            alert.setContentText("Выберите запись");
            alert.showAndWait();
        }

    }


    @FXML
    protected void handleShowDetails(ActionEvent actionEvent) {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bd/bd/DetailWindow.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Детали");
            stage.setScene(scene);
            stage.getIcons().add(new Image("/Icon.png"));
            stage.show();
            DetailWindowController controller= loader.getController();
            controller.loadDetails();

        }  catch (IOException e) {
            System.out.println(e.getMessage());
            Alerts.showErr(e.getMessage());
        }

    }

    @FXML
    private void handleShowAllRecords() {
        loadOrderData();
    }
    public void loadOrderData()
    {
        ObservableList<OrderData> orders = FXCollections.observableArrayList();
        String SQL_SELECT = "SELECT\n" +
                "        o.\"Order_id\",\n" +
                "        o.\"Datetime\",\n" +
                "        o.\"Status\",\n" +
                "        c.\"Name\" AS Customer_name,\n" +
                "        c.\"Phone\" AS Customer_phone,\n" +
                "        e.\"FirstName\" AS Employee_first_name,\n" +
                "        det.\"DetailsNumber\",\n" +
                "        doo.\"amount\",\n" +
                "        p.\"Name\" AS Provider_name,\n" +
                "        p.\"Detail_delivery\"\n" +
                "    FROM\n" +
                "        \"Order\" o\n" +
                "        JOIN \"Customer\" c ON o.\"Customer_id\" = c.\"Customer_id\"\n" +
                "        JOIN \"Employee\" e ON o.\"Employee_id\" = e.\"Employee_id\"\n" +
                "        JOIN \"Detail_order\" doo ON o.\"Order_id\" = doo.\"Order_id\"\n" +
                "        JOIN \"Detail\" det ON doo.\"Detail_id\" = det.\"Detail_id\"\n" +
                "        JOIN \"Provider\" p ON det.\"Provider_id\" = p.\"Provider_id\";";

        try (Connection conn = DatabaseHandler.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL_SELECT)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                orders.add(new OrderData(rs.getInt("order_id"),
                        rs.getString("datetime"),
                        rs.getString("status"),
                        rs.getString("customer_name"),
                        rs.getString("customer_phone"),
                        rs.getString("employee_first_name"),
                        rs.getString("detailsnumber"),
                        rs.getInt("amount"),
                        rs.getString("provider_name"),
                        rs.getString("detail_delivery")));

            }
            databaseTable.setItems(orders); // Устанавливаем данные в таблицу
        } catch (SQLException ex) {
            Alerts.showErrSQL(String.valueOf(ex.getErrorCode()));
        }

    }

    private void setupStatusColumnForEditing() {
        columnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        ObservableList<String> statusOptions = FXCollections.observableArrayList(
                "Новый", "В обработке", "Отправлен", "Доставлен", "Отменен");
        columnStatus.setCellFactory(TextFieldTableCell.forTableColumn());
        columnStatus.setCellFactory(ComboBoxTableCell.forTableColumn(statusOptions));
        columnStatus.setOnEditCommit(event -> {
            OrderData orderData = event.getRowValue();
            String newStatus = event.getNewValue();
            orderData.setStatus(newStatus);
            updateOrderStatusInDatabase(orderData.getOrderId(), newStatus);
        });
    }
    private void setupPhoneColumnForEditing() {
        columnCustomerPhone.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
        columnCustomerPhone.setCellFactory(TextFieldTableCell.forTableColumn());
        columnCustomerPhone.setOnEditCommit((TableColumn.CellEditEvent<OrderData, String> t) -> {
            OrderData order = t.getTableView().getItems().get(t.getTablePosition().getRow());
            order.setCustomerPhone(t.getNewValue());
            updatePhoneInDatabase(order.getOrderId(), t.getNewValue());
        });
    }
    private void setupCustomerNameForEditing()
    {
        columnCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        columnCustomerName.setCellFactory(TextFieldTableCell.forTableColumn());
        columnCustomerName.setOnEditCommit((TableColumn.CellEditEvent<OrderData, String> t)-> {
            OrderData name = t.getTableView().getItems().get(t.getTablePosition().getRow());
            name.setCustomerName(t.getNewValue());
            updateCustomerNameDatabase(name.getOrderId(),t.getNewValue());
                }
        );

    }
    private void updateCustomerNameDatabase(int orderId, String newName)
    {
        String SQL_UPDATE = "UPDATE \"Customer\" SET \"Name\" = ? WHERE \"Customer_id\" = ?;";
        try (Connection conn = DatabaseHandler.connect();
        PreparedStatement pstmt = conn.prepareStatement(SQL_UPDATE)){
            pstmt.setString(1,newName);
            pstmt.setInt(2,orderId);
            pstmt.executeUpdate();
        }  catch (SQLException ex) {
            Alerts.showErrSQL(String.valueOf(ex.getErrorCode()));
        }

    }
    private void updateOrderStatusInDatabase(int orderId, String newStatus) {
        String SQL_UPDATE = "UPDATE \"Order\" SET \"Status\" = ? WHERE \"Order_id\" = ?;";
        try (Connection conn = DatabaseHandler.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL_UPDATE)) {
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, orderId);
            pstmt.executeUpdate();
        }  catch (SQLException ex) {
            Alerts.showErrSQL(String.valueOf(ex.getErrorCode()));
        }
    }
    private void updatePhoneInDatabase(int orderId, String newStatus) {
        String SQL_UPDATE = "UPDATE \"Customer\" SET \"Phone\" = ? WHERE \"Customer_id\" = ?;";
        try (Connection conn = DatabaseHandler.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL_UPDATE)) {
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, orderId);
            pstmt.executeUpdate();
        }  catch (SQLException ex) {
            Alerts.showErrSQL(String.valueOf(ex.getErrorCode()));
        }
    }
    private void deleteOrderFromDatabase(int orderID)
    {
        String SQL_DELETE = "DELETE FROM \"Order\" WHERE \"Order_id\" = ?;";
        try (Connection conn = DatabaseHandler.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL_DELETE))
        {
            pstmt.setInt(1,orderID);
            int affectedRows = pstmt.executeUpdate();
            if(affectedRows > 0 )
            {
                showSuc("All deleted");

            }
            else
            {
                Alerts.showErr("all bad");
            }


        }  catch (SQLException ex) {
            Alerts.showErrSQL(String.valueOf(ex.getErrorCode()));        }
    }

    public void handleWatchProviders(ActionEvent actionEvent) {
    }

    public void handleUserInput(ActionEvent actionEvent) {
    }
}
