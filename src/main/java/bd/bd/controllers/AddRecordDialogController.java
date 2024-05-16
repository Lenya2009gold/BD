package bd.bd.controllers;

import bd.bd.*;
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
    private final CustomerDAOImpl customerDAO;
    private final EmployeeDAOImpl employeeDAO;
    private final DetailDAOImpl detailDAO;
    private final OrderDAOImpl orderDAO;
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

    public AddRecordDialogController() {
        this.customerDAO = new CustomerDAOImpl();
        this.employeeDAO=new EmployeeDAOImpl();
        this.detailDAO = new DetailDAOImpl();
        this.orderDAO=new OrderDAOImpl();
    }

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
        int orderAmount = Integer.parseInt(amountTextField.getText());
        detailDAO.insertDetailOrder(selectedDetails.getId(),orderAmount,sqlDate,status,selectedCustomer.getId(),selectedEmployee.getId());
    }
    public void handleAddNewCustomer(ActionEvent actionEvent) {
        app.switchWindow("AddRecordDialogNewCustomer.fxml", "Добавление нового клиента");
    }
    public void loadCustomers() {
        ObservableList<Customer> customers = customerDAO.getAllCustomers();
        customerPhoneComboBox.setItems(customers);
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
    }
    public void loadEmployee()
    {
        ObservableList<Employee> employee = employeeDAO.loadEmployee();
        employeeComboBox.setItems((employee));
    }
    public void loadDetails() {

            allDetails.addAll(detailDAO.loadDetails());
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
