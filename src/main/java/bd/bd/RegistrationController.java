package bd.bd;

import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
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
    public void setApp(MainApplication app) {
        this.app = app;
    }
/*    public static void setCurrentUserRole(String role) {
        this.currentUserRole = role;
        adjustUIBasedOnRole();
    }*/
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
        String role="user";
        String SQL_CHECK = "SELECT login FROM users WHERE login = ?";

        String SQL="INSERT INTO users (login, password, name, role) VALUES (?, crypt(?, gen_salt('bf')), ?, ?);\n";
        if(!pass.equals(second))
        {
            Alerts.showErr("Пароли не совпадают");
            return;
        }
            try (Connection conn = DatabaseHandler.connect();
                 PreparedStatement pstmtcheck = conn.prepareStatement(SQL_CHECK)) {
                pstmtcheck.setString(1,user);
                ResultSet resultSet;
                resultSet= pstmtcheck.executeQuery();
                while (resultSet.next())
                {
                    Alerts.showErr("Этот логин уже используется.");
                    return;
                }
                try(PreparedStatement pstmt = conn.prepareStatement(SQL)) {
                pstmt.setString(1, user);
                pstmt.setString(2, pass);
                pstmt.setString(3, name);
                pstmt.setString(4, role);
                int affected =pstmt.executeUpdate();
                if(affected>0)
                {
                    Alerts.showSuc("Успешно.");
                }
            }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                Alerts.showErrSQL(e.getMessage());
            }
        }

    public void backButtonAction(ActionEvent actionEvent) {

        setApp(app);
        app.showLoginWindow();
    }
}

