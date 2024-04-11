package bd.bd;

import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.*;
import java.time.LocalDate;

public class AddRecordDialogController {
    public DatePicker datePicker;
    public Button addButton;
    public ComboBox<Detail>  detailsNumberComboBox;
    //public ComboBox<Provider>  providerComboBox;
    public ComboBox<Customer>   customerPhoneComboBox;
    public TextField amountTextField;
    public TextField searchTextField;
    @FXML
    private ComboBox<String> statusComboBox;
    public ComboBox<Employee>  employeeComboBox;
    private MainApplication app;
    public void setApp(MainApplication app) {
        this.app = app;
    }
    private ObservableList<Detail> allDetails = FXCollections.observableArrayList();
    private FilteredList<Detail> filteredDetails;
    @FXML
    public void initialize() {
        statusComboBox.getItems().addAll("Новый", "В обработке", "Отправлен","Доставлен", "Отменен" );
        statusComboBox.setValue("Новый");

        loadCustomers();
        loadEmployee();
        loadDetails();
        //loadProvider();
        BooleanBinding isFormIncomplete = datePicker.valueProperty().isNull()
                .or(statusComboBox.valueProperty().isNull())
                .or(customerPhoneComboBox.valueProperty().isNull())
                .or(employeeComboBox.valueProperty().isNull())
                .or(detailsNumberComboBox.valueProperty().isNull())
                .or(amountTextField.textProperty().isEmpty());
        addButton.disableProperty().bind(isFormIncomplete);
//        filteredDetails = new FilteredList<>(allDetails, p -> true);
//        detailsNumberComboBox.setItems(filteredDetails);
//        detailsNumberComboBox.setEditable(true); // Разрешить редактирование
//        // Добавьте слушатель на ввод в ComboBox
//        detailsNumberComboBox.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
//            filterDetails(newValue);
//        });
//        if (!filteredDetails.isEmpty()) {
//            detailsNumberComboBox.getSelectionModel().selectFirst();
//        } else {
//            detailsNumberComboBox.getSelectionModel().clearSelection();
//        }

    }

    public void handleCancel(ActionEvent actionEvent) {
        app.switchWindow("userWindow.fxml", "Primary window");
    }

    private void filterDetails(String input) {
        filteredDetails.setPredicate(detail -> {
            // Если фильтр пустой, отображаем все детали
            if (input == null || input.isEmpty()) {
                return true;
            }
            // Сравниваем номер детали с вводом
            String lowerCaseFilter = input.toLowerCase();
            if (detail.getDetailsNumber().toLowerCase().contains(lowerCaseFilter)) {
                return true; // Фильтр совпадает
            }
            return false; // Не совпадает
        });
    }
    public void handleAdd(ActionEvent actionEvent) {

        LocalDate localDate = datePicker.getValue();
        Date sqlDate = Date.valueOf(localDate);
        String status = statusComboBox.getValue();
        Customer selectedCustomer = customerPhoneComboBox.getValue();
        Employee selectedEmployee = employeeComboBox.getValue();
       Detail selectedDetails=detailsNumberComboBox.getValue();
        //Provider selectedProvider=providerComboBox.getValue();
        int orderAmount = Integer.parseInt(amountTextField.getText());
       // String updateAmountSQL = "UPDATE \"Detail\" SET \"Amount\" = \"Amount\" - ? WHERE \"Detail_id\" = ?;";
        //String insertOrderSQL = "INSERT INTO \"Order\" (\"Datetime\", \"Status\", \"Customer_id\", \"Employee_id\") VALUES (?, ?, ?, ?) RETURNING \"Order_id\";";
        //String getMaxIdSQL = "SELECT MAX(\"Detail_order_id\") FROM \"Detail_order\";";
        int availableAmount = 0;
        //String insertDetailOrderSQL = "INSERT INTO \"Detail_order\" (\"Order_id\", \"Detail_id\") VALUES (?, ?);";
        try (Connection conn = DatabaseHandler.connect()) {
            String checkAmountSQL = "SELECT p.\"Amount_available\"\n" +
                    "FROM \"Provider\" p\n" +
                    "JOIN \"Detail\" dp ON p.\"Provider_id\" = dp.\"Provider_id\"\n" +
                    "WHERE dp.\"Detail_id\" = ?;\n";
            String updateAmountSQL = "UPDATE \"Detail_order\" SET \"amount\" =  ? WHERE \"Detail_order_id\" = ?;";
/*            try (PreparedStatement checkAmount = conn.prepareStatement(checkAmountSQL)) {

                checkAmount.setInt(1,selectedDetails.getId());
                ResultSet rs = checkAmount.executeQuery();
                while (rs.next())
                {
                    availableAmount=rs.getInt("Amount_available");
                }
                if(availableAmount >= orderAmount) {
                    try (PreparedStatement updateAmountStmt = conn.prepareStatement(updateAmountSQL)) {
                        updateAmountStmt.setInt(1, orderAmount);
                        updateAmountStmt.setInt(2, selectedDetails.getId());
                        updateAmountStmt.executeUpdate();

                    } catch (SQLException e) {
                        Alerts.showErrSQL(e.getMessage());
                    }
                }
                else
                {

                    Alerts.showErr("У поставщика меньше запчастей");
                    return;
                }
            } catch (SQLException e)
            {
                Alerts.showErrSQL(e.getMessage());
            }*/
            // Вставка заказа
            String insertOrderSQL = "INSERT INTO \"Order\" (\"Datetime\", \"Status\", \"Customer_id\", \"Employee_id\") VALUES (?, ?, ?, ?) RETURNING \"Order_id\";";
            int orderId;
            try (PreparedStatement insertOrderStmt = conn.prepareStatement(insertOrderSQL, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement checkAmountpstmt = conn.prepareStatement(checkAmountSQL)) {

                checkAmountpstmt.setInt(1, selectedDetails.getId());
                ResultSet rs = checkAmountpstmt.executeQuery();
                while (rs.next()) {
                    availableAmount = rs.getInt("Amount_available");
                }
                if (availableAmount >= orderAmount)
                {
                    insertOrderStmt.setDate(1, sqlDate);
                    insertOrderStmt.setString(2, status);
                    insertOrderStmt.setInt(3, selectedCustomer.getId());
                    insertOrderStmt.setInt(4, selectedEmployee.getId());
                    insertOrderStmt.executeUpdate();
                    try (ResultSet rsOrder = insertOrderStmt.getGeneratedKeys()) {
                        if (rsOrder.next()) {
                            orderId = rsOrder.getInt(1);
                        } else {
                            Alerts.showErr("Не удалось создать заказ.");
                            return;
                        }
                    }


                    // Вставка связи заказа с деталью
                    String insertDetailOrderSQL = "INSERT INTO \"Detail_order\" (\"Order_id\", \"Detail_id\", \"amount\") VALUES (?, ?, ?);";
                    try (PreparedStatement insertDetailOrderStmt = conn.prepareStatement(insertDetailOrderSQL)) {
                        insertDetailOrderStmt.setInt(1, orderId);
                        insertDetailOrderStmt.setInt(2, selectedDetails.getId());
                        insertDetailOrderStmt.setInt(3, orderAmount);
                        insertDetailOrderStmt.executeUpdate();
                        Alerts.showSuc("Всё добавлено");
                    }
                }
                else
                {
                    Alerts.showErr("У поставщика меньше запчастей");
                    return;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Alerts.showErrSQL(e.getMessage());
        }
    }
    public void handleAddNewCustomer(ActionEvent actionEvent) {
        app.switchWindow("AddRecordDialogNewCustomer.fxml", "Добавление нового клиента");
    }
    public void loadCustomers()
    {
        ObservableList<Customer> customer = FXCollections.observableArrayList();
        String SQL_SELECT = "SELECT * FROM \"Customer\"  ";
        try (Connection conn = DatabaseHandler.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL_SELECT)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                customer.add(new Customer(rs.getInt("Customer_id"),
                        rs.getString("Name"),
                        rs.getString("Phone")));

            }
            customerPhoneComboBox.setItems((customer));
            customerPhoneComboBox.setCellFactory(cell -> new ListCell<Customer>() {
                @Override
                protected void updateItem(Customer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getName() + " (" + item.getPhone() + ")");
                    }
                }
            });

            // Настройка отображения выбранного элемента
            customerPhoneComboBox.setButtonCell(new ListCell<Customer>() {
                @Override
                protected void updateItem(Customer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getName() + " (" + item.getPhone() + ")");
                    }
                }
            });
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }
    public void loadEmployee()
    {
        ObservableList<Employee> employee = FXCollections.observableArrayList();
        String SQL_SELECT = "SELECT * FROM \"Employee\"  ";
        try (Connection conn = DatabaseHandler.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL_SELECT)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                employee.add(new Employee(rs.getInt("employee_id"),
                        rs.getString("firstName"),
                        rs.getString("secondName")));

            }
            employeeComboBox.setItems((employee));
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    public void loadDetails() {

        String SQL_SELECT = " SELECT d.\"Detail_id\", d.\"DetailsNumber\", d.\"Manufactur\", p.\"Name\" AS \"ProviderName\", p.\"Amount_available\"\n" +
                "    FROM \"Detail\" d\n" +
                "    JOIN \"Provider\" p ON d.\"Provider_id\" = p.\"Provider_id\"";

        try (Connection conn = DatabaseHandler.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL_SELECT))
        {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                allDetails.add(new Detail(rs.getInt("detail_id"),
                        rs.getString("detailsNumber"),
                        rs.getInt("amount_available"),
                        rs.getString("Manufactur"),
                        rs.getString("ProviderName")));

            }
            detailsNumberComboBox.setItems((allDetails));
            // Настройка отображения элементов в выпадающем списке
            detailsNumberComboBox.setCellFactory(cell -> new ListCell<Detail>() {
                @Override
                protected void updateItem(Detail item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getDetailsNumber() + " производитель - " + item.getManufactur()+ " поставщик - " + item.getProviderName());
                    }
                }
            });
            // Настройка отображения выбранного элемента
            detailsNumberComboBox.setButtonCell(new ListCell<Detail>() {
                @Override
                protected void updateItem(Detail item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getDetailsNumber()+ " производитель - " + item.getManufactur() + " поставщик - " + item.getProviderName());
                    }
                }
            });
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        filteredDetails = new FilteredList<>(allDetails, p -> true); // Инициализируем фильтрованный список

        // Слушатель изменений текста в поле поиска
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredDetails.setPredicate(detail ->
            {
                // Если поле поиска пустое, показываем все детали
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Проверяем, содержит ли наименование детали текст из поля поиска
                String lowerCaseFilter = newValue.toLowerCase();
                if (detail.getDetailsNumber().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (detail.getManufactur().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false; // Не показываем деталь
            });
        });

        detailsNumberComboBox.setItems(filteredDetails);
    }
}
