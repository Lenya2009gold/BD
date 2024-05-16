package bd.bd.controllers;

import bd.bd.MainApplication;
import bd.bd.UserDAO;
import bd.bd.UserDAOImpl;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistrationController {
    public TextField nameField;
    public PasswordField passwordField;
    public PasswordField confirmPasswordField;
    public Button registerButton;
    public TextField loginField;
    private MainApplication app;
    private final UserDAO userDAO = new UserDAOImpl();
    public void setApp(MainApplication app) {
        this.app = app;
    }
@FXML
public void initialize() {
    setApp(app);
    BooleanBinding isFormIncomplete = nameField.textProperty().isEmpty()
            .or(passwordField.textProperty().isEmpty()
            .or(confirmPasswordField.textProperty().isEmpty())
            .or(loginField.textProperty().isEmpty()));
    registerButton.disableProperty().bind(isFormIncomplete);
}

    public void registerButtonAction(ActionEvent actionEvent) {
    setApp(app);
        String user=loginField.getText();
        String name=nameField.getText();
        String pass = passwordField.getText();
        String second = confirmPasswordField.getText();
        userDAO.registerUserWithOutRole(user,pass,name,second);
    }

    public void backButtonAction(ActionEvent actionEvent) {

        setApp(app);
        app.showLoginWindow();
    }
}

