package bd.bd.controllers;

import bd.bd.UserDAO;
import bd.bd.UserDAOImpl;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChangePasswordController {
    public PasswordField newPasswordField;
    public PasswordField confirmPasswordField;
    public TextField loginTextField;
    private final UserDAO userDAO = new UserDAOImpl();

    public void handleChangePassword(ActionEvent actionEvent) {
        String login = loginTextField.getText();
        String newPas= newPasswordField.getText();
        String confPas=confirmPasswordField.getText();
        if(!newPas.equals(confPas))
        {
            Alerts.showErr("Пароли не совпадают");
            return;
        }
        if(newPas.isEmpty() || confPas.isEmpty() || login.isEmpty())
        {
            Alerts.showErr("Не все поля заполнены.");
            return;
        }
        userDAO.chacngePassword(login,newPas);
    }
}
