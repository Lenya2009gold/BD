package bd.bd.controllers;

import bd.bd.MainApplication;
import bd.bd.UserDAO;
import bd.bd.UserDAOImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddNewUserController {
    public Button registerButton;
    public PasswordField confirmPasswordField;
    public PasswordField passwordField;
    public TextField loginField;
    public TextField nameField;
    public Button backButton;
    public ComboBox<String> roleComboBox;
    private final UserDAO userDAO = new UserDAOImpl();
    private MainApplication app;
    public void setApp(MainApplication app) {
        this.app = app;
    }
    @FXML
    public void initialize() {
        roleComboBox.getItems().addAll("user", "admin" );
        roleComboBox.setValue("user");
    }
    public void registerButtonAction(ActionEvent actionEvent) {

        String user=loginField.getText();
        String name=nameField.getText();
        String pass = passwordField.getText();
        String second = confirmPasswordField.getText();
        String role = roleComboBox.getValue();
        userDAO.registerUser(pass,  user,  name,  role, second);


    }
}
