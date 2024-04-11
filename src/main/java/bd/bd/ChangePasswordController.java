package bd.bd;

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

    public void handleChangePassword(ActionEvent actionEvent) {
        String login = loginTextField.getText();
        String newPas= newPasswordField.getText();
        String confPas=confirmPasswordField.getText();
        if(!newPas.equals(confPas))
        {
            Alerts.showErr("Пароли не совпадают");
            return;
        }
        String SQL_UPDATE = "UPDATE users SET password = crypt(?, gen_salt('bf')) WHERE login = ? ";
        String SQL_CHECK = "SELECT login FROM users WHERE login = ?";

        try(Connection conn = DatabaseHandler.connect();
            PreparedStatement pstmtCheck = conn.prepareStatement(SQL_CHECK))
        {
            pstmtCheck.setString(1,login);
            ResultSet rs = pstmtCheck.executeQuery();
            if(rs.next())
            {
                PreparedStatement pstmUpd = conn.prepareStatement(SQL_UPDATE);
                pstmUpd.setString(1,newPas);
                pstmUpd.setString(2,login);
                int affcet = pstmUpd.executeUpdate();
                if(affcet>0)
                {
                    Alerts.showSuc("Пароль изменён.");
                }
                else
                {
                    Alerts.showErr("Пароль не изменён.");
                }
            }
            else
            {
                Alerts.showErr("Такого пользователя нет.");
                return;
            }

        }
        catch (SQLException e) {
            Alerts.showErrSQL(e.getMessage());
        }
    }
}
