package bd.bd;

import bd.bd.controllers.Alerts;
import bd.bd.controllers.DatabaseHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static bd.bd.controllers.DatabaseHandler.connect;

public class UserDAOImpl implements UserDAO {

    public User authenticateUser(String login, String password) {
        String SQL_SELECT = "SELECT * FROM users WHERE login = ? AND password = crypt(?, password)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL_SELECT)) {
            pstmt.setString(1, login);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("Успешная аутентификация пользователя: " + login);
                return new User(
                        rs.getInt("id"),
                        rs.getString("login"),
                        EncryptionUtility.decrypt(rs.getString("name")),
                        rs.getString("role")

                );
            } else {
                Alerts.showErr("Неудачная попытка аутентификации для пользователя: " + login);
            }
        } catch (SQLException ex) {
            Alerts.showErr(ex.getMessage());
        }
        return null;
    }

    public ObservableList<User> loadUsers()
    {
        ObservableList<User> users = FXCollections.observableArrayList();
        String SQL_SELECT = "SELECT id, login, name, role FROM users;\n";
        try (Connection conn = connect();
             PreparedStatement pstmtSelect = conn.prepareStatement(SQL_SELECT))
        {
            ResultSet rs = pstmtSelect.executeQuery();
            while(rs.next())
            {
                users.add(new User(rs.getInt("id"),
                        rs.getString("login"),
                        EncryptionUtility.decrypt(rs.getString("name")),
                        rs.getString("role")));
            }
        } catch (SQLException e) {
            Alerts.showErrSQL(e.getMessage());
        }
        return users;
    }
    public void registerUser(String pass, String user, String name, String role,String second)
    {
        String SQL_CHECK = "SELECT login FROM users WHERE login = ?";
        String SQL="INSERT INTO users (login, password, name, role) VALUES (?, crypt(?, gen_salt('bf')), ?, ?);\n";
        if(!pass.equals(second))
        {
            Alerts.showErr("Пароли не совпадают");
            return;
        }
        try (Connection conn = connect();
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
                pstmt.setString(3, EncryptionUtility.encrypt(name));
                pstmt.setString(4, role);
                int affected =pstmt.executeUpdate();
                if(affected>0)
                {
                    Alerts.showSuc("Успешно.");
                }
            }
        } catch (SQLException e) {
            Alerts.showErrSQL(e.getMessage());
        }

    }
    public void chacngePassword(String login,String newPas)
    {
        String SQL_UPDATE = "UPDATE users SET password = crypt(?, gen_salt('bf')) WHERE login = ? ";
        String SQL_CHECK = "SELECT login FROM users WHERE login = ?";

        try(Connection conn = connect();
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
    public void deleteUserFromDatabase(Integer id)
    {
        String SQL_DELETE="DELETE FROM users WHERE id = ?;\n";
        try (Connection conn = connect();
             PreparedStatement pstmtDelete = conn.prepareStatement(SQL_DELETE)){
            pstmtDelete.setInt(1,id);
            int affected = pstmtDelete.executeUpdate();
            if(affected>0)
            {
                Alerts.showSuc("Всё удалено");
            }
            else
            {
                Alerts.showErrSQL("Не удалено");
            }

        } catch (SQLException e) {
            Alerts.showErrSQL(e.getMessage());
        }
    }
    public void registerUserWithOutRole(String user, String pass, String name, String second)
    {
        String role="user";
        String SQL_CHECK = "SELECT login FROM users WHERE login = ?";
        String SQL="INSERT INTO users (login, password, name, role) VALUES (?, crypt(?, gen_salt('bf')), ?, ?);\n";
        if(!pass.equals(second))
        {
            Alerts.showErr("Пароли не совпадают");
            return;
        }
        try (Connection conn = connect();
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
                pstmt.setString(3, EncryptionUtility.encrypt(name));
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

}
