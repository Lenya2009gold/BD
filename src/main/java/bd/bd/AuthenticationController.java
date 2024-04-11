package bd.bd;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

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

        }
        if (user != null) {
            if ("admin".equals(user.getRole())) {
                app.switchWindow("adminWindow.fxml", "Admin Dashboard");
            } else if ("user".equals(user.getRole())) {
                app.switchWindow("userWindow.fxml", "User Dashboard");
            }
        } else {
            Alerts.showErr("Неверный логин или пароль. Пожалуйста, попробуйте снова.");
        }
    }
    public void registerButtonAction(ActionEvent actionEvent) {
        setApp(app);
        app.switchWindow("Registration.fxml","Регистрация");
    }
}
