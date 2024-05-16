package bd.bd.controllers;

import bd.bd.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;


import java.util.Optional;

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
    private final OrderDAO orderDAO = new OrderDAOImpl();
    public TextField userInput;
    public TextArea botResponse;
    private Button deleteButton;
    private MainApplication app;
    public void setApp(MainApplication app) {
        this.app = app;
    }
    @FXML
    private TableView<OrderData> databaseTable;
    private final CustomerDAO customerDAO= new CustomerDAOImpl();


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
                orderDAO.deleteOrderFromDatabase(selectedOrder.getOrderId());
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
        setApp(app);
        app.newWindow("Детали","/bd/bd/DetailWindow.fxml");
    }

    @FXML
    private void handleShowAllRecords() {
        databaseTable.setItems(orderDAO.loadOrderData());
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
            customerDAO.updateOrderStatusInDatabase(orderData.getOrderId(), newStatus);
        });
    }
    private void setupPhoneColumnForEditing() {
        columnCustomerPhone.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
        columnCustomerPhone.setCellFactory(TextFieldTableCell.forTableColumn());
        columnCustomerPhone.setOnEditCommit((TableColumn.CellEditEvent<OrderData, String> t) -> {
            OrderData phone = t.getTableView().getItems().get(t.getTablePosition().getRow());
            phone.setCustomerPhone(t.getNewValue());
            customerDAO.updatePhoneInDatabase(phone.getOrderId(), t.getNewValue());
        });
    }
    private void setupCustomerNameForEditing()
    {
        columnCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        columnCustomerName.setCellFactory(TextFieldTableCell.forTableColumn());
        columnCustomerName.setOnEditCommit((TableColumn.CellEditEvent<OrderData, String> t)-> {
            OrderData name = t.getTableView().getItems().get(t.getTablePosition().getRow());
            name.setCustomerName(t.getNewValue());
            customerDAO.updateCustomerNameDatabase(name.getOrderId(),t.getNewValue());
                }
        );

    }
    public void handleUserInput(ActionEvent actionEvent) {
    }
}
