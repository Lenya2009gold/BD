package bd.bd.controllers;

import bd.bd.MainApplication;
import bd.bd.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

public class AuthenticationController {
    public Button registerButton;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button signInButton;

    private MainApplication app;
    public void setApp(MainApplication app) {
        this.app = app; // Устанавливаем ссылку на основное приложение
    }

    public void signInButtonAction(ActionEvent actionEvent) {
        setApp(app);
        String username = usernameField.getText();
        String password = passwordField.getText();
        DatabaseHandler dbHandler = new DatabaseHandler();
        User user = dbHandler.authenticateUser(username, password);
        if(username.isEmpty() || password.isEmpty())
        {
            Alerts.showErr("Введите данные для входа.");
            return;
        }
        if (user != null) {
            switch (user.getRole())
            {
                case "admin":
                    app.switchWindow("adminWindow.fxml", "Admin Dashboard");
                    break;
                case "user":
                    app.switchWindow("userWindow.fxml", "User Dashboard");
                    break;
                default:
                    Alerts.showErr("Вы кто?");
                    break;

            }
        }
    }
    public void registerButtonAction(ActionEvent actionEvent) {
        setApp(app);
        app.switchWindow("Registration.fxml","Регистрация");
    }
}
