package bd.bd;

import javafx.beans.binding.BooleanBinding;
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
        String SQL_CHECK = "SELECT login FROM users WHERE login = ?";

        String SQL="INSERT INTO users (login, password, name, role) VALUES (?, crypt(?, gen_salt('bf')), ?, ?);\n";
        if(!pass.equals(second))
        {
            Alerts.showErr("Пароли не совпадают");
            return;
        }
        try (Connection conn = DatabaseHandler.connect();
             PreparedStatement hatcheck = conn.prepareStatement(SQL_CHECK)) {
            hatcheck.setString(1,user);
            ResultSet resultSet= hatcheck.executeQuery();
            while (resultSet.next())
            {
                Alerts.showErr("Этот логин уже используется.");
                return;
            }
            try(PreparedStatement pstmt = conn.prepareStatement(SQL)) {
                pstmt.setString(1, user);
                pstmt.setString(2, pass);
                pstmt.setString(3, name);
                pstmt.setString(4, roleComboBox.getValue());
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
}
