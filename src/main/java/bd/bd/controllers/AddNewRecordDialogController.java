package bd.bd.controllers;

import bd.bd.CustomerDAO;
import bd.bd.CustomerDAOImpl;
import bd.bd.MainApplication;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.sql.*;
import java.util.Optional;

public class AddNewRecordDialogController {
    public TextField nameTextField;
    public TextField phoneTextField;
    public TextField brdAuto;
    public TextField brandAuto;
    public TextField modelAuto;
    public TextField vinAuto;
    public Button addButton;
    private final CustomerDAO customerDAO = new CustomerDAOImpl();
    public DatePicker datePicker;
    public ComboBox statusComboBox;
    public ComboBox customerPhoneComboBox;
    public ComboBox employeeComboBox;
    public TextField searchTextField;
    public ComboBox detailsNumberComboBox;
    public TextField amountTextField;
    private MainApplication app;
    public void setApp(MainApplication app) {
        this.app = app;
    }
    public void initialize() {
        setApp(app);
        BooleanBinding isFormIncomplete = nameTextField.textProperty().isEmpty()
                .or(phoneTextField.textProperty().isEmpty())
                .or(brdAuto.textProperty().isEmpty())
                .or(modelAuto.textProperty().isEmpty())
                .or(vinAuto.textProperty().isEmpty())
                .or(brdAuto.textProperty().isEmpty());

        addButton.disableProperty().bind(isFormIncomplete);
    }


    public void handleCancel(ActionEvent actionEvent) {
        setApp(app);
        app.switchWindow("AddRecordDialog.fxml","Добавить заказ");

    }

    public void handleAdd(ActionEvent actionEvent) {
        setApp(app);
        String nameCustomer = nameTextField.getText();
        String phoneCustomer = phoneTextField.getText();
        String vinAutoSQL = vinAuto.getText();
        String dateAuto = brdAuto.getText();
        String brandAutoSQL = brandAuto.getText();
        String modelAutoSQL = modelAuto.getText();
        Alert conf = new Alert(Alert.AlertType.CONFIRMATION);
        conf.setTitle("Подтверждение");
        conf.setHeaderText(null);
        conf.setContentText("Вы точно хотите добавить?");
        Optional<ButtonType> action = conf.showAndWait();
        if ((action.isPresent()) && (action.get() == ButtonType.OK)) {
            customerDAO.addCustomer(nameCustomer,phoneCustomer,vinAutoSQL,dateAuto,brandAutoSQL,modelAutoSQL);
        }
    }

    public void handleAddNewCustomer(ActionEvent actionEvent) {
    }
}
